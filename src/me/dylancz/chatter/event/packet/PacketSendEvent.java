package me.dylancz.chatter.event.packet;

import me.dylancz.chatter.net.packet.Packet;

public abstract class PacketSendEvent<T extends Packet> extends PacketEvent<T> {

    private PacketSendEvent(T packet) {
        super(packet);
    }

    public class Pre<T extends Packet> extends PacketSendEvent<T> {

        public Pre(T packet) {
            super(packet);
        }

    }

    public class Post<T extends Packet> extends PacketSendEvent<T> {

        public Post(T packet) {
            super(packet);
        }

    }

}
