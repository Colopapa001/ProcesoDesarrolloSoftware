package com.sportsmatching.repository;

import com.sportsmatching.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(String id);
    Optional<User> findByUsername(String username);
    Collection<User> findAll();
}

