package me.dylancz.chatter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Bootstrapper {

    public Set<InputFileHandle> findExistingFiles(final Path root) {
        final Set<InputFileHandle> handles = new HashSet<>();

        final File folder = root.toFile();
        final File[] entries = folder.listFiles();
        if (entries == null) return handles;

        for (final File file : entries) {
            handles.add(new InputFileHandle(file));
        }
        return handles;
    }

    public UUID readOrGenerateUUID(final Path storagePath) {
        UUID uuid;
        try {
            final Storage storage = this.readStorage(storagePath);
            uuid = storage.uuid;
        } catch (final FileNotFoundException e) {
            try {
                uuid = UUID.randomUUID();
                final Storage storage = new Storage();
                storage.uuid = uuid;
                this.writeStorage(storagePath, storage);
            } catch (IOException e1) {
                throw new RuntimeException(
                    "Unable to create new Storage file (" + storagePath + "):", e1
                );
            }
        } catch (final IOException e) {
            throw new RuntimeException("An unexpected error occurred:", e);
        }
        return uuid;
    }

    public Storage readStorage(final Path storageFile) throws IOException {
        DataInputStream in = null;
        try {
            final File file = storageFile.toFile();
            in = new DataInputStream(new FileInputStream(file));
            final int length = in.available();
            final byte[] bytes = new byte[length];
            in.read(bytes, 0, length);
            return Storage.fromBytes(bytes);
        } finally {
            if (in != null) in.close();
        }
    }

    public void writeStorage(final Path storageFile, final Storage storage) throws IOException {
        DataOutputStream out = null;
        try {
            final File file = storageFile.toFile();
            out = new DataOutputStream(new FileOutputStream(file));
            final byte[] bytes = storage.toBytes();
            out.write(bytes, 0, bytes.length);
        } finally {
            if (out != null) out.close();
        }
    }

}
