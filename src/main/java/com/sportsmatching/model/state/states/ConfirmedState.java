package com.sportsmatching.model.state.states;

import com.sportsmatching.data.ReferenceData;
import com.sportsmatching.model.Match;
import com.sportsmatching.model.state.MatchState;
import com.sportsmatching.notification.MatchEvent;

import java.time.LocalDateTime;

public class ConfirmedState implements MatchState {
    @Override public String name() { return "Confirmado"; }

    @Override public void onPlayerJoined(Match match) { /* ignore */ }

    @Override public void confirm(Match match) { /* already confirmed */ }

    @Override
    public void start(Match match) {
        if (LocalDateTime.now().isAfter(match.getStartDateTime()) || LocalDateTime.now().isEqual(match.getStartDateTime())) {
            match.setState(new InProgressState());
            match.notifyObservers(ReferenceData.get().event("IN_PROGRESS"));
        }
    }

    @Override public void finish(Match match) { /* cannot */ }

    @Override
    public void cancel(Match match) {
        match.setState(new CanceledState());
        match.notifyObservers(ReferenceData.get().event("CANCELED"));
    }
}

