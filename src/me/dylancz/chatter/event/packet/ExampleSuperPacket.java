package me.dylancz.chatter.event.packet;

import me.dylancz.chatter.net.packet.Packet;
import me.dylancz.chatter.net.packet.PacketType;

public abstract class ExampleSuperPacket extends Packet {

    public ExampleSuperPacket(PacketType type) {
        super(type);
    }

    @Override
    public void read(byte[] bytes) {}

    @Override
    public byte[] write() {
        return new byte[0];
    }

}
