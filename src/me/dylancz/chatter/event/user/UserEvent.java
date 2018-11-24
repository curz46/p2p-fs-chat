package me.dylancz.chatter.event.user;

import me.dylancz.chatter.event.Event;
import me.dylancz.chatter.user.User;

public abstract class UserEvent extends Event {

    private final User user;

    public UserEvent(final User user) {
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }

}
