package com.sportsmatching.service;

import com.sportsmatching.data.ReferenceData;
import com.sportsmatching.model.Location;
import com.sportsmatching.model.Match;
import com.sportsmatching.model.SportType;
import com.sportsmatching.model.User;
import com.sportsmatching.notification.MatchEvent;
import com.sportsmatching.notification.NotificationFacade;
import com.sportsmatching.repository.MatchRepository;
import com.sportsmatching.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class MatchService {
    private final MatchRepository matchRepository;
    private final UserRepository userRepository;
    private final MatchingService matchingService;
    private final NotificationFacade notificationFacade = new NotificationFacade();
    private final LocationService locationService = new LocationService();

    public MatchService(MatchRepository matchRepository, UserRepository userRepository, MatchingService matchingService) {
        this.matchRepository = matchRepository;
        this.userRepository = userRepository;
        this.matchingService = matchingService;
    }

    public Match createMatch(SportType sportType, int requiredPlayers, int durationMinutes, Location location, String locationDescription, LocalDateTime startDateTime) {
        Match match = new Match(sportType, requiredPlayers, durationMinutes, location, locationDescription, startDateTime);
        matchRepository.save(match);
        // Notify users who have this favorite sport
        userRepository.findAll().forEach(u -> {
            if (sportType.equals(u.getFavoriteSport())) {
                notificationFacade.notifyAllChannels(u, match, ReferenceData.get().event("NEW_FOR_FAVORITE_SPORT"));
            }
        });
        return match;
    }

    public Optional<Match> findMatch(String id) { return matchRepository.findById(id); }

    public Collection<Match> findBySport(SportType sportType) { return matchRepository.findBySport(sportType); }
    
    public Collection<Match> findNearby(SportType sportType, Location userLocation) {
        Collection<Match> matches = sportType != null 
            ? matchRepository.findBySport(sportType) 
            : matchRepository.findAll();
        
        // Ordenar por distancia y devolver
        return matches.stream()
            .sorted(Comparator.comparingDouble(m -> locationService.calculateDistance(userLocation, m.getLocation())))
            .collect(Collectors.toList());
    }
    
    public double getDistance(Location userLocation, Match match) {
        return locationService.calculateDistance(userLocation, match.getLocation());
    }

    public void joinMatch(String matchId, String username) {
        Match match = matchRepository.findById(matchId).orElseThrow();
        User user = userRepository.findByUsername(username).orElseThrow();
        match.join(user);
        if (match.hasEnoughPlayers()) {
            // Notification is emitted from state transition to ASSEMBLED
        }
    }

    public List<User> suggestPlayers(String matchId) {
        Match match = matchRepository.findById(matchId).orElseThrow();
        return matchingService.suggestPlayers(match, userRepository.findAll());
    }

    public void confirm(String matchId) { matchRepository.findById(matchId).orElseThrow().confirm(); }
    public void startIfTime(String matchId) { matchRepository.findById(matchId).orElseThrow().start(); }
    public void finish(String matchId) { matchRepository.findById(matchId).orElseThrow().finish(); }
    public void cancel(String matchId) { matchRepository.findById(matchId).orElseThrow().cancel(); }
    
    public Collection<Match> findMatchesByUser(String username) {
        User user = userRepository.findByUsername(username).orElseThrow();
        return matchRepository.findByUser(user);
    }
}

