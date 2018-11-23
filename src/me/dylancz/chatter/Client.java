package me.dylancz.chatter;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import me.dylancz.chatter.event.DefaultEventBus;
import me.dylancz.chatter.net.io.FileWatcher;
import me.dylancz.chatter.net.io.PacketWriter;
import me.dylancz.chatter.net.packet.PingPacket;
import me.dylancz.chatter.user.User;
import me.dylancz.chatter.user.UserHandler;

public class Client {

    public static final String STORAGE_NAME = ".storage";

    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    private final Path root;
    private final Path home;

    public static void main(final String[] args) {
//        if (args.length != 2) {
//            throw new RuntimeException("args: <root> <home>");
//        }
//        final Path root = Paths.get(args[0]);
//        final Path home = Paths.get(args[1]);
        (new Client(Paths.get("P:/p2ptest/"), Paths.get("P:/"))).start();
    }

    public Client(final Path root, final Path home) {
        this.root = root;
        this.home = home;
    }

    public void start() {
        // Initialise the EventBus
        final DefaultEventBus eventBus = new DefaultEventBus();
        (new Thread(eventBus)).start();

        // Initialise the FileWatcher
        final FileWatcher fileWatcher = new FileWatcher(this.root, eventBus);
        (new Thread(fileWatcher)).start();

        final Bootstrapper bootstrapper = new Bootstrapper();
        final UserHandler userHandler = new UserHandler();

        // Get UUID from Storage
//        final Path storagePath = this.home.resolve(STORAGE_NAME);
//        final UUID uuid = bootstrapper.readOrGenerateUUID(storagePath);
//        if (uuid == null) throw new RuntimeException("readOrGenerateUUID(...) == null");
        final UUID uuid = UUID.randomUUID();
        System.out.println("Self UUID: " + uuid);

        // Register the FileWatchListener in advance such that Users can't come online while the
        // listener is not yet registered.
        eventBus.registerListeners(new FileWatchListener(userHandler, eventBus, uuid));

        userHandler.addUsers(
            // Get a Collection of the Users which are currently online.
            bootstrapper
                .findExistingFiles(this.root).stream()
                .map(User::fromHandle)
                .collect(Collectors.toSet())
        );

        final Path selfPath = this.root.resolve(uuid.toString() + ".txt");
        final OutputFileHandle selfHandle = new OutputFileHandle(selfPath.toFile());
        final PacketWriter writer = new PacketWriter(selfHandle, eventBus);

        eventBus.registerListeners(new PingPacketHandler(writer, uuid, this.executorService));
    }

}
