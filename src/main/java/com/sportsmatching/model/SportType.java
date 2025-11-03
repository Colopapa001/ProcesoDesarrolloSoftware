package com.sportsmatching.model;

import java.util.Objects;

public class SportType {
    private String name;

    public SportType() {}
    public SportType(String name) { this.name = Objects.requireNonNull(name); }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SportType sportType = (SportType) o;
        return Objects.equals(name, sportType.name);
    }

    @Override public int hashCode() { return Objects.hash(name); }
    @Override public String toString() { return name; }
}

