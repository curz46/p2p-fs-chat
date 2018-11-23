package me.dylancz.chatter.packet;

import java.util.function.Supplier;

public enum PacketType {

    CONNECTED(0, ConnectedPacket.class, ConnectedPacket::new),
    PING(1, PingPacket.class, PingPacket::new),
    PONG(2, PongPacket.class, PongPacket::new);

    private final int id;
    private final Class<? extends Packet> packetClass;
    private final Supplier<Packet> packetFn;

    PacketType(final int id, final Class<? extends Packet> packetClass, final Supplier<Packet> packetFn) {
        this.id = id;
        this.packetClass = packetClass;
        this.packetFn = packetFn;
    }

    public static PacketType fromId(final int id) {
        for (final PacketType type : PacketType.values()) {
            if (type.getId() == id) return type;
        }
        throw new RuntimeException("Unknown Packet ID: " + id);
    }

    public static PacketType fromClass(final Class<? extends Packet> packetClass) {
        for (final PacketType type : PacketType.values()) {
            if (type.getPacketClass() == packetClass) return type;
        }
        throw new RuntimeException("Unrecognised Packet class: " + packetClass);
    }

    public int getId() {
        return this.id;
    }

    public Class<? extends Packet> getPacketClass() {
        return this.packetClass;
    }

    public Supplier<Packet> getPacketSupplier() {
        return this.packetFn;
    }

    public Packet createPacket() {
        return this.packetFn.get();
    }

}
