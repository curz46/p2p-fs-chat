package me.dylancz.chatter.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class EventBus<E> implements IEventBus<E, EventListener<E>> {

    protected final List<EventListener<E>> listeners = new ArrayList<>();

    @SuppressWarnings("SuspiciousMethodCalls")
    public <T extends E> void post(final T event) {
        Class<?> tempClass = event.getClass();
        while (tempClass != Object.class) {
            final Class<?> clazz = tempClass;
            //noinspection unchecked
            this.listeners.stream()
                .filter(listener -> listener.getEventClass() == clazz)
                .forEach(listener -> ((Consumer<E>) listener.getConsumer()).accept(event));
            tempClass = tempClass.getSuperclass();
        }
    }

    @Override
    public void registerListener(final EventListener<E> listener) {
        this.listeners.add(listener);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void registerListeners(final Object object) {
        final Class<?> clazz = object.getClass();
        for (final Method method : clazz.getMethods()) {
            // Check if it has the annotation
            if (!method.isAnnotationPresent(Subscribe.class)) continue;
            // TODO: Here, we would get any additional listener metadata like priority.
            // final Subscribe annotation = method.getAnnotation(Subscribe.class);
            final Class<?> eventClass = method.getParameterTypes()[0];
//            if (!Event.class.isAssignableFrom(eventClass)) {
//                throw new RuntimeException("The parameter is not a subclass of Event.");
//            }
            final Class<? extends E> castedClass;
            // TODO: When a @Subscribe method has some nonsense parameter like a String, this doesn't break.
            // TODO: That's very suspicious; something to look into.
            try {
                castedClass = (Class<? extends E>) eventClass;
            } catch (final ClassCastException e) {
                throw new RuntimeException("The parameter is not a subclass of this Event type: ", e);
            }
            final Consumer<E> consumer = event -> {
                try {
                    method.invoke(object, event);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(
                        "An exception occurred while invoking a listener: ", e
                    );
                }
            };
            this.registerListener(new EventListener(consumer, castedClass));
        }
    }

}
