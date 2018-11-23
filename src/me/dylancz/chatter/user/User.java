package me.dylancz.chatter.user;

import java.util.UUID;
import me.dylancz.chatter.InputFileHandle;

public class User {

    private final UUID uuid;
    private InputFileHandle handle;

    public User(final UUID uuid) {
        this.uuid = uuid;
    }

    public static User fromHandle(final InputFileHandle handle) {
        final UUID uuid = handle.parseUUID()
            .orElseThrow(() -> new RuntimeException(""));
        final User user = new User(uuid);
        user.setHandle(handle);
        return user;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public InputFileHandle getHandle() {
        return this.handle;
    }

    public void setHandle(final InputFileHandle handle) {
        this.handle = handle;
    }

}
