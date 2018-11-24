package me.dylancz.chatter.event.user;

import me.dylancz.chatter.user.User;

public class UserDiscardEvent extends UserEvent {

    public UserDiscardEvent(final User user) {
        super(user);
    }

}
