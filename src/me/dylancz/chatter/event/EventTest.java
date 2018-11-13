package me.dylancz.chatter.event;

import me.dylancz.chatter.event.packet.PacketEventBus;

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
        final EventBus bus = new PacketEventBus();
        final EventTest test = new EventTest();
//        bus.registerListener(test::onSomeEvent, SomeEvent.class);
        bus.registerListeners(test);
        bus.post(new SomeEvent("dog"));
        bus.post(new AnotherEvent("cat"));
        bus.post(new SomeEvent("hello"));
    }

    @Subscribe
    public void onSomeEvent(final SomeEvent evt) {
        System.out.println("Hello: " + evt.getName());
    }

    @Subscribe
    public void onAnotherEvent(final AnotherEvent evt) {
        System.out.println("Another event: " + evt.getName());
    }

    public void someRandomMethod() {}

}
