package com.sportsmatching.repository;

import com.sportsmatching.model.Match;
import com.sportsmatching.model.SportType;

import java.util.Collection;
import java.util.Optional;

public interface MatchRepository {
    Match save(Match match);
    Optional<Match> findById(String id);
    Collection<Match> findAll();
    Collection<Match> findBySport(SportType sportType);
}

