package me.dylancz.chatter.gui;

import java.util.Date;

public class UserMessage extends Message {

    private final String source;
    private final String content;

    public UserMessage(final String source, final Date timestamp, final String content) {
        super(timestamp);
        this.source = source;
        this.content = content;
    }

    @Override
    public String getHeader() {
        // TODO: For now, until usernames are added.
        return this.source;
    }

    @Override
    public String getBody() {
        return this.content;
    }

}
