package me.dylancz.chatter.event;

/**
 * An abstract class that requires Events that are to be compatible with ParameterisedEventBus
 * to provide a method of obtaining the classes of the Type parameters attached to the Event
 * instance. This is necessary such that the ParameterisedEventBus can differentiate between Events
 * which should be consumed by their subscribed Listeners according to the Type parameter they
 * require.
 */
public abstract class ParameterisedEvent extends Event {

    /**
     * @return The classes of the Type parameters of this Parameterised Event. For example, if there
     * was SomeClass<T> that extends ParameterisedEvent, this method would return an array
     * containing the class of T.
     */
    public abstract Class<?>[] getTypeParameters();

}
