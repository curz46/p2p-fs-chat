package me.dylancz.chatter.event;

import java.util.function.Consumer;

public class EventListener<E> {

    public static <E, T extends E> EventListener<E> of(final Consumer<T> consumer,
                                                       final Class<T> eventClass) {
        return new EventListener<>(consumer, eventClass);
    }

    public static <E, T extends E> ParameterisedEventListener<E> of(final Consumer<T> consumer,
                                                                    final Class<T> eventClass,
                                                                    final Class<?>... typeParameterClasses) {
        //noinspection unchecked
        return new ParameterisedEventListener<>(
            (Consumer<E>) consumer,
            eventClass,
            typeParameterClasses
        );
    }

    private final Consumer<? extends E> consumer;
    private final Class<? extends E> eventClass;

    protected EventListener(final Consumer<? extends E> consumer, final Class<? extends E> eventClass) {
        this.consumer = consumer;
        this.eventClass = eventClass;
    }

    public Consumer<? extends E> getConsumer() {
        return this.consumer;
    }

    public Class<? extends E> getEventClass() {
        return this.eventClass;
    }

}
