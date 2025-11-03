package com.sportsmatching.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sportsmatching.model.SkillLevel;
import com.sportsmatching.model.SportType;
import com.sportsmatching.notification.MatchEvent;
import com.sportsmatching.notification.NotificationChannel;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class ReferenceData {
    private static ReferenceData INSTANCE;

    public static synchronized ReferenceData get() {
        if (INSTANCE == null) {
            INSTANCE = new ReferenceData();
            try { INSTANCE.loadFromClasspath("/data/mockdata.json"); } catch (IOException e) { throw new IllegalStateException(e); }
        }
        return INSTANCE;
    }

    private final Map<String, SportType> sportTypes = new LinkedHashMap<>();
    private final Map<String, SkillLevel> skillLevels = new LinkedHashMap<>();
    private final Map<String, NotificationChannel> channels = new LinkedHashMap<>();
    private final Map<String, MatchEvent> events = new LinkedHashMap<>();
    private final List<String> defaultChannelNames = new ArrayList<>();

    private void loadFromClasspath(String path) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream is = ReferenceData.class.getResourceAsStream(path)) {
            if (is == null) throw new IOException("Resource not found: " + path);
            MockData md = mapper.readValue(is, MockData.class);
            if (md.sportTypes != null) md.sportTypes.forEach(s -> sportTypes.put(s.getName(), s));
            if (md.skillLevels != null) md.skillLevels.forEach(s -> skillLevels.put(s.getName(), s));
            if (md.notificationChannels != null) md.notificationChannels.forEach(c -> channels.put(c.getName(), c));
            if (md.matchEvents != null) md.matchEvents.forEach(e -> events.put(e.getName(), e));
            if (md.defaultNotificationChannels != null) defaultChannelNames.addAll(md.defaultNotificationChannels);
        }
    }

    public SportType sport(String name) { return sportTypes.get(name); }
    public SkillLevel skill(String name) { return skillLevels.get(name); }
    public NotificationChannel channel(String name) { return channels.get(name); }
    public MatchEvent event(String name) { return events.get(name); }

    public Collection<SportType> allSports() { return sportTypes.values(); }
    public Collection<SkillLevel> allSkills() { return skillLevels.values(); }
    public Collection<NotificationChannel> allChannels() { return channels.values(); }
    public Collection<MatchEvent> allEvents() { return events.values(); }

    public List<NotificationChannel> defaultChannels() {
        List<NotificationChannel> out = new ArrayList<>();
        for (String n : defaultChannelNames) {
            NotificationChannel c = channel(n);
            if (c != null) out.add(c);
        }
        return out;
    }

    // Persisting back to file: optional quick method to add new entries (in-memory only by default)
    public void addSport(SportType s) { sportTypes.put(s.getName(), s); }
    public void addSkill(SkillLevel s) { skillLevels.put(s.getName(), s); }
    public void addChannel(NotificationChannel c) { channels.put(c.getName(), c); }
    public void addEvent(MatchEvent e) { events.put(e.getName(), e); }

    // DTO for JSON mapping
    public static class MockData {
        public List<SportType> sportTypes;
        public List<SkillLevel> skillLevels;
        public List<NotificationChannel> notificationChannels;
        public List<String> defaultNotificationChannels;
        public List<MatchEvent> matchEvents;
    }
}

