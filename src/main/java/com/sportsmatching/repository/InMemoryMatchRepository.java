package com.sportsmatching.repository;

import com.sportsmatching.model.Match;
import com.sportsmatching.model.SportType;
import com.sportsmatching.model.User;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryMatchRepository implements MatchRepository {
    private final Map<String, Match> matchesById = new HashMap<>();

    @Override
    public Match save(Match match) {
        matchesById.put(match.getId(), match);
        return match;
    }

    @Override 
    public Optional<Match> findById(String id) {
        // Buscar por ID completo primero
        Match match = matchesById.get(id);
        if (match != null) {
            return Optional.of(match);
        }
        // Si no se encuentra, buscar por código sin prefijo (permite ingresar solo "A3B9X2" en lugar de "MAT-A3B9X2")
        if (!id.startsWith("MAT-") && id.length() == 6) {
            String fullId = "MAT-" + id.toUpperCase();
            return Optional.ofNullable(matchesById.get(fullId));
        }
        return Optional.empty();
    }

    @Override public Collection<Match> findAll() { return Collections.unmodifiableCollection(matchesById.values()); }

    @Override
    public Collection<Match> findBySport(SportType sportType) {
        return matchesById.values().stream().filter(m -> m.getSportType().equals(sportType)).collect(Collectors.toList());
    }

    @Override
    public Collection<Match> findByUser(User user) {
        return matchesById.values().stream()
                .filter(m -> m.getPlayers().contains(user))
                .collect(Collectors.toList());
    }
}

