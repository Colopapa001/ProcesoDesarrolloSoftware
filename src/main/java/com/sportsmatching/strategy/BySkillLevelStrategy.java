package com.sportsmatching.strategy;

import com.sportsmatching.model.Match;
import com.sportsmatching.model.SkillLevel;
import com.sportsmatching.model.User;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class BySkillLevelStrategy implements PlayerMatchingStrategy {
    @Override
    public List<User> suggestPlayers(Match match, Collection<User> candidates) {
        // Prefer higher rank first
        Comparator<User> bySkill = Comparator.comparingInt((User u) -> u.getSkillLevel() != null ? u.getSkillLevel().getRank() : 0).reversed();
        return candidates.stream().sorted(bySkill).limit(10).collect(Collectors.toList());
    }

}

