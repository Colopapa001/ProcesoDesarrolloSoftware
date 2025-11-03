package com.sportsmatching.notification;

import java.util.Objects;

public class NotificationChannel {
    private String name;

    public NotificationChannel() {}
    public NotificationChannel(String name) { this.name = Objects.requireNonNull(name); }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationChannel that = (NotificationChannel) o;
        return Objects.equals(name, that.name);
    }

    @Override public int hashCode() { return Objects.hash(name); }
    @Override public String toString() { return name; }
}

