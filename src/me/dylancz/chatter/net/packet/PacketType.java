package me.dylancz.chatter.net.packet;

import java.util.function.Supplier;

public enum PacketType {

    CONNECTED(0, ConnectedPacket::new),
    PING(1, PingPacket::new),
    PONG(2, PongPacket::new);

    private final int id;
    private final Supplier<Packet> packetFn;

    PacketType(final int id, final Supplier<Packet> packetFn) {
        this.id = id;
        this.packetFn = packetFn;
    }

    public static PacketType fromId(final int id) {
        for (PacketType type : PacketType.values()) {
            if (type.getId() == id) return type;
        }
        throw new RuntimeException("Unknown Packet ID: " + id);
    }

    public int getId() {
        return this.id;
    }

    public Supplier<Packet> getPacketSupplier() {
        return this.packetFn;
    }

    public Packet createPacket() {
        return this.packetFn.get();
    }

}
