package com.sportsmatching.repository;

import com.sportsmatching.model.User;

import java.util.*;

public class InMemoryUserRepository implements UserRepository {
    private final Map<String, User> usersById = new HashMap<>();
    private final Map<String, User> usersByUsername = new HashMap<>();

    @Override
    public User save(User user) {
        usersById.put(user.getId(), user);
        usersByUsername.put(user.getUsername(), user);
        return user;
    }

    @Override public Optional<User> findById(String id) { return Optional.ofNullable(usersById.get(id)); }

    @Override public Optional<User> findByUsername(String username) { return Optional.ofNullable(usersByUsername.get(username)); }

    @Override public Collection<User> findAll() { return Collections.unmodifiableCollection(usersById.values()); }
}

