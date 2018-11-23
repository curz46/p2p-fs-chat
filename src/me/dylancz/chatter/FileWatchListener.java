package me.dylancz.chatter;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;
import me.dylancz.chatter.event.DefaultEventBus;
import me.dylancz.chatter.event.Subscribe;
import me.dylancz.chatter.event.file.FileWatchEvent;
import me.dylancz.chatter.event.packet.PacketReceivedEvent;
import me.dylancz.chatter.net.packet.Packet;
import me.dylancz.chatter.user.User;
import me.dylancz.chatter.user.UserHandler;

public class FileWatchListener {

    private final UserHandler userHandler;
    private final DefaultEventBus eventBus;
    private final UUID selfUUID;

    public FileWatchListener(final UserHandler userHandler, final DefaultEventBus eventBus,
                             final UUID selfUUID) {
        this.userHandler = userHandler;
        this.eventBus = eventBus;
        this.selfUUID = selfUUID;
    }

    @Subscribe
    public void onFileCreated(final FileWatchEvent.Created event) {
        final Path path = event.getPath();
        final InputFileHandle handle = new InputFileHandle(path.toFile());
        final UUID uuid = handle.parseUUID()
            .orElseThrow(() -> new RuntimeException("Unable to parse UUID from File name: " + path));
        // If the UUID is the ID of this Client, ignore the Event.
        if (this.selfUUID.equals(uuid)) return;
        this.userHandler.addUser(User.fromHandle(handle));
        System.out.println("User created: " + path);
    }

    @Subscribe
    public void onFileDeleted(final FileWatchEvent.Deleted event) {
        final Path path = event.getPath();
        for (final User user : this.userHandler.getUsers()) {
            if (user.getHandle().getFile().toPath().equals(path)) {
                this.userHandler.removeUser(user);
                break;
            }
        }
        System.out.println("User removed: " + path);
    }

    @Subscribe
    public void onFileModified(final FileWatchEvent.Modified event) {
        final Path path = event.getPath();
        final Optional<User> optUser = this.userHandler.getUsers().stream()
            .filter(user -> user.getHandle().getFile().toPath().equals(path))
            .findAny();
        // If there is no User for this Path, we can assume that it's a Message that we sent.
        // Therefore, we can ignore the Event.
        if (!optUser.isPresent()) return;
        final User user = optUser.get();
        final InputFileHandle handle = user.getHandle();
        try {
            handle.open();
            for (final Packet packet : handle.read()) {
                //noinspection unchecked
                this.eventBus.post(new PacketReceivedEvent(packet, user));
            }
        } catch (final IOException e) {
            throw new RuntimeException("An exception occurred while trying to open an InputFileHandle.");
        }
    }

}
