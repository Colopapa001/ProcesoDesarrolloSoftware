package com.sportsmatching.notification;

public class NotificationFactory {
    public Notifier create(NotificationChannel channel) {
        String name = channel != null ? channel.getName() : "";
        if ("EMAIL".equalsIgnoreCase(name)) return new EmailNotifier();
        if ("PUSH".equalsIgnoreCase(name)) return new PushNotifier();
        throw new IllegalArgumentException("Unsupported channel: " + name);
    }
}

