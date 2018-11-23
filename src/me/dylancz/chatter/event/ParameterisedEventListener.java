package me.dylancz.chatter.event;

import java.util.function.Consumer;

public class ParameterisedEventListener<E> extends EventListener<E> {

    private final Class<?>[] typeParameterClasses;

    protected ParameterisedEventListener(final Consumer<E> consumer,
                                      final Class<? extends E> eventClass,
                                      final Class<?>[] typeParameterClasses) {
        super(consumer, eventClass);
        this.typeParameterClasses = typeParameterClasses;
    }

    public Class<?>[] getTypeParameters() {
        return this.typeParameterClasses;
    }

}