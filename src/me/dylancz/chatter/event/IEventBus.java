package me.dylancz.chatter.event;

public interface IEventBus<E, L extends EventListener<E>> {

    <T extends E> void post(final T event);

    void registerListener(final L listener);

    void registerListeners(final Object object);

}
