package me.dylancz.chatter.net.packet.handlers;

import me.dylancz.chatter.Chatter;
import me.dylancz.chatter.net.io.PacketIO;
import me.dylancz.chatter.net.packet.PingPacket;
import me.dylancz.chatter.net.packet.PongPacket;
import me.dylancz.chatter.user.User;

public class PingPacketHandler extends PacketHandler<PingPacket> {

    @Override
    public void handle(final User source, final PingPacket packet) {
        PacketIO.write(Chatter.file, new PongPacket());
    }

}
