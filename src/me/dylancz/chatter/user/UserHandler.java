package me.dylancz.chatter.user;

import me.dylancz.chatter.event.DefaultEventBus;
import me.dylancz.chatter.event.Subscribe;
import me.dylancz.chatter.event.packet.PacketReceivedEvent;
import me.dylancz.chatter.file.PacketWriter;
import me.dylancz.chatter.packet.PingPacket;
import me.dylancz.chatter.packet.PongPacket;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class UserHandler {

    private final DefaultEventBus eventBus;
    private final ScheduledExecutorService executorService;
    private final PacketWriter packetWriter;
    private final UUID selfUUID;
    private final Set<User> users;

    private long lastPing;

    public UserHandler(final DefaultEventBus eventBus, final ScheduledExecutorService executorService,
                       final PacketWriter packetWriter, final UUID selfUUID) {
        this.eventBus = eventBus;
        this.executorService = executorService;
        this.packetWriter = packetWriter;
        this.selfUUID = selfUUID;
        this.users = new HashSet<>();
    }

    public void register() {
        this.eventBus.registerListeners(new PingListener());
        this.executorService.scheduleAtFixedRate(
            () -> {
                this.packetWriter.write(new PingPacket());
                this.lastPing = System.currentTimeMillis();
            },
            0, 1, TimeUnit.SECONDS
        );
    }

    public void addUser(final User user) {
        this.users.add(user);
    }

    public void removeUser(final User user) {
        this.users.remove(user);
    }

    public void addUsers(final Collection<User> toAdd) {
        this.users.addAll(toAdd);
    }

    public Set<User> getUsers() {
        return this.users;
    }

    public class PingListener {

        @Subscribe
        public void onPingPacket(final PacketReceivedEvent<PingPacket> event) {
            final UUID uuid = event.getSource().getUUID();
            UserHandler.this.packetWriter.write(new PongPacket(uuid));
        }

        @Subscribe
        public void onPongPacket(final PacketReceivedEvent<PongPacket> event) {
            if (!event.getPacket().getTarget().equals(UserHandler.this.selfUUID)) return;
            final long delta = System.currentTimeMillis() - UserHandler.this.lastPing;
            System.out.println("Received PongPacket: delta " + delta);
        }

    }

}
