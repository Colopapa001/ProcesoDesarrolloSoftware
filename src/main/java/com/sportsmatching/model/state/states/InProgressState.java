package com.sportsmatching.model.state.states;

import com.sportsmatching.data.ReferenceData;
import com.sportsmatching.model.Match;
import com.sportsmatching.model.state.MatchState;
import com.sportsmatching.notification.MatchEvent;

public class InProgressState implements MatchState {
    @Override public String name() { return "En juego"; }
    @Override public void onPlayerJoined(Match match) { /* cannot join */ }
    @Override public void confirm(Match match) { /* ignore */ }

    @Override
    public void start(Match match) { /* already started */ }

    @Override
    public void finish(Match match) {
        match.setState(new FinishedState());
        match.notifyObservers(ReferenceData.get().event("FINISHED"));
    }

    @Override
    public void cancel(Match match) {
        match.setState(new CanceledState());
        match.notifyObservers(ReferenceData.get().event("CANCELED"));
    }
}

