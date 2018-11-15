package me.dylancz.chatter.event.packet;

import me.dylancz.chatter.net.packet.PacketType;

public class ExampleSubPacket extends ExampleSuperPacket {

    public ExampleSubPacket() {
        super(PacketType.SUB_PACKET);
    }

}