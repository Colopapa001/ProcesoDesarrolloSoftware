package com.sportsmatching.strategy;

import com.sportsmatching.model.Match;
import com.sportsmatching.model.User;

import java.util.Collection;
import java.util.List;

public interface PlayerMatchingStrategy {
    List<User> suggestPlayers(Match match, Collection<User> candidates);
}

