package com.sportsmatching.notification;

import com.sportsmatching.data.ReferenceData;
import com.sportsmatching.model.Match;
import com.sportsmatching.model.User;

import java.util.List;

public class NotificationFacade {
    private final NotificationFactory factory = new NotificationFactory();

    public void notifyAllChannels(User user, Match match, MatchEvent event) {
        List<NotificationChannel> channels = ReferenceData.get().defaultChannels();
        for (NotificationChannel channel : channels) {
            Notifier notifier = factory.create(channel);
            notifier.notify(user, match, event);
        }
    }
}

