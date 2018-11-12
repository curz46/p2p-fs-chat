package me.dylancz.chatter.user;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class UserHandler {

    private final Set<User> users = new HashSet<>();

    public Optional<User> findUserForPath(final Path path) {
        return this.users.stream()
            .filter(user -> user.getPath().equals(path))
            .findAny();
    }

    public Set<User> getUsers() {
        return this.users;
    }

    public User createUser(final Path path) {
        final Optional<User> existing = this.findUserForPath(path);
        if (existing.isPresent()) throw new RuntimeException("A user for this Path already exists: " + path);
        final User user = new User(path);
        this.users.add(user);
        return user;
    }

    public void deleteUser(final Path path) {
        this.findUserForPath(path).ifPresent(this.users::remove);
    }

}
