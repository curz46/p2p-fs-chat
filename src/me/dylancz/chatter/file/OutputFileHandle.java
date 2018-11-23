package me.dylancz.chatter.file;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import me.dylancz.chatter.packet.Packet;
import me.dylancz.chatter.packet.PacketType;

public class OutputFileHandle extends FileHandle {

    private FileOutputStream stream;
    private DataOutputStream out;

    public OutputFileHandle(final File file) {
        super(file);
    }

    public synchronized void write(final Packet packet) {
        if (!this.isOpen()) {
            throw new RuntimeException("OutputFileHandle cannot write a Packet while not open");
        }
        try {
            // byte; packet id
            // int; length
            // bytes; content
            final PacketType type = packet.getType();
            this.out.writeByte(type.getId());
            final byte[] bytes = packet.write();
            this.out.writeInt(bytes.length);
            this.out.write(bytes, 0, bytes.length);
            // This line is necessary in order to make sure that the WatchService accurately
            // determine that the File has changed.
            super.file.setLastModified(System.currentTimeMillis());
            this.out.flush();
        } catch (final IOException e) {
            throw new RuntimeException("Unable to write packets to File: " + e);
        }
    }

    @Override
    public DataOutputStream open() throws FileNotFoundException {
        if (this.isOpen()) return this.out;
        this.stream = new FileOutputStream(super.file);
        return this.out = new DataOutputStream(this.stream);
    }

    @Override
    public void close() throws IOException {
        this.out.close();
        this.out = null;
        this.stream = null;
    }

    @Override
    public boolean isOpen() {
        return this.stream != null && this.out != null && this.stream.getChannel().isOpen();
    }

}
