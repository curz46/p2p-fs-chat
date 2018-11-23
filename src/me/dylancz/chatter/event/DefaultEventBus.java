package me.dylancz.chatter.event;

public class DefaultEventBus extends SynchronousBus<Event, EventListener<Event>> {

    public DefaultEventBus() {
        super(new ParameterisedEventBus());
    }

}
