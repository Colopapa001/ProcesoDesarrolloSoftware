package com.sportsmatching.model;

import java.util.Objects;

public class SkillLevel {
    private String name;
    private int rank;

    public SkillLevel() {}
    public SkillLevel(String name, int rank) {
        this.name = Objects.requireNonNull(name);
        this.rank = rank;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getRank() { return rank; }
    public void setRank(int rank) { this.rank = rank; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SkillLevel that = (SkillLevel) o;
        return Objects.equals(name, that.name);
    }

    @Override public int hashCode() { return Objects.hash(name); }
    @Override public String toString() { return name; }
}

