package me.dylancz.chatter;

import java.io.File;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

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

}
