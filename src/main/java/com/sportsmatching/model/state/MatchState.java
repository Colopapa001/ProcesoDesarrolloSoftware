package com.sportsmatching.model.state;

import com.sportsmatching.model.Match;

public interface MatchState {
    String name();
    void onPlayerJoined(Match match);
    void confirm(Match match);
    void start(Match match);
    void finish(Match match);
    void cancel(Match match);
}

