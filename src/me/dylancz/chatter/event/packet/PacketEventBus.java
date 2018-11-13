package me.dylancz.chatter.event.packet;

import me.dylancz.chatter.event.EventBus;

public class PacketEventBus extends EventBus {

    public <T extends PacketEvent> void post(final T packetEvent) {

    }

}
