package me.dylancz.chatter.packet;

import me.dylancz.chatter.util.ByteBuf;

public class MessagePacket extends Packet {

    private String content;

    public MessagePacket() {
        super(PacketType.MESSAGE);
    }

    public MessagePacket(final String content) {
        this();
        this.content = content;
    }

    @Override
    public void read(final byte[] bytes) {
        final ByteBuf buf = ByteBuf.wrap(bytes);
        this.content = buf.readLengthedString();
    }

    @Override
    public byte[] write() {
        final byte[] bytes = content.getBytes();
        final ByteBuf buf = ByteBuf.allocate(4 + bytes.length);
        buf.writeLengthedString(content);
        return buf.getByteArray();
    }

    public String getContent() {
        return this.content;
    }

}
