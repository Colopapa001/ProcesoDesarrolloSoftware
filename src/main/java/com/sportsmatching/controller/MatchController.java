package com.sportsmatching.controller;

import com.sportsmatching.model.Match;
import com.sportsmatching.model.SportType;
import com.sportsmatching.model.User;
import com.sportsmatching.service.MatchService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public class MatchController {
    private final MatchService matchService;

    public MatchController(MatchService matchService) { this.matchService = matchService; }

    public Match create(SportType sportType, int requiredPlayers, int durationMinutes, com.sportsmatching.model.Location location, String locationDescription, LocalDateTime start) {
        return matchService.createMatch(sportType, requiredPlayers, durationMinutes, location, locationDescription, start);
    }

    public Collection<Match> search(SportType sportType) { return matchService.findBySport(sportType); }
    
    public Collection<Match> searchNearby(SportType sportType, com.sportsmatching.model.Location userLocation) {
        return matchService.findNearby(sportType, userLocation);
    }
    public void join(String matchId, String username) { matchService.joinMatch(matchId, username); }
    public void confirm(String matchId) { matchService.confirm(matchId); }
    public void startIfTime(String matchId) { matchService.startIfTime(matchId); }
    public void finish(String matchId) { matchService.finish(matchId); }
    public void cancel(String matchId) { matchService.cancel(matchId); }
    public List<User> suggestions(String matchId) { return matchService.suggestPlayers(matchId); }
    public Collection<Match> findMyMatches(String username) { return matchService.findMatchesByUser(username); }
}

