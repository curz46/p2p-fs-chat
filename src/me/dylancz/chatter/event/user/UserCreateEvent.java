package me.dylancz.chatter.event.user;

import me.dylancz.chatter.user.User;

public class UserCreateEvent extends UserEvent {

    public UserCreateEvent(final User user) {
        super(user);
    }

}
