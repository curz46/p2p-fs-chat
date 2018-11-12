package me.dylancz.chatter.net.packet.handlers;

import me.dylancz.chatter.net.packet.Packet;
import me.dylancz.chatter.user.User;

public abstract class PacketHandler<T extends Packet> {

    public abstract void handle(final User source, final T packet);

}
