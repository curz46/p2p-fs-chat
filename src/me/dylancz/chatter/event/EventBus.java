package me.dylancz.chatter.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class EventBus extends LockableBus {

    private final Map<Class<? extends Event>, List<Consumer<Object>>> listenerMap = new HashMap<>();

    @SuppressWarnings("SuspiciousMethodCalls")
    public <T extends Event> void post(final T event) {
        this.verifyThread();
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

    public <T extends Event> void registerListener(final Consumer<T> listener, final Class<T> clazz) {
        //noinspection unchecked
        this.listenerMap
            .computeIfAbsent(clazz, k -> new ArrayList<>())
            .add((Consumer) listener);
    }

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
            final Class<? extends Event> castedClass;
            // TODO: When a @Subscribe method has some nonsense parameter like a String, this doesn't break.
            // TODO: That's very suspicious; something to look into.
            try {
                //noinspection unchecked
                castedClass = (Class<? extends Event>) eventClass;
            } catch (final ClassCastException e) {
                throw new RuntimeException("The parameter is not a subclass of this Event type: ", e);
            }
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
