package rw.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * This file contains player related classes (Player abstract class, BasketballPlayer and
 * HockeyPlayer implementation.)
 * Abstract class representing a sports player.
 */

public abstract class Player implements Comparable<Player> {
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

 // This class represents a basketball player, extending the abstract Player class.
class BasketballPlayer extends Player {
    private static final String POINTS_PER_GAME = "pointsPerGame";
    private static final String REBOUNDS_PER_GAME = "reboundsPerGame";
    private static final String ASSISTS_PER_GAME = "assistsPerGame";

    //Constructor for a basketball player with basic stats.
    public BasketballPlayer(String playerName, String teamName, String position,
                            double pointsPerGame, double reboundsPerGame, double assistsPerGame) {
        super(playerName, teamName, position);

        // Initialize basketball specific stats
        stats.put(POINTS_PER_GAME, pointsPerGame);
        stats.put(REBOUNDS_PER_GAME, reboundsPerGame);
        stats.put(ASSISTS_PER_GAME, assistsPerGame);
    }

    // Get points per game.
    public double getPointsPerGame() {
        return stats.getOrDefault(POINTS_PER_GAME, 0.0);
    }

    // Get rebounds per game.
    public double getReboundsPerGame() {
        return stats.getOrDefault(REBOUNDS_PER_GAME, 0.0);
    }

    // Get assists per game.
    public double getAssistsPerGame() {
        return stats.getOrDefault(ASSISTS_PER_GAME, 0.0);
    }

   // Update points per game.
    public void setPointsPerGame(double pointsPerGame) {
        stats.put(POINTS_PER_GAME, pointsPerGame);
    }

    // Update rebounds per game.
    public void setReboundsPerGame(double reboundsPerGame) {
        stats.put(REBOUNDS_PER_GAME, reboundsPerGame);
    }

    // Update assists per game.
    public void setAssistsPerGame(double assistsPerGame) {
        stats.put(ASSISTS_PER_GAME, assistsPerGame);
    }


    // Calculate player efficiency rating.
    // This is a basic metric combining points, rebounds and assists.

    @Override
    public double calculatePerformanceMetric() {
        return getPointsPerGame() + (getReboundsPerGame() * 0.7) + (getAssistsPerGame() * 1.3);
    }

    //  Return the player type.
    @Override
    public String getPlayerType() {
        return "BasketballPlayer";
    }

    // Returns a string representation of this basketball player with stats.
    @Override
    public String toString() {
        return String.format("%s - PPG: %.1f, RPG: %.1f, APG: %.1f, Performance: %.1f",
                super.toString(), getPointsPerGame(), getReboundsPerGame(),
                getAssistsPerGame(), calculatePerformanceMetric());
    }

    // Converts the player to a CSV string for file storage.
    public String toCsv() {
        return String.format("%s,%s,%.2f,%.2f,%.2f",
                super.getBaseCsvData(), getPlayerType(),
                getPointsPerGame(), getReboundsPerGame(), getAssistsPerGame());
    }

    // Create a BasketballPlayer object from a CSV line.
    public static BasketballPlayer fromCsv(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length < 7) {
            throw new IllegalArgumentException("Invalid CSV format for BasketballPlayer");
        }

        String playerName = parts[0];
        String teamName = parts[1];
        String position = parts[2];
        double pointsPerGame = Double.parseDouble(parts[4]);
        double reboundsPerGame = Double.parseDouble(parts[5]);
        double assistsPerGame = Double.parseDouble(parts[6]);

        return new BasketballPlayer(playerName, teamName, position,
                pointsPerGame, reboundsPerGame, assistsPerGame);
    }
}

class HockeyPlayer extends Player {
    private static final String POINTS_PER_GAME = "pointsPerGame";
    private static final String ASSISTS_PER_GAME = "assistsPerGame";


    //Constructor for a hockey player with basic stats.
    public HockeyPlayer(String playerName, String teamName, String position,
                            double pointsPerGame, double assistsPerGame) {
        super(playerName, teamName, position);

        // Initialize basketball-specific stats
        stats.put(POINTS_PER_GAME, pointsPerGame);
        stats.put(ASSISTS_PER_GAME, assistsPerGame);
    }

    // Get points per game.
    public double getPointsPerGame() {
        return stats.getOrDefault(POINTS_PER_GAME, 0.0);
    }

    // Get assists per game.
    public double getAssistsPerGame() {
        return stats.getOrDefault(ASSISTS_PER_GAME, 0.0);
    }

    // Update points per game.
    public void setPointsPerGame(double pointsPerGame) {
        stats.put(POINTS_PER_GAME, pointsPerGame);
    }

    // Update assists per game.
    public void setAssistsPerGame(double assistsPerGame) {
        stats.put(ASSISTS_PER_GAME, assistsPerGame);
    }


    // Calculate player efficiency rating.
    // This is a basic metric combining points, rebounds, and assists.

    @Override
    public double calculatePerformanceMetric() {
        return getPointsPerGame() + (getAssistsPerGame() * 1.3);
    }

    //  Return the player type.
    @Override
    public String getPlayerType() {
        return "HockeyPlayer";
    }

    // Returns a string representation of this hockey player with stats.
    @Override
    public String toString() {
        return String.format("%s - PPG: %.1f, APG: %.1f, Performance: %.1f",
                super.toString(), getPointsPerGame(),
                getAssistsPerGame(), calculatePerformanceMetric());
    }

    // Converts the player to a CSV string for file storage.
    public String toCsv() {
        return String.format("%s,%s,%.2f,%.2f",
                super.getBaseCsvData(), getPlayerType(),
                getPointsPerGame(), getAssistsPerGame());
    }

    // Create a HockeyPlayer object from a CSV line.
    public static HockeyPlayer fromCsv(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length < 6) {
            throw new IllegalArgumentException("Invalid CSV format for HockeyPlayer");
        }

        String playerName = parts[0];
        String teamName = parts[1];
        String position = parts[2];
        double pointsPerGame = Double.parseDouble(parts[4]);
        double assistsPerGame = Double.parseDouble(parts[5]);

        return new HockeyPlayer(playerName, teamName, position,
                pointsPerGame, assistsPerGame);
    }
}