package com.sportsmatching.controller;

import com.sportsmatching.model.SkillLevel;
import com.sportsmatching.model.SportType;
import com.sportsmatching.model.User;
import com.sportsmatching.repository.UserRepository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) { this.userRepository = userRepository; }

    public User registerUser(String username, String email, String password, SportType favoriteSport, SkillLevel skillLevel) {
        String hash = sha256(password);
        User user = new User(username, email, hash, favoriteSport, skillLevel);
        return userRepository.save(user);
    }

    private String sha256(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encoded = digest.digest(value.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : encoded) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }
}

