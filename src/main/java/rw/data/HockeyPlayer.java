package rw.data;

public class HockeyPlayer extends Player {
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
