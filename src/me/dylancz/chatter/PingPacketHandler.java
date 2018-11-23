package me.dylancz.chatter;

import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import me.dylancz.chatter.event.Subscribe;
import me.dylancz.chatter.event.packet.PacketReceivedEvent;
import me.dylancz.chatter.net.io.PacketWriter;
import me.dylancz.chatter.net.packet.PingPacket;
import me.dylancz.chatter.net.packet.PongPacket;

public class PingPacketHandler {

    private final PacketWriter writer;
    private final UUID selfUUID;

    private long lastPing;

    public PingPacketHandler(final PacketWriter writer,
                             final UUID selfUUID,
                             final ScheduledExecutorService executorService) {
        this.writer = writer;
        this.selfUUID = selfUUID;
        executorService.scheduleAtFixedRate(
            () -> {
                this.lastPing = System.currentTimeMillis();
                writer.write(new PingPacket());
            }, 0, 1, TimeUnit.SECONDS);
    }

    @Subscribe
    public void onPingPacket(final PacketReceivedEvent<PingPacket> event) {
        this.writer.write(new PongPacket(event.getSource().getUUID()));
    }

    @Subscribe
    public void onPongPacket(final PacketReceivedEvent<PongPacket> event) {
        if (!event.getPacket().getTarget().equals(this.selfUUID)) return;
        final long delta = System.currentTimeMillis() - this.lastPing;
        System.out.println("Received PongPacket: delta " + delta);
    }

}
