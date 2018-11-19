package me.dylancz.chatter;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class Storage {

    public UUID uuid;

    public static Storage fromBytes(final byte[] bytes) {
        final String string = new String(bytes, StandardCharsets.UTF_8);

        final Storage storage = new Storage();
        storage.uuid = UUID.fromString(string);
        return storage;
    }

    public byte[] toBytes() {
        final String string = this.uuid.toString();
        return string.getBytes();
    }

}
