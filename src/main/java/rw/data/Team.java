package rw.data;
/**
 * CPSC 233 Project Team class ...
 * @author Jarod Rideout, Risha Vaghani, Sardar Waheed
 * @email jarod.rideout@ucalgary.ca, risha.vaghani@ucalgary.ca, sardar.waheed@ucalgary.ca
 * @tutorial Tutorial 1 Tuesdays 11:00 - 13:00
 * @version 3.0
 */

import java.io.Serializable;
import java.util.Objects;

/**
 *  This class represents a sports team with its relevant statistics.
 */
public class Team implements Comparable<Team>, Serializable {
    private static final long serialVersionUID = 1L;
    private String teamName;
    private String conference;
    private int wins;
    private int losses;
    private int pointsScored;
    private int pointsAllowed;

    // Constructor for a new team.
    public Team(String teamName, String conference) {
        this.teamName = teamName;
        this.conference = conference;
        this.wins = 0;
        this.losses = 0;
        this.pointsScored = 0;
        this.pointsAllowed = 0;
    }

    //  Full constructor for a team with all stats.
    public Team(String teamName, String conference, int wins, int losses, int pointsScored, int pointsAllowed) {
        this.teamName = teamName;
        this.conference = conference;
        this.wins = wins;
        this.losses = losses;
        this.pointsScored = pointsScored;
        this.pointsAllowed = pointsAllowed;
    }

    // All getters
    public String getTeamName() {
        return teamName;
    }

    public String getConference() {
        return conference;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public int getPointsScored() {
        return pointsScored;
    }

    public int getPointsAllowed() {
        return pointsAllowed;
    }

    // All Setters for updating stats
    public void setWins(int wins) {
        this.wins = wins;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public void setPointsScored(int pointsScored) {
        this.pointsScored = pointsScored;
    }

    public void setPointsAllowed(int pointsAllowed) {
        this.pointsAllowed = pointsAllowed;
    }

    // Increment the wins count.
    public void addWin() {
        this.wins++;
    }

    // Increment the losses count.
    public void addLoss() {
        this.losses++;
    }

    // Add points scored in a game.
    public void addPointsScored(int points) {
        this.pointsScored += points;
    }

    //  add points allowed in a game.
    public void addPointsAllowed(int points) {
        this.pointsAllowed += points;
    }

    // Calculate the winning percentage.
    public double getWinningPercentage() {
        int totalGames = wins + losses;
        if (totalGames == 0) return 0.0;
        return (double) wins / totalGames;
    }

    //  Calculate the points differential.
    public int getPointsDifferential() {
        return pointsScored - pointsAllowed;
    }

    //  Implements Comparable to sort teams alphabetically by name.
    @Override
    public int compareTo(Team other) {
        return this.teamName.compareToIgnoreCase(other.teamName);
    }

    // Overrides equals to consider teams equal if they have the same name.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return Objects.equals(teamName, team.teamName);
    }

    // Overrides hashCode to be consistent with equals.
    @Override
    public int hashCode() {
        return Objects.hash(teamName);
    }

    //  Returns a string representation of this team.
    @Override
    public String toString() {
        return String.format("%s (%s) - W: %d, L: %d, PF: %d, PA: %d, Diff: %+d",
                teamName, conference, wins, losses, pointsScored, pointsAllowed, getPointsDifferential());
    }

    // Converts the team to a CSV string for file storage.
    public String toCsv() {
        return String.format("%s,%s,%d,%d,%d,%d",
                teamName, conference, wins, losses, pointsScored, pointsAllowed);
    }

    //  Create a Team object from a CSV line.
    public static Team fromCsv(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length < 6) {
            throw new IllegalArgumentException("Invalid CSV format for team");
        }

        String teamName = parts[0];
        String conference = parts[1];
        int wins = Integer.parseInt(parts[2]);
        int losses = Integer.parseInt(parts[3]);
        int pointsScored = Integer.parseInt(parts[4]);
        int pointsAllowed = Integer.parseInt(parts[5]);

        return new Team(teamName, conference, wins, losses, pointsScored, pointsAllowed);
    }
}
