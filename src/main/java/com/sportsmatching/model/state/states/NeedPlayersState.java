package com.sportsmatching.model.state.states;

import com.sportsmatching.data.ReferenceData;
import com.sportsmatching.model.Match;
import com.sportsmatching.model.state.MatchState;
import com.sportsmatching.notification.MatchEvent;

public class NeedPlayersState implements MatchState {
    @Override public String name() { return "Necesitamos jugadores"; }

    @Override
    public void onPlayerJoined(Match match) {
        if (match.hasEnoughPlayers()) {
            match.setState(new AssembledState());
            match.notifyObservers(ReferenceData.get().event("ASSEMBLED"));
        }
    }

    @Override public void confirm(Match match) { /* cannot confirm yet */ }
    @Override public void start(Match match) { /* cannot start yet */ }
    @Override public void finish(Match match) { /* cannot finish */ }

    @Override
    public void cancel(Match match) {
        match.setState(new CanceledState());
        match.notifyObservers(ReferenceData.get().event("CANCELED"));
    }
}

