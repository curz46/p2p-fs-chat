package me.dylancz.chatter.file;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class FileHandle<T> {

    private static final Pattern FILENAME_NO_EXTENSION = Pattern.compile("^(.+)\\..+$");

    protected final File file;

    protected FileHandle(final File file) {
        this.file = file;
    }

    public File getFile() {
        return this.file;
    }

    public Optional<UUID> parseUUID() {
        final Matcher matcher = FILENAME_NO_EXTENSION.matcher(this.getFileName());
        if (!matcher.find()) return Optional.empty();
        final String str = matcher.group(1);
        return Optional.of(UUID.fromString(str));
    }

    public String getFileName() {
        return this.file.getName();
    }

    public abstract T open() throws IOException;

    public abstract void close() throws IOException;

    public abstract boolean isOpen();

}
