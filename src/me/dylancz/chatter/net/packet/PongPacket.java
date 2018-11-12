package me.dylancz.chatter.net.packet;

public class PongPacket extends Packet  {

    public PongPacket() {
        super(PacketType.PONG);
    }

    @Override
    public void read(final byte[] bytes) {}

    @Override
    public byte[] write() {
        return new byte[0];
    }

}
