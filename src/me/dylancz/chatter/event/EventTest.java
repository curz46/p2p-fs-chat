package me.dylancz.chatter.event;

import me.dylancz.chatter.event.packet.ExampleSubPacket;
import me.dylancz.chatter.event.packet.ExampleSuperPacket;
import me.dylancz.chatter.event.packet.PacketBus;
import me.dylancz.chatter.event.packet.PacketEvent;
import me.dylancz.chatter.event.packet.PacketReceivedEvent;
import me.dylancz.chatter.net.packet.ConnectedPacket;
import me.dylancz.chatter.net.packet.Packet;
import me.dylancz.chatter.net.packet.PingPacket;
import me.dylancz.chatter.net.packet.PongPacket;

public class EventTest {

    public static class AnotherEvent extends SomeEvent {

        public AnotherEvent(String name) {
            super(name);
        }
    }

    public static class SomeEvent extends Event {

        private final String name;

        public SomeEvent(final String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

    }

    public static void main(final String[] args) {
        final PacketBus bus = new PacketBus();
        final EventTest test = new EventTest();
        bus.registerListeners(test);
        bus.post(new PacketReceivedEvent<>(new ExampleSubPacket(), null));
    }

    @Subscribe
    public void onSomeEvent(final PacketEvent<ExampleSubPacket> event) {
        System.out.println("Sub Packet: " + event.getPacket());
    }

    public void someRandomMethod() {}

}
