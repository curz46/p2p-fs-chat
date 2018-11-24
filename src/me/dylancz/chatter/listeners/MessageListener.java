package me.dylancz.chatter.listeners;

import me.dylancz.chatter.event.Subscribe;
import me.dylancz.chatter.event.packet.PacketReceivedEvent;
import me.dylancz.chatter.gui.UserMessage;
import me.dylancz.chatter.gui.Window;
import me.dylancz.chatter.packet.MessagePacket;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

public class MessageListener {

    private final Window window;

    public MessageListener(final Window window) {
        this.window = window;
    }

    @Subscribe
    public void onMessagePacket(final PacketReceivedEvent<MessagePacket> event) {
        final MessagePacket packet = event.getPacket();
        final Date date = Date.from(Instant.now());
        final UUID uuid = event.getSource().getUUID();
        final String name = uuid.toString().substring(0, 8) + "...";
        this.window.update(new UserMessage(name, date, packet.getContent()));
    }

}
