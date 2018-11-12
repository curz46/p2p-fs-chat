package me.dylancz.chatter.user;

import java.nio.file.Path;

public class User {

    private final Path path;
    private long byteOffset;
    private long pingSent;

    public User(final Path path) {
        this.path = path;
    }

    public void setByteOffset(final long byteOffset) {
        this.byteOffset = byteOffset;
    }

    public void setPingTime(final long pingSent) {
        this.pingSent = pingSent;
    }

    public Path getPath() {
        return this.path;
    }

    public long getByteOffset() {
        return this.byteOffset;
    }

    public long getPingTime() {
        return this.pingSent;
    }

}
