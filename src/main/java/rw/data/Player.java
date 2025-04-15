package rw.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * CPSC 233 Project Player class ...
 * @author Jarod Rideout, Risha Vaghani, Sardar Waheed
 * @email jarod.rideout@ucalgary.ca, risha.vaghani@ucalgary.ca, sardar.waheed@ucalgary.ca
 * @tutorial Tutorial 1 Tuesdays 11:00 - 13:00
 * @version 3.0
 *
 * This file contains player related classes (Player abstract class, BasketballPlayer and
 * HockeyPlayer implementation.)
 * Abstract class representing a sports player.
 */

public abstract class Player implements Comparable<Player>, Serializable {
    private static final long serialVersionUID = 1L;
    private String playerName;
    private String teamName;
    private String position;
    protected Map<String, Double> stats;


    // Constructor for a player.
    public Player(String playerName, String teamName, String position) {
        this.playerName = playerName;
        this.teamName = teamName;
        this.position = position;
        this.stats = new HashMap<>();
    }

    // Getters
    public String getPlayerName() {
        return playerName;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getPosition() {
        return position;
    }


    //Get a specific stat value.
    public double getStat(String statName) {
        return stats.getOrDefault(statName, 0.0);
    }

    // Get all stats.
    public Map<String, Double> getStats() {
        return new HashMap<>(stats); // Return a copy to prevent modification
    }

    // Update a specific stat value.
    public void setStat(String statName, double value) {
        stats.put(statName, value);
    }

     // Increment a stat by a given amount.
    public void incrementStat(String statName, double amount) {
        double currentValue = stats.getOrDefault(statName, 0.0);
        stats.put(statName, currentValue + amount);
    }

    // Abstract method to be implemented by subclasses to calculate player-specific performance metrics.
    public abstract double calculatePerformanceMetric();


    // Abstract method to get the type of player (example: "BasketballPlayer").
    public abstract String getPlayerType();


    // Implements Comparable to sort players by team name, then player name.
    @Override
    public int compareTo(Player other) {
        int teamComparison = this.teamName.compareToIgnoreCase(other.teamName);
        if (teamComparison != 0) {
            return teamComparison;
        }
        return this.playerName.compareToIgnoreCase(other.playerName);
    }

    // Overrides equals to consider players equal if they have the same name and team.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(playerName, player.playerName) &&
                Objects.equals(teamName, player.teamName);
    }

    // Overrides hashCode to be consistent with equals.
    @Override
    public int hashCode() {
        return Objects.hash(playerName, teamName);
    }

    // Returns a string representation of this player.
    @Override
    public String toString() {
        return String.format("%s (%s) - %s", playerName, teamName, position);
    }

   // Basic CSV representation to be extended by subclasses.
    public String getBaseCsvData() {
        return String.format("%s,%s,%s", playerName, teamName, position);
    }
}

