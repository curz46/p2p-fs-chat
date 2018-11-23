package me.dylancz.chatter;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;
import me.dylancz.chatter.event.EventBus;
import me.dylancz.chatter.user.User;
import me.dylancz.chatter.user.UserHandler;

public class Client {

    public static final String STORAGE_NAME = ".storage";

    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    private final Path root;
    private final Path home;

    public static void main(final String[] args) {
        if (args.length != 2) {
            throw new RuntimeException("args[0] != root path");
        }
        final Path root = Paths.get(args[0]);
        final Path home = Paths.get(args[1]);
        (new Client(root, home)).start();
    }

    public Client(final Path root, final Path home) {
        this.root = root;
        this.home = home;
    }

    public void start() {
        final Bootstrapper bootstrapper = new Bootstrapper();
        final UserHandler userHandler = new UserHandler(
            // Get a Collection of the Users which are currently online.
            bootstrapper
                .findExistingFiles(this.root).stream()
                .map(User::fromHandle)
                .collect(Collectors.toSet())
        );
//        // Initialise the Buses
        final EventBus eventBus = new EventBus();
//        final PacketBus packetBus = new PacketBus();

        // Get UUID from Storage
        final Path storagePath = this.home.resolve(STORAGE_NAME);
        final UUID uuid = bootstrapper.readOrGenerateUUID(storagePath);
        if (uuid == null) throw new RuntimeException("readOrGenerateUUID(...) == null");

        // Initialise the FileWatcher
        
    }

}
