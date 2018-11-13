package me.dylancz.chatter.event.packet;

import me.dylancz.chatter.net.packet.Packet;
import me.dylancz.chatter.user.User;

public class PacketReceivedEvent<T extends Packet> extends PacketEvent<T> {

    private final User source;

    public PacketReceivedEvent(final T packet, final User source) {
        super(packet);
        this.source = source;
    }

    public User getSource() {
        return this.source;
    }

}
