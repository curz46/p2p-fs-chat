package me.dylancz.chatter.net.packet;

public abstract class Packet {

    protected final PacketType type;

    public Packet(final PacketType type) {
        this.type = type;
    }

    public abstract void read(final byte[] bytes);

    public abstract byte[] write();

    public PacketType getType() {
        return this.type;
    }

}
