package me.dylancz.chatter.event.file;

import java.nio.file.Path;
import me.dylancz.chatter.event.Event;

public abstract class FileWatchEvent extends Event {

    private final Path path;

    FileWatchEvent(final Path path) {
        this.path = path;
    }

    public Path getPath() {
        return this.path;
    }

    public static class Created extends FileWatchEvent {

        public Created(final Path path) {
            super(path);
        }

    }

    public static class Deleted extends FileWatchEvent {

        public Deleted(Path path) {
            super(path);
        }

    }

    public static class Modified extends FileWatchEvent {

        public Modified(Path path) {
            super(path);
        }

    }

}
