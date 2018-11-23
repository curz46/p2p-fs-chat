package me.dylancz.chatter.packet;

import me.dylancz.chatter.util.ByteBuf;

public class ConnectedPacket extends Packet {

    private int version;

    public ConnectedPacket() {
        super(PacketType.CONNECTED);
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getVersion() {
        return this.version;
    }

    @Override
    public void read(final byte[] bytes) {
        final ByteBuf buf = ByteBuf.wrap(bytes);
        this.version = buf.readInt();
    }

    @Override
    public byte[] write() {
        final ByteBuf buf = ByteBuf.allocate(4);
        buf.writeInt(this.version);
        return buf.getByteArray();
    }

}
