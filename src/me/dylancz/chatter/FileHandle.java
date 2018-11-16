package me.dylancz.chatter;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.regex.Pattern;

public abstract class FileHandle {

    private static final Pattern FILENAME_NO_EXTENSION = Pattern.compile("^(.+)\\..+$");

    protected final File file;

    protected FileHandle(final File file) {
        this.file = file;
    }

    public File getFile() {
        return this.file;
    }

    public UUID getIdentifier() {
        final String str = FILENAME_NO_EXTENSION.matcher(this.getFileName()).group(0);
        return UUID.fromString(str);
    }

    public String getFileName() {
        return this.file.getName();
    }

    public abstract void open() throws IOException;

    public abstract boolean isOpen();

}
