package me.dylancz.chatter.event.packet;

import me.dylancz.chatter.event.Event;
import me.dylancz.chatter.net.packet.Packet;

public abstract class PacketEvent<T extends Packet> extends Event {

    protected final T packet;

    public PacketEvent(final T packet) {
        this.packet = packet;
    }

    public T getPacket() {
        return this.packet;
    }

}
