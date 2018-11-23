package me.dylancz.chatter.event;

import com.sun.xml.internal.bind.v2.runtime.reflect.Lister.Pack;
import me.dylancz.chatter.event.packet.PacketReceivedEvent;
import me.dylancz.chatter.net.packet.ConnectedPacket;
import me.dylancz.chatter.net.packet.PingPacket;

public class EventTest {

    public static class TestEvent extends Event {}

    public static void main(final String[] args) {
        final EventBus<Event> eventBus = new ParameterisedEventBus();
        eventBus.registerListener(EventListener.of(
            event -> {
                System.out.println("Event: " + event);
                System.out.println("Packet: " + event.getPacket());
            },
            PacketReceivedEvent.class,
            ConnectedPacket.class
        ));
        eventBus.registerListener(EventListener.of(
            event -> System.out.println("TestEvent: " + event),
            TestEvent.class
        ));
        eventBus.registerListeners(new EventTest());
        eventBus.post(new PacketReceivedEvent<>(new PingPacket(), null));
        eventBus.post(new PacketReceivedEvent<>(new ConnectedPacket(), null));
        eventBus.post(new TestEvent());
    }

    @Subscribe
    public void onTestEvent(final TestEvent event) {
        System.out.println("Subscribed: " + event);
    }

    @Subscribe
    public void onPingPacket(final PacketReceivedEvent<PingPacket> event) {
        System.out.println("Subscribed, event: " + event);
        System.out.println("Subscribed, packet: " + event.getPacket());
    }

}
