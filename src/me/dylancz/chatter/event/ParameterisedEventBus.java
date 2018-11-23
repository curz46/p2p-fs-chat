package me.dylancz.chatter.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.function.Consumer;
import me.dylancz.chatter.net.packet.Packet;

public class ParameterisedEventBus extends EventBus<Event> {

    @Override
    public <T extends Event> void post(final T event) {
        if (!(event instanceof ParameterisedEvent)) {
            super.post(event);
            return;
        }
        final ParameterisedEvent castedEvent = (ParameterisedEvent) event;
        Class<?> tempClass = castedEvent.getClass();
        while (tempClass != Object.class) {
            final Class<?> clazz = tempClass;
            //noinspection unchecked
            super.listeners.stream()
                .filter(listener -> listener instanceof ParameterisedEventListener)
                .map(listener -> (ParameterisedEventListener) listener)
                .filter(listener -> listener.getEventClass() == clazz)
                .filter(listener ->
                    Arrays.equals(listener.getTypeParameters(), castedEvent.getTypeParameters()))
                .forEach(listener -> ((Consumer<Event>) listener.getConsumer()).accept(castedEvent));
            tempClass = tempClass.getSuperclass();
        }
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
            final Class<? extends Event> castedClass;
            // TODO: When a @Subscribe method has some nonsense parameter like a String, this doesn't break.
            // TODO: That's very suspicious; something to look into.
            try {
                castedClass = (Class<? extends Event>) eventClass;
            } catch (final ClassCastException e) {
                throw new RuntimeException("The parameter is not a subclass of this Event type: ", e);
            }
            final Consumer<Event> consumer = event -> {
                try {
                    method.invoke(object, event);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(
                        "An exception occurred while invoking a listener: ", e
                    );
                }
            };
            final Type type = method.getGenericParameterTypes()[0];
            if (!(type instanceof ParameterizedType)) {
                this.registerListener(new EventListener<>(consumer, castedClass));
                continue;
            }
            final Class<?>[] typeParameters =
                Arrays.stream(((ParameterizedType) type).getActualTypeArguments())
                    .map(argument -> (Class<?>) argument)
                    .toArray(Class<?>[]::new);
            this.registerListener(new ParameterisedEventListener<>(
                consumer,
                castedClass,
                typeParameters
            ));
        }
    }

}
