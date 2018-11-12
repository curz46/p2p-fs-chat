package me.dylancz.chatter.util;

import me.dylancz.chatter.net.packet.Packet;
import me.dylancz.chatter.net.packet.PacketRegistry;
import me.dylancz.chatter.net.packet.PacketType;

// byte; packet id
// int; length
// byte array; content
public class PacketUtil {

    public static byte[] toBytes(final Packet packet) {
        final byte[] content = packet.write();
        final ByteBuf buf = ByteBuf.allocate(1 + 4 + content.length);
        buf.writeByte((byte) packet.getType().getId());
        buf.writeInt(content.length);
        buf.writeBytes(content);
        return buf.getByteArray();
    }

    public static Packet toPacket(final byte[] bytes) {
        final ByteBuf buf = ByteBuf.wrap(bytes);
        final byte id = buf.readByte();
        final int length = buf.readInt();
        final byte[] content = buf.readBytes(length);

        final Packet packet = PacketRegistry.createPacket(PacketType.fromId(id));
        packet.read(content);
        return packet;
    }

}
