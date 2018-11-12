package me.dylancz.chatter.net.io;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Queue;
import me.dylancz.chatter.net.Event;
import me.dylancz.chatter.net.Event.EventType;

public class FileWatcher implements Runnable {

    public static long lastMessage;

    private final Path path;
    private final WatchService watcher;
    private final Queue<Event> queue;

    public FileWatcher(final Path path, final Queue<Event> queue) {
        this.path = path;
        try {
            this.watcher = FileSystems.getDefault().newWatchService();
        } catch (final IOException e) {
            throw new RuntimeException("Failed to create WatchService: ", e);
        }

        try {
            // ENTRY_CREATE: A new user has joined the network.
            // ENTRY_DELETE: An existing user has left the network.
            // ENTRY_MODIFY: An existing user has sent a packet.
            path.register(this.watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        } catch (final IOException e) {
            e.printStackTrace();
        }
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
                final WatchKey key = this.watcher.take();
                for (final WatchEvent event : key.pollEvents()) {
                    final Path relative = (Path) event.context();
                    final Path path = this.path.resolve(relative);
                    if (event.kind() == ENTRY_CREATE) {
                        this.queue.add(new Event(EventType.USER_CONNECT, path));
                    } else if (event.kind() == ENTRY_DELETE) {
                        this.queue.add(new Event(EventType.USER_DISCONNECT, path));
                    } else if (event.kind() == ENTRY_MODIFY) {
                        this.queue.add(new Event(EventType.USER_MESSAGE, path));
                        lastMessage = System.currentTimeMillis();
                    }
                }
                key.reset();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
