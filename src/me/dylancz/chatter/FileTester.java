package me.dylancz.chatter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import me.dylancz.chatter.event.DefaultEventBus;
import me.dylancz.chatter.event.EventListener;
import me.dylancz.chatter.event.file.FileWatchEvent;
import me.dylancz.chatter.net.io.FileWatcher;
import me.dylancz.chatter.net.packet.PingPacket;

public class FileTester {

    public static void main(final String[] args) {
        final Path path = Paths.get("P:/p2ptest");
        final DefaultEventBus eventBus = new DefaultEventBus();

        eventBus.registerListener(EventListener.of(
            System.out::println,
            FileWatchEvent.Modified.class
        ));

        final FileWatcher watcher = new FileWatcher(path, eventBus);
        (new Thread(watcher)).start();

        final OutputFileHandle handle = new OutputFileHandle(path.resolve("somefile.txt").toFile());
        try {
            handle.open();
            for (int i = 0; i < 10; i++) {
                final PingPacket packet = new PingPacket();
                System.out.println("Writing packet...");
                handle.write(packet);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
