package me.dylancz.chatter.net.packet;

import java.util.HashMap;
import java.util.Map;
import me.dylancz.chatter.net.packet.handlers.ConnectedPacketHandler;
import me.dylancz.chatter.net.packet.handlers.PacketHandler;
import me.dylancz.chatter.net.packet.handlers.PingPacketHandler;
import me.dylancz.chatter.net.packet.handlers.PongPacketHandler;

public class PacketRegistry {

//    private static final Map<Integer, Pair<Class<Packet>, PacketHandler>> packets = new HashMap<>();

    private static final Map<PacketType, Class<Packet>> packets = new HashMap<>();
    private static final Map<PacketType, PacketHandler> handlers = new HashMap<>();

    public static <T extends Packet> void register(final PacketType type, final Class<T> packetClazz,
                                            final PacketHandler<T> handler) {
        //noinspection unchecked
        packets.put(type, (Class<Packet>) packetClazz);
        handlers.put(type, handler);
    }

    public static Packet createPacket(final PacketType type) {
        final Class<Packet> clazz = packets.get(type);
        try {
            return clazz.newInstance();
        } catch ( InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Failed to create Packet{" + type + "}: " + e);
        }
    }

    public static PacketHandler getHandler(final PacketType type) {
        return handlers.get(type);
    }

    static {
        register(PacketType.CONNECTED, ConnectedPacket.class, new ConnectedPacketHandler());
        register(PacketType.PING, PingPacket.class, new PingPacketHandler());
        register(PacketType.PONG, PongPacket.class, new PongPacketHandler());
    }

}
