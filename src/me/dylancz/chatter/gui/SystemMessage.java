package me.dylancz.chatter.gui;

import java.time.Instant;
import java.util.Date;

public class SystemMessage extends Message {

    private final String content;

    public SystemMessage(final Date timestamp, final String content) {
        super(timestamp);
        this.content = content;
    }

    public SystemMessage(final String content) {
        this(Date.from(Instant.now()), content);
    }

    @Override
    public String getHeader() {
        return "[System]";
    }

    @Override
    public String getBody() {
        return this.content;
    }

}
