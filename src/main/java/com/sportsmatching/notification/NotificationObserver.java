package com.sportsmatching.notification;

import com.sportsmatching.model.Match;

public interface NotificationObserver {
    void onMatchEvent(Match match, MatchEvent event);
}

