package com.sportsmatching.model;

import java.util.Objects;
import java.util.UUID;

public class User {
    private final String id;
    private final String username;
    private final String email;
    private final String passwordHash;
    private final SportType favoriteSport;
    private final SkillLevel skillLevel;

    public User(String username, String email, String passwordHash, SportType favoriteSport, SkillLevel skillLevel) {
        this.id = UUID.randomUUID().toString();
        this.username = Objects.requireNonNull(username);
        this.email = Objects.requireNonNull(email);
        this.passwordHash = Objects.requireNonNull(passwordHash);
        this.favoriteSport = favoriteSport;
        this.skillLevel = skillLevel;
    }

    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public SportType getFavoriteSport() { return favoriteSport; }
    public SkillLevel getSkillLevel() { return skillLevel; }
}

