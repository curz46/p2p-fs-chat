package me.dylancz.chatter.net;

import java.nio.file.Path;

public class Event {

    public enum EventType {
        USER_CONNECT, USER_DISCONNECT, USER_MESSAGE
    }

    private final EventType type;
    private final Path path;

    public Event(final EventType type, final Path path) {
        this.type = type;
        this.path = path;
    }

    public EventType getType() {
        return this.type;
    }

    public Path getPath() {
        return this.path;
    }

}
