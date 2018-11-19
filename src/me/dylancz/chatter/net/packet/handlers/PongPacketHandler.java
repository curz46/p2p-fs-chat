package me.dylancz.chatter.net.packet.handlers;

import me.dylancz.chatter.net.io.FileWatcher;
import me.dylancz.chatter.net.packet.PongPacket;
import me.dylancz.chatter.user.User;

public class PongPacketHandler extends PacketHandler<PongPacket> {

    @Override
    public void handle(final User source, final PongPacket packet) {
//        final long delta = System.currentTimeMillis() - source.getPingTime();
//        System.out.println("PING: " + delta + "ms User{" + source.getPath() + "}... internal: " + (System.currentTimeMillis() - FileWatcher.lastMessage));
    }

}
