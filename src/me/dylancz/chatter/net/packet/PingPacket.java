package me.dylancz.chatter.net.packet;

public class PingPacket extends Packet {

    public PingPacket() {
        super(PacketType.PING);
    }

    @Override
    public void read(final byte[] bytes) {}

    @Override
    public byte[] write() {
        return new byte[0];
    }

}
