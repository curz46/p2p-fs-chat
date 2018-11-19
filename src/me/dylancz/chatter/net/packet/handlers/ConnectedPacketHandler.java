package me.dylancz.chatter.net.packet.handlers;

import me.dylancz.chatter.net.packet.ConnectedPacket;
import me.dylancz.chatter.user.User;

public class ConnectedPacketHandler extends PacketHandler<ConnectedPacket> {

    public static final int CURRENT_VERSION = 0x01;

    @Override
    public void handle(final User source, final ConnectedPacket packet) {
//        System.out.println("User{" + source.getPath() + "} connected, version: " + packet.getVersion());
    }

}
