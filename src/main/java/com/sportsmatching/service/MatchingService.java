package com.sportsmatching.service;

import com.sportsmatching.model.Match;
import com.sportsmatching.model.User;
import com.sportsmatching.strategy.PlayerMatchingStrategy;

import java.util.Collection;
import java.util.List;

public class MatchingService {
    private PlayerMatchingStrategy strategy;

    public MatchingService(PlayerMatchingStrategy strategy) { this.strategy = strategy; }

    public void setStrategy(PlayerMatchingStrategy strategy) { this.strategy = strategy; }

    public List<User> suggestPlayers(Match match, Collection<User> candidates) {
        return strategy.suggestPlayers(match, candidates);
    }
}

