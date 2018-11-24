package me.dylancz.chatter.file;

import me.dylancz.chatter.event.DefaultEventBus;
import me.dylancz.chatter.event.file.FileWatchEvent;

import java.io.IOException;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.*;

public class FileWatcher implements Runnable {

    private final Path root;
    private final WatchService watcher;
    private final DefaultEventBus eventBus;

    public FileWatcher(final Path root, final DefaultEventBus eventBus) {
        this.root = root;
        try {
            this.watcher = FileSystems.getDefault().newWatchService();
        } catch (final IOException e) {
            throw new RuntimeException("Failed to create WatchService: ", e);
        }

        try {
            root.register(this.watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        } catch (final IOException e) {
            e.printStackTrace();
        }
        this.eventBus = eventBus;
    }

    @Override
    public void run() {
        while (true) {
            try {
                final WatchKey key = this.watcher.take();
                for (final WatchEvent event : key.pollEvents()) {
                    final Path relative = (Path) event.context();
                    final Path path = this.root.resolve(relative);
                    if (event.kind() == ENTRY_CREATE) {
                        this.eventBus.post(new FileWatchEvent.Created(path));
                    } else if (event.kind() == ENTRY_DELETE) {
                        System.out.println("delete");
                        this.eventBus.post(new FileWatchEvent.Deleted(path));
                    } else if (event.kind() == ENTRY_MODIFY) {
                        this.eventBus.post(new FileWatchEvent.Modified(path));
                    }
                }
                key.reset();
            } catch (final InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
