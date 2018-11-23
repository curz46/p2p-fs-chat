package me.dylancz.chatter.packet;

import java.io.UnsupportedEncodingException;
import java.util.UUID;
import me.dylancz.chatter.util.ByteBuf;

public class PongPacket extends Packet  {

    private UUID target;

    public PongPacket() {
        super(PacketType.PONG);
    }

    public PongPacket(final UUID target) {
        this();
        this.target = target;
    }

    @Override
    public void read(final byte[] bytes) {
        final ByteBuf buf = ByteBuf.wrap(bytes);
        this.target = UUID.fromString(buf.readString(36));
    }

    @Override
    public byte[] write() {
        try {
            return this.target.toString().getBytes("UTF-8");
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException("Invalid encoding when converting String to Bytes: ", e);
        }
    }

    public UUID getTarget() {
        return this.target;
    }

    public void setTarget(UUID target) {
        this.target = target;
    }

}
