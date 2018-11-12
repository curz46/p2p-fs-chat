package me.dylancz.chatter.net.io;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedList;
import java.util.List;
import me.dylancz.chatter.net.packet.Packet;
import me.dylancz.chatter.net.packet.PacketType;

public class PacketIO {

    public static synchronized List<Packet> read(final File file, final long byteOffset) {
        final List<Packet> packets = new LinkedList<>();
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(file, "r");
            randomAccessFile.seek(byteOffset);
            // TODO: This probably won't work.
            while (randomAccessFile.getFilePointer() != randomAccessFile.length()) {
                // Packets follow the given format...
                // ==================================
                // byte; packet id
                // byte; target type
                // if target type == user
                //   string; UUID
                // int; length
                // bytes; content
                final int packetId = randomAccessFile.readByte();
                final int length = randomAccessFile.readInt();
                final byte[] bytes = new byte[length];
                randomAccessFile.read(bytes, 0, length);

//                final Packet packet = PacketRegistry.createPacket(PacketType.fromId(packetId));
                final Packet packet = PacketType.fromId(packetId).createPacket();
                packet.read(bytes);
                packets.add(packet);
            }
        } catch (final IOException e) {
            throw new RuntimeException("Unable to read packets from File: ", e);
        } finally {
            if (randomAccessFile != null) {
                try {
                    randomAccessFile.close();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return packets;
    }

    public static synchronized void write(final File file, final Packet packet) {
        final DataOutputStream out;
        try {
            out = new DataOutputStream(new FileOutputStream(file, true));
            // byte; packet id
            // byte; target type
            // if target type == user
            //   string; UUID
            // int; length
            // bytes; content
            final PacketType type = packet.getType();
            out.writeByte(type.getId());
            final byte[] bytes = packet.write();
            out.writeInt(bytes.length);
            out.write(bytes, 0, bytes.length);

            out.flush();
            out.close();
        } catch (IOException e) {
            throw new RuntimeException("Unable to write packets to File: " + e);
        }

    }

}
