package com.sportsmatching.model.state.states;

import com.sportsmatching.data.ReferenceData;
import com.sportsmatching.model.Match;
import com.sportsmatching.model.state.MatchState;
import com.sportsmatching.notification.MatchEvent;

public class AssembledState implements MatchState {
    @Override public String name() { return "Partido armado"; }

    @Override public void onPlayerJoined(Match match) { /* already assembled */ }

    @Override
    public void confirm(Match match) {
        match.setState(new ConfirmedState());
        match.notifyObservers(ReferenceData.get().event("CONFIRMED"));
    }

    @Override public void start(Match match) { /* wait for confirm or time */ }
    @Override public void finish(Match match) { /* cannot */ }

    @Override
    public void cancel(Match match) {
        match.setState(new CanceledState());
        match.notifyObservers(ReferenceData.get().event("CANCELED"));
    }
}

