package me.dylancz.chatter.net;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import me.dylancz.chatter.net.io.PacketIO;
import me.dylancz.chatter.net.packet.Packet;
import me.dylancz.chatter.net.packet.PacketRegistry;
import me.dylancz.chatter.net.packet.handlers.PacketHandler;
import me.dylancz.chatter.user.UserHandler;

public class EventRouter implements Runnable {

    private final PacketIO reader;
    private final UserHandler userHandler;
    private final BlockingQueue<Event> queue;

    public EventRouter(final PacketIO reader, final UserHandler userHandler,
                       final BlockingQueue<Event> queue) {
        this.reader = reader;
        this.userHandler = userHandler;
        this.queue = queue;
    }

    public Thread start() {
        final Thread thread = new Thread(this);
        thread.start();
        return thread;
    }

    @Override
    public void run() {
        while (true) {
            try {
                final Event event = this.queue.take();
                switch (event.getType()) {
                    case USER_CONNECT:
                        this.connect(event.getPath());
                        break;
                    case USER_DISCONNECT:
                        this.disconnect(event.getPath());
                        break;
                    case USER_MESSAGE:
                        this.message(event.getPath());
                        break;
                    default:
                        throw new RuntimeException("Undefined Event type.");
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void connect(final Path path) {
        System.out.println("Connected: " + path);
        // Create a new User for this Path
//        this.userHandler.createUser(path);
    }

    public void disconnect(final Path path) {
        System.out.println("Disconnected: " + path);
        // Delete the User for this Path
    }

    public void message(final Path path) {
//        try {
//            // TODO: This is really hacky, but it seems like it's necessary to deal with the network
//            // delay.
//            Thread.sleep(50);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        System.out.println("Message: " + path);
        // Parse and handle the message content as a Packet
//        this.userHandler.findUserForPath(path)
//            .ifPresent(user -> {
//                final File file = path.toFile();
//                final List<Packet> packets = PacketIO.read(file, user.getByteOffset());
//                for (final Packet packet : packets) {
//                    final PacketHandler handler = PacketRegistry.getHandler(packet.getType());
//                    //noinspection unchecked
//                    handler.handle(user, packet);
//                }
//                user.setByteOffset(file.length());
//            });
    }

}
