package com.sportsmatching.notification;

import com.sportsmatching.model.Match;
import com.sportsmatching.model.User;

public interface Notifier {
    void notify(User user, Match match, MatchEvent event);
}

