package rw.data;
/**
 * CPSC 233 Project Basketball player class ...
 * @author Jarod Rideout, Risha Vaghani, Sardar Waheed
 * @email jarod.rideout@ucalgary.ca, risha.vaghani@ucalgary.ca, sardar.waheed@ucalgary.ca
 * @tutorial Tutorial 1 Tuesdays 11:00 - 13:00
 * @version 3.0
 */

// This class represents a basketball player, extending the abstract Player class.
 public class BasketballPlayer extends Player {
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
