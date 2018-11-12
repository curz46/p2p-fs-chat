package me.dylancz.chatter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import me.dylancz.chatter.net.Event;
import me.dylancz.chatter.net.EventRouter;
import me.dylancz.chatter.net.io.FileWatcher;
import me.dylancz.chatter.net.io.PacketIO;
import me.dylancz.chatter.net.packet.ConnectedPacket;
import me.dylancz.chatter.net.packet.PingPacket;
import me.dylancz.chatter.net.packet.handlers.ConnectedPacketHandler;
import me.dylancz.chatter.user.User;
import me.dylancz.chatter.user.UserHandler;

public class Chatter {

    // The root directory of the p2p application. This is where all communication is done.
    private final String path;

    public static File file;

    public Chatter(final String path) {
        this.path = path;
    }

    public void start() throws IOException {
        System.out.println("Initialising...");

        final File file = new File(path, UUID.randomUUID() + ".txt");

        final UserHandler userHandler = new UserHandler();
        final PacketIO packetIO = new PacketIO();

//        final Thread onShutdown = new Thread(file::delete);
//        Runtime.getRuntime().addShutdownHook(onShutdown);

        final File[] existingFiles = Paths.get(this.path).toFile().listFiles();
        if (existingFiles != null) {
            final long now = System.currentTimeMillis();
            for (final File existing : existingFiles) {
                if (now - existing.lastModified() > 1000 * 5) {
                    existing.delete();
                } else {
                    System.out.println("Found existing User{" + existing.toPath() + "}");
                    final User user = userHandler.createUser(existing.toPath());
                    user.setByteOffset(existing.length());
                }
            }
        }

        if (!file.exists()) {
            System.out.println("File does not exist, creating...");
            file.getParentFile().mkdirs();
            file.createNewFile();
            System.out.println("Created File.");
        }

        final BlockingQueue<Event> queue = new LinkedBlockingQueue<>(10);
        final FileWatcher watcher = new FileWatcher(Paths.get(this.path), queue);
        System.out.println("Watching...");
        watcher.start();

        new EventRouter(packetIO, userHandler, queue).start();

        System.out.println("Ready. Sending ConnectedPacket...");

        final ConnectedPacket packet = new ConnectedPacket();
        packet.setVersion(ConnectedPacketHandler.CURRENT_VERSION);

        System.out.println("Sent.");

        Chatter.file = file;

        long lastPing = 0;
        while (true) {
            final long now = System.currentTimeMillis();
            if (now - 1000 > lastPing) {
                lastPing = now;
                final PingPacket ping = new PingPacket();
                PacketIO.write(file, ping);
                userHandler.getUsers().forEach(user -> user.setPingTime(now));
            }
        }
    }

}
