package com.sportsmatching.model;

import com.sportsmatching.model.state.MatchState;
import com.sportsmatching.model.state.states.NeedPlayersState;
import com.sportsmatching.notification.MatchEvent;
import com.sportsmatching.notification.NotificationObserver;

import com.sportsmatching.util.MatchIdGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Match {
    private final String id;
    private final SportType sportType;
    private final int requiredPlayers;
    private final int durationMinutes;
    private final Location location;
    private final String locationDescription;
    private final LocalDateTime startDateTime;

    private final List<User> players = new ArrayList<>();
    private final List<NotificationObserver> observers = new ArrayList<>();

    private MatchState state;

    public Match(SportType sportType, int requiredPlayers, int durationMinutes, Location location, String locationDescription, LocalDateTime startDateTime) {
        this.id = MatchIdGenerator.generate();
        this.sportType = Objects.requireNonNull(sportType);
        this.requiredPlayers = requiredPlayers;
        this.durationMinutes = durationMinutes;
        this.location = Objects.requireNonNull(location);
        this.locationDescription = locationDescription != null ? locationDescription : location.toString();
        this.startDateTime = Objects.requireNonNull(startDateTime);
        this.state = new NeedPlayersState();
    }

    public String getId() { return id; }
    public String getShortId() { return id; } // Ahora el ID ya es corto, así que devolvemos el mismo
    public SportType getSportType() { return sportType; }
    public int getRequiredPlayers() { return requiredPlayers; }
    public int getDurationMinutes() { return durationMinutes; }
    public Location getLocation() { return location; }
    public String getLocationDescription() { return locationDescription; }
    public LocalDateTime getStartDateTime() { return startDateTime; }
    public MatchState getState() { return state; }
    public List<User> getPlayers() { return Collections.unmodifiableList(players); }

    public void addObserver(NotificationObserver observer) { observers.add(observer); }
    public void removeObserver(NotificationObserver observer) { observers.remove(observer); }
    public void notifyObservers(MatchEvent event) { observers.forEach(o -> o.onMatchEvent(this, event)); }

    public void setState(MatchState state) { this.state = state; }

    public void join(User user) {
        if (!players.contains(user)) {
            players.add(user);
            state.onPlayerJoined(this);
        }
    }

    public boolean hasEnoughPlayers() { return players.size() >= requiredPlayers; }

    public void confirm() { state.confirm(this); }
    public void start() { state.start(this); }
    public void finish() { state.finish(this); }
    public void cancel() { state.cancel(this); }
}

