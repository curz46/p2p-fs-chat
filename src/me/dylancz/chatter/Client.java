package me.dylancz.chatter;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;

public class Client {

    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    private final Path path;

    public static void main(final String[] args) {
        if (args.length != 1) {
            throw new RuntimeException("");
        }
        final Path path = Paths.get(args[0]);
        (new Client(path)).start();
    }

    public Client(final Path path) {
        this.path = path;
    }

    public void start() {
        final Bootstrapper bootstrapper = new Bootstrapper();
        final Set<InputFileHandle> handles = bootstrapper.findExistingFiles(this.path);
        System.out.println(
            handles.stream()
                .map(FileHandle::getIdentifier)
                .collect(Collectors.toSet())
        );
    }

}
