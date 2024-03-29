package me.dylancz.chatter.event.packet;

import me.dylancz.chatter.event.ParameterisedEvent;
import me.dylancz.chatter.packet.Packet;

public abstract class PacketEvent<T extends Packet> extends ParameterisedEvent {

    protected final T packet;

    PacketEvent(final T packet) {
        this.packet = packet;
    }

    public T getPacket() {
        return this.packet;
    }

    @Override
    public Class<?>[] getTypeParameters() {
        return new Class<?>[] { this.packet.getClass() };
    }

}
