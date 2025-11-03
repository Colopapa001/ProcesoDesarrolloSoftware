package com.sportsmatching.model.state.states;

import com.sportsmatching.model.Match;
import com.sportsmatching.model.state.MatchState;

public class FinishedState implements MatchState {
    @Override public String name() { return "Finalizado"; }
    @Override public void onPlayerJoined(Match match) { /* closed */ }
    @Override public void confirm(Match match) { /* closed */ }
    @Override public void start(Match match) { /* closed */ }
    @Override public void finish(Match match) { /* closed */ }
    @Override public void cancel(Match match) { /* closed */ }
}

