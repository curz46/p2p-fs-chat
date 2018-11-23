package me.dylancz.chatter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import me.dylancz.chatter.net.packet.Packet;
import me.dylancz.chatter.net.packet.PacketType;

public class InputFileHandle extends FileHandle<RandomAccessFile> {

    private RandomAccessFile randomAccessFile;

    protected InputFileHandle(final File file) {
        super(file);
    }

    public synchronized List<Packet> read() {
        if (!this.isOpen()) {
            throw new RuntimeException("InputFileHandle cannot read while not open!");
        }
        final List<Packet> packets = new ArrayList<>();
        try {
//            randomAccessFile.seek(byteOffset);
            while (this.randomAccessFile.getFilePointer() != this.randomAccessFile.length()) {
                // Packets follow the given format...
                // ==================================
                // byte; packet id
                // int; length
                // bytes; content
                final int packetId = this.randomAccessFile.readByte();
                final int length = this.randomAccessFile.readInt();
                final byte[] bytes = new byte[length];
                this.randomAccessFile.read(bytes, 0, length);

                final Packet packet = PacketType.fromId(packetId).createPacket();
                packet.read(bytes);
                packets.add(packet);
            }
        } catch (final IOException e) {
            throw new RuntimeException("Unable to read packets from File: ", e);
        }
        return packets;
    }

    @Override
    public RandomAccessFile open() throws IOException {
        if (this.isOpen()) return this.randomAccessFile;
        this.randomAccessFile = new RandomAccessFile(super.file, "r");
        // Assume that we should be reading only new changes to the File
        this.randomAccessFile.seek(this.randomAccessFile.length());
        return this.randomAccessFile;
    }

    @Override
    public void close() throws IOException {
        this.randomAccessFile.close();
        this.randomAccessFile = null;
    }

    @Override
    public boolean isOpen() {
        return this.file != null &&
            this.randomAccessFile != null &&
            this.randomAccessFile.getChannel().isOpen();
    }

}
