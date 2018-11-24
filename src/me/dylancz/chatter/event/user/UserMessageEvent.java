package me.dylancz.chatter.event.user;

import me.dylancz.chatter.user.User;

public class UserMessageEvent extends UserEvent {

    private final String content;

    public UserMessageEvent(final User user, final String content) {
        super(user);
        this.content = content;
    }

    public String getContent() {
        return this.content;
    }

}
