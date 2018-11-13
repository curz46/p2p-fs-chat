package me.dylancz.chatter.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class EventBus {

    private final Map<Class<? extends Event>, List<Consumer<Object>>> listenerMap = new HashMap<>();

    /**
     * Post an Event to the EventBus. Listeners will receive this Event if they are subscribed to
     * it, or they are subscribed to a superclass of the Event.
     * @param event The Event to post.
     */
    @SuppressWarnings("SuspiciousMethodCalls")
    public <T extends Event> void post(final T event) {
        Class<?> tempClass = event.getClass();
        while (tempClass != Object.class) {
            if (!this.listenerMap.containsKey(tempClass)) {
                tempClass = tempClass.getSuperclass();
                continue;
            }
            final List<Consumer<Object>> list = this.listenerMap.get(tempClass);
            for (final Consumer<Object> consumer : list) {
                consumer.accept(event);
            }
            tempClass = tempClass.getSuperclass();
        }
    }

    /**
     * Register an Event Listener which should trigger when an Event of the given type is fired.
     * @param listener The Event Listener to register.
     * @param clazz The class of the Event it should trigger for.
     * @param <T> The type of the Event it should trigger for.
     */
    public <T extends Event> void registerListener(final Consumer<T> listener, final Class<T> clazz) {
        //noinspection unchecked
        this.listenerMap
            .computeIfAbsent(clazz, k -> new ArrayList<>())
            .add((Consumer) listener);
    }

    /**
     * Scan the given Object for methods which are annotated with {@link Subscribe} and register
     * them as Event Listeners for the Event type that they take.
     * @param object The Object to scan for Event Listeners.
     */
    public void registerListeners(final Object object) {
        final Class<?> clazz = object.getClass();
        for (final Method method : clazz.getMethods()) {
            // Check if it has the annotation
            if (!method.isAnnotationPresent(Subscribe.class)) continue;
            // TODO: Here, we would get any additional listener metadata like priority.
            // final Subscribe annotation = method.getAnnotation(Subscribe.class);
            final Class<?> eventClass = method.getParameterTypes()[0];
            if (!Event.class.isAssignableFrom(eventClass)) {
                throw new RuntimeException("The parameter is not a subclass of Event.");
            }
            //noinspection unchecked
            final Class<? extends Event> castedClass = (Class<? extends Event>) eventClass;
            this.registerListener(event -> {
                try {
                    method.invoke(object, event);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(
                        "An exception occurred while invoking a listener: ", e
                    );
                }
            }, castedClass);
        }
    }

}
