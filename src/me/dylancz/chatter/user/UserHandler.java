package me.dylancz.chatter.user;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class UserHandler {

    private final Set<User> users;

    public UserHandler() {
        this.users = new HashSet<>();
    }

    public UserHandler(final Set<User> users) {
        this.users = users;
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

}
