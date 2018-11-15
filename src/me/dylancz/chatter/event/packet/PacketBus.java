package me.dylancz.chatter.event.packet;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import me.dylancz.chatter.event.EventBus;
import me.dylancz.chatter.event.Subscribe;
import me.dylancz.chatter.net.packet.Packet;
import me.dylancz.chatter.net.packet.PacketType;

public class PacketBus {

    private final Map<PacketType, EventBus<PacketEvent>> busMap = new HashMap<>();

    public <T extends PacketEvent> void post(final T event) {
        final PacketType type = event.getPacket().getType();
        if (this.busMap.containsKey(type)) {
            this.busMap.get(type).post(event);
        }
    }

    public <P extends Packet, T extends PacketEvent<P>> void registerListener(final Consumer<T> listener,
                                                                              final Class<T> clazz,
                                                                              final PacketType type) {
        this.busMap
            .computeIfAbsent(type, k -> new EventBus<>())
            .registerListener(listener, clazz);
    }

    @SuppressWarnings("unchecked")
    public void registerListeners(final Object object) {
        final Class<?> clazz = object.getClass();
        for (final Method method : clazz.getMethods()) {
            // Check if it has the annotation
            if (!method.isAnnotationPresent(Subscribe.class)) continue;
            // TODO: Here, we would get any additional listener metadata like priority.
            // final Subscribe annotation = method.getAnnotation(Subscribe.class);
            final Class<?> eventClass = method.getParameterTypes()[0];
            final Class<? extends PacketEvent> castedClass;
            // TODO: When a @Subscribe method has some nonsense parameter like a String, this doesn't break.
            // TODO: That's very suspicious; something to look into.
            try {
                castedClass = (Class<? extends PacketEvent>) eventClass;
            } catch (final ClassCastException e) {
                throw new RuntimeException("The parameter is not a subclass of this Event type: ", e);
            }
            final Type type = method.getGenericParameterTypes()[0];
            if (!(type instanceof ParameterizedType)) {
                throw new RuntimeException(
                    "PacketBus does not support Non-parameterized Event types. Use the default Bus."
                );
            }
            final Class<? extends Packet> genericClass = (Class<? extends Packet>)
                ((ParameterizedType) type).getActualTypeArguments()[0];
            this.registerListener(event -> {
                try {
                    method.invoke(object, event);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(
                        "An exception occurred while invoking a listener: ", e
                    );
                }
            }, castedClass, PacketType.fromClass(genericClass));
        }
    }

}
