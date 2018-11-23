package me.dylancz.chatter.net.io;

import java.io.FileNotFoundException;
import me.dylancz.chatter.OutputFileHandle;
import me.dylancz.chatter.event.DefaultEventBus;
import me.dylancz.chatter.event.packet.PacketSendEvent;
import me.dylancz.chatter.net.packet.Packet;

public class PacketWriter {

    private final OutputFileHandle handle;
    private final DefaultEventBus eventBus;

    public PacketWriter(final OutputFileHandle handle, final DefaultEventBus eventBus) {
        this.handle = handle;
        this.eventBus = eventBus;
    }

    @SuppressWarnings("unchecked")
    public void write(final Packet packet) {
        this.eventBus.post(new PacketSendEvent.Pre(packet));
        try {
            this.handle.open();
            this.handle.write(packet);
        } catch (final FileNotFoundException e) {
            throw new RuntimeException("Unable to write Packet:", e);
        }
        this.eventBus.post(new PacketSendEvent.Post(packet));
    }

}
