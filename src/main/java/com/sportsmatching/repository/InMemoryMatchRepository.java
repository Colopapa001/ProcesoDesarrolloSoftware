package com.sportsmatching.repository;

import com.sportsmatching.model.Match;
import com.sportsmatching.model.SportType;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryMatchRepository implements MatchRepository {
    private final Map<String, Match> matchesById = new HashMap<>();

    @Override
    public Match save(Match match) {
        matchesById.put(match.getId(), match);
        return match;
    }

    @Override public Optional<Match> findById(String id) { return Optional.ofNullable(matchesById.get(id)); }

    @Override public Collection<Match> findAll() { return Collections.unmodifiableCollection(matchesById.values()); }

    @Override
    public Collection<Match> findBySport(SportType sportType) {
        return matchesById.values().stream().filter(m -> m.getSportType().equals(sportType)).collect(Collectors.toList());
    }
}

