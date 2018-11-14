package me.dylancz.chatter.event;

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
        final EventBus<Packet> bus = new EventBus<>();
        final EventTest test = new EventTest();
        bus.registerListeners(test);
        bus.post(new ConnectedPacket());
        bus.post(new PingPacket());
        bus.post(new PongPacket());
    }

    @Subscribe
    public void onSomeEvent(final ConnectedPacket packet) {
        System.out.println("Hello: " + packet.getType());
    }

    @Subscribe
    public void onAnotherEvent(final Packet packet) {
        System.out.println("All events: " + packet);
    }

    public void someRandomMethod() {}

}
