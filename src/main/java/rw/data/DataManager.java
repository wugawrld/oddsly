package rw.data;
/**
 * CPSC 233 Project DataManager class ...
 * @author Jarod Rideout, Risha Vaghani, Sardar Waheed
 * @email jarod.rideout@ucalgary.ca, risha.vaghani@ucalgary.ca, sardar.waheed@ucalgary.ca
 * @tutorial Tutorial 1 Tuesdays 11:00 - 13:00
 * @version 3.0
*/

import rw.enums.BetType;
import rw.enums.BetOutcome;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * This class manages all data operations for the sports betting tracker,
 * including data storage, updating and file operations.
 */

public class DataManager {
    private List<Bet> bets;
    private List<Team> teams;
    private List<Player> players;
    private int betCounter;

    // File Paths
    private static final String BETS_FILE = "bets.csv";
    private static final String TEAMS_FILE = "teams.csv";
    private static final String PLAYERS_FILE = "players.csv";

    // Constructor initializes empty collections.
    public DataManager() {
        bets = new ArrayList<>();
        teams = new ArrayList<>();
        players = new ArrayList<>();
        betCounter = 0;
    }

                                                 // Bet Details

    //Add new bet to the system.
    public Bet addBet(String gameDate, String team1, String team2, BetType betType,
                      double amountWagered, double odds, String league) {
        String betId = "bet" + (++betCounter);
        Bet bet = new Bet(betId, gameDate, team1, team2, betType, amountWagered, odds, BetOutcome.PENDING, league);
        bets.add(bet);
        return bet;
    }

    // Get a list of all bets.
    public List<Bet> getAllBets() {
        return new ArrayList<>(bets); // Return a copy to prevent external modification
    }

    // Get a bet by its ID.
    public Bet getBetById(String id) {
        for (Bet bet : bets) {
            if (bet.getId().equals(id)) {
                return bet;
            }
        }
        return null;
    }

    // Get a bet by its index in the list.
    public Bet getBetByIndex(int index) {
        if (index >= 0 && index < bets.size()) {
            return bets.get(index);
        }
        return null;
    }

    // Edit the bet.
    public void editBet() {
        Scanner scanner = new Scanner(System.in);
        if (bets.isEmpty()) {
            System.out.println("There are no bets available to edit.");
            return;
        }

        int betNumber = Validation.getValidIntegerInput("Enter the number of the bet" +
                " you wish to edit 1-"+bets.size()+"): ", 1,bets.size());
        Bet bet = bets.get(betNumber-1);

        System.out.println("\nEditing Bet " + betNumber + ":");
        System.out.println("Current Bet Information");
        System.out.println("1. Bet Type: " + bet.getBetType());
        System.out.println("2. League: " + bet.getLeague());
        System.out.println("3. Home Team: " + bet.getTeam1());
        System.out.println("4. Away Team: " + bet.getTeam2());
        System.out.println("5. Date: " + bet.getGameDate());
        System.out.println("6. Bet Amount: " + bet.getAmountWagered());
        System.out.println("7. Return to previous menu");

        int betUpdateChoice = Validation.getValidIntegerInput("Which bet information would you" +
                " like to edit (1-6):", 1,6);

        switch(betUpdateChoice) {
            case 1:
                // This loop prompts user to enter a valid bet type.
                System.out.println("What bet type will you be making?");
                System.out.println("(1) Moneyline (2) Point Spread (3) Over/Under");
                int betTypeChoice = Validation.getValidIntegerInput("Enter your choice (1-3): ", 1, 3);
                BetType betType = BetType.fromChoice(betTypeChoice);
                System.out.println("Your updated bet type is: " + betType);
                bet.setBetType(betType);
                break;

            case 2:
                String league = Validation.checkLeague(scanner, "Enter new league (NBA,NHL): ");
                bet.setLeague(league);
                break;

            case 3:
                String homeTeam = Validation.checkTeam(scanner, "Enter new home team: ", bet.getLeague());
                bet.setTeam1(homeTeam);
                break;

            case 4:
                String awayTeam = Validation.checkTeam(scanner, "Enter new away team: ", bet.getLeague());
                bet.setTeam2(awayTeam);
                break;

            case 5:
                String date = Validation.getValidDate(scanner, "Enter new date (YYYY-MM-DD): ");
                bet.setFormattedGameDate(date);
                break;

            case 6:
                Double betAmount = Validation.getValidDouble(scanner, "Enter new bet amount: ");
                bet.setAmountWagered(betAmount);
                break;

            case 7:
                return;
        }
        System.out.println("Bet update completed.");
    }

    // Update a bet's outcome and recalculate payout.
    public boolean updateBetOutcome(String betId, BetOutcome outcome) {
        Bet bet = getBetById(betId);
        if (bet != null) {
            bet.updateOutcome(outcome);
            return true;
        }
        return false;
    }

    // Update a bet's outcome by index.
    public boolean updateBetOutcomeByIndex(int index, BetOutcome outcome) {
        Bet bet = getBetByIndex(index);
        if (bet != null) {
            bet.updateOutcome(outcome);
            return true;
        }
        return false;
    }

    //  Calculate total profit/loss from all bets.
    public double calculateTotalProfitLoss() {
        double total = 0;
        for (Bet bet : bets) {
            if (bet.getOutcome() == BetOutcome.WIN) {
                total += bet.getPayout() - bet.getAmountWagered();
            } else if (bet.getOutcome() == BetOutcome.LOSS) {
                total -= bet.getAmountWagered();
            }
        }
        return total;
    }

    // Get profit/loss by bet type.
    public Map<BetType, Double> getProfitLossByBetType() {
        Map<BetType, Double> profitByType = new HashMap<>();
        Map<BetType, Integer> countByType = new HashMap<>();

        for (Bet bet : bets) {
            if (bet.getOutcome() != BetOutcome.PENDING) {
                BetType type = bet.getBetType();
                double currentProfit = profitByType.getOrDefault(type, 0.0);
                int currentCount = countByType.getOrDefault(type, 0);

                if (bet.getOutcome() == BetOutcome.WIN) {
                    currentProfit += bet.getPayout() - bet.getAmountWagered();
                } else {
                    currentProfit -= bet.getAmountWagered();
                }

                profitByType.put(type, currentProfit);
                countByType.put(type, currentCount + 1);
            }
        }
        return profitByType;
    }

    // Find the most profitable bet type.
    public BetType getMostProfitableBetType() {
        Map<BetType, Double> profitByType = getProfitLossByBetType();
        BetType mostProfitable = null;
        double highestProfit = Double.NEGATIVE_INFINITY;

        for (Map.Entry<BetType, Double> entry : profitByType.entrySet()) {
            if (entry.getValue() > highestProfit) {
                highestProfit = entry.getValue();
                mostProfitable = entry.getKey();
            }
        }
        return mostProfitable;
    }

    // Get bets filtered by team.
    public List<Bet> getBetsByTeam(String teamName) {
        List<Bet> teamBets = new ArrayList<>();
        for (Bet bet : bets) {
            if (bet.getTeam1().equalsIgnoreCase(teamName) || bet.getTeam2().equalsIgnoreCase(teamName)) {
                teamBets.add(bet);
            }
        }
        return teamBets;
    }

    // Get bets filtered by date.
    public List<Bet> getBetsByDate(String date) {
        List<Bet> dateBets = new ArrayList<>();
        for (Bet bet : bets) {
            if (bet.getFormattedGameDate().equals(date)) {
                dateBets.add(bet);
            }
        }
        return dateBets;
    }

                                                // Team Details
    //Add a new team to the system.
    public Team addTeam(String teamName, String conference) {
        // check if team already exists
        for (Team team : teams) {
            if (team.getTeamName().equalsIgnoreCase(teamName)) {
                return team; // team already exists
            }
        }
        Team team = new Team(teamName, conference);
        teams.add(team);
        return team;
    }

    //  Get a list of all teams.
    public List<Team> getAllTeams() {
        return new ArrayList<>(teams);
    }

    //  Get a team by its name.
    public Team getTeamByName(String teamName) {
        for (Team team : teams) {
            if (team.getTeamName().equalsIgnoreCase(teamName)) {
                return team;
            }
        }
        return null;
    }

    // Get a team by its index in the list.
    public Team getTeamByIndex(int index) {
        if (index >= 0 && index < teams.size()) {
            return teams.get(index);
        }
        return null;
    }

    // Update team statistics.
    public boolean updateTeamStats(String teamName, int wins, int losses, int pointsScored, int pointsAllowed) {
        Team team = getTeamByName(teamName);
        if (team != null) {
            team.setWins(wins);
            team.setLosses(losses);
            team.setPointsScored(pointsScored);
            team.setPointsAllowed(pointsAllowed);
            return true;
        }
        return false;
    }

    // Update team statistics by index.
    public boolean updateTeamStatsByIndex(int index, int wins, int losses, int pointsScored, int pointsAllowed) {
        Team team = getTeamByIndex(index);
        if (team != null) {
            team.setWins(wins);
            team.setLosses(losses);
            team.setPointsScored(pointsScored);
            team.setPointsAllowed(pointsAllowed);
            return true;
        }
        return false;
    }


                                              // Player details.

    // Add a new basketball player to the system.
    public BasketballPlayer addBasketballPlayer(String playerName, String teamName, String position,
                                                double pointsPerGame, double reboundsPerGame, double assistsPerGame) {
        // Check if player already exists
        for (Player player : players) {
            if (player.getPlayerName().equalsIgnoreCase(playerName) &&
                    player.getTeamName().equalsIgnoreCase(teamName)) {
                if (player instanceof BasketballPlayer) {
                    return (BasketballPlayer) player; // player already exists
                }
            }
        }

        BasketballPlayer player = new BasketballPlayer(playerName, teamName, position,
                pointsPerGame, reboundsPerGame, assistsPerGame);
        players.add(player);
        return player;
    }

    // Add a new hockey player to the system.
    public HockeyPlayer addHockeyPlayer(String playerName, String teamName, String position,
                                        double pointsPerGame, double assistsPerGame) {
        // Check if player already exists
        for (Player player : players) {
            if (player.getPlayerName().equalsIgnoreCase(playerName) &&
                    player.getTeamName().equalsIgnoreCase(teamName)) {
                if (player instanceof HockeyPlayer) {
                    return (HockeyPlayer) player; // player already exists
                }
            }
        }

        HockeyPlayer player = new HockeyPlayer(playerName, teamName, position,
                pointsPerGame, assistsPerGame);
        players.add(player);
        return player;
    }

     // Get a list of all players.
    public List<Player> getAllPlayers() {
        return new ArrayList<>(players);
    }

     //  Get a player by name and team.
    public Player getPlayerByNameAndTeam(String playerName, String teamName) {
        for (Player player : players) {
            if (player.getPlayerName().equalsIgnoreCase(playerName) &&
                    player.getTeamName().equalsIgnoreCase(teamName)) {
                return player;
            }
        }
        return null;
    }

     //  Get a player by index in the list.
    public Player getPlayerByIndex(int index) {
        if (index >= 0 && index < players.size()) {
            return players.get(index);
        }
        return null;
    }

     // Update basketball player statistics.
    public boolean updateBasketballPlayerStats(String playerName, String teamName,
                                               double pointsPerGame, double reboundsPerGame, double assistsPerGame) {
        Player player = getPlayerByNameAndTeam(playerName, teamName);
        if (player instanceof BasketballPlayer) {
            BasketballPlayer basketballPlayer = (BasketballPlayer) player;
            basketballPlayer.setPointsPerGame(pointsPerGame);
            basketballPlayer.setReboundsPerGame(reboundsPerGame);
            basketballPlayer.setAssistsPerGame(assistsPerGame);
            return true;
        }
        return false;
    }

    // Update hockey player statistics.
    public boolean updateHockeyPlayerStats(String playerName, String teamName,
                                               double pointsPerGame, double assistsPerGame) {
        Player player = getPlayerByNameAndTeam(playerName, teamName);
        if (player instanceof HockeyPlayer) {
            HockeyPlayer hockeyPlayer = (HockeyPlayer) player;
            hockeyPlayer.setPointsPerGame(pointsPerGame);
            hockeyPlayer.setAssistsPerGame(assistsPerGame);
            return true;
        }
        return false;
    }

     // Update basketball player stats by index.
    public boolean updateBasketballPlayerStatsByIndex(int index,
                                                      double pointsPerGame, double reboundsPerGame, double assistsPerGame) {
        Player player = getPlayerByIndex(index);
        if (player instanceof BasketballPlayer) {
            BasketballPlayer basketballPlayer = (BasketballPlayer) player;
            basketballPlayer.setPointsPerGame(pointsPerGame);
            basketballPlayer.setReboundsPerGame(reboundsPerGame);
            basketballPlayer.setAssistsPerGame(assistsPerGame);
            return true;
        }
        return false;
    }

    // Update basketball player stats by index.
    public boolean updateHockeyPlayerStatsByIndex(int index,
                                                      double pointsPerGame, double assistsPerGame) {
        Player player = getPlayerByIndex(index);
        if (player instanceof HockeyPlayer) {
            HockeyPlayer hockeyPlayer = (HockeyPlayer) player;
            hockeyPlayer.setPointsPerGame(pointsPerGame);
            hockeyPlayer.setAssistsPerGame(assistsPerGame);
            return true;
        }
        return false;
    }

                                                //  File I/O data

    // Save all data to CSV files.
    public void saveToFiles() throws IOException {
        // save bets
        try (PrintWriter writer = new PrintWriter(new FileWriter(BETS_FILE,true))) {
            for (Bet bet : bets) {
                writer.println(bet.toCsv());
            }
        }

        // save teams
        try (PrintWriter writer = new PrintWriter(new FileWriter(TEAMS_FILE, true))) {
            for (Team team : teams) {
                writer.println(team.toCsv());
            }
        }

        // save players
        try (PrintWriter writer = new PrintWriter(new FileWriter(PLAYERS_FILE, true))) {
            for (Player player : players) {
                if (player instanceof BasketballPlayer) {
                    writer.println(((BasketballPlayer) player).toCsv());
                }
                else if(player instanceof HockeyPlayer) {
                    writer.println(((HockeyPlayer) player).toCsv());
                }
            }
        }

        System.out.println("Data saved successfully to CSV files.");
        // clear out local data that has been saved to CSV.
        bets.clear();
        teams.clear();
        players.clear();
        betCounter = 0;

    }

    // Load data from CSV files.
    public void loadFromFiles() throws IOException {
        // clear existing data
        bets.clear();
        teams.clear();
        players.clear();
        betCounter = 0;

        // load bets
        if (Files.exists(Paths.get(BETS_FILE))) {
            List<String> betLines = Files.readAllLines(Paths.get(BETS_FILE));
            for (String line : betLines) {
                try {
                    Bet bet = Bet.fromCsv(line);
                    // for each bet successfully read and created from BETS_FILE update betCounter
                    betCounter++;
                    // Set bet ID to be what ever index they are in BETS_FILE
                    bet.setId("bet"+betCounter);
                    bets.add(bet);

                    // update bet counter
                    String id = bet.getId();
                    if (id.startsWith("bet")) {
                        try {
                            int count = Integer.parseInt(id.substring(3));
                            if (count > betCounter) {
                                betCounter = count;
                            }
                        } catch (NumberFormatException e) {
                            // ignore invalid bet IDs
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error loading bet: " + e.getMessage());
                }
            }
        }

        // Load teams
        if (Files.exists(Paths.get(TEAMS_FILE))) {
            List<String> teamLines = Files.readAllLines(Paths.get(TEAMS_FILE));
            for (String line : teamLines) {
                try {
                    Team team = Team.fromCsv(line);
                    teams.add(team);
                } catch (Exception e) {
                    System.err.println("Error loading team: " + e.getMessage());
                }
            }
        }

        // Load players
        if (Files.exists(Paths.get(PLAYERS_FILE))) {
            List<String> playerLines = Files.readAllLines(Paths.get(PLAYERS_FILE));
            for (String line : playerLines) {
                try {
                    String[] parts = line.split(",");
                    // if basketball player load basketball player format
                    if (parts.length > 3 && "BasketballPlayer".equals(parts[3])) {
                        BasketballPlayer player = BasketballPlayer.fromCsv(line);
                        players.add(player);
                    }
                    // else load hockey player format
                    else if (parts.length > 3 && "HockeyPlayer".equals(parts[3])){
                        HockeyPlayer player = HockeyPlayer.fromCsv(line);
                        players.add(player);
                    }
                } catch (Exception e) {
                    System.err.println("Error loading player: " + e.getMessage());
                }
            }
        }

        System.out.println("Data loaded successfully from CSV files.");
    }

                                     // Data Updating UI Methods

    //Updates the outcome of a bet from pending to either win or loss.
    public void updateBetOutcome(Scanner scanner) {
        if (bets.isEmpty()) {
            System.out.println("No bets to update.Please add bets first.");
            return;
        }

        // Display all bets to the user
        displayBets();

        // Get the bet to update
        int betIndex = Validation.getValidIntegerInput("Enter the number of the bet to update: ",
                1, bets.size()) - 1;

        Bet bet = getBetByIndex(betIndex);
        if (bet == null) {
            System.out.println("invalid bet selection.");
            return;
        }

        // Display current outcome
        System.out.println("Current outcome: " + bet.getOutcome().getDisplayName());

        // Get the new outcome
        System.out.println("Update outcome to: (1) Win (2) Loss");
        int outcomeChoice = Validation.getValidIntegerInput("Enter your choice (1-2): ", 1, 2);

        BetOutcome newOutcome = BetOutcome.fromChoice(outcomeChoice);
        updateBetOutcomeByIndex(betIndex, newOutcome);

        System.out.println("Bet updated successfully!");
        System.out.println("New outcome: " + newOutcome.getDisplayName());
        System.out.println("Payout: $" + String.format("%.2f", bet.getPayout()));
    }

    // Updates the statistics for a selected team.
    public void updateTeamStats(Scanner scanner) {
        if (teams.isEmpty()) {
            System.out.println("No teams to update. Please add teams first.");
            return;
        }

        // Display all teams to the user
        displayTeams();

        // Get the team to update
        int teamIndex = Validation.getValidIntegerInput("Enter the number of the team to update: ",
                1, teams.size()) - 1;

        Team team = getTeamByIndex(teamIndex);
        if (team == null) {
            System.out.println("Invalid team selection.");
            return;
        }

        // Display current stats
        System.out.println("\nCurrent stats for " + team.getTeamName() + ":");
        System.out.println("Wins: " + team.getWins());
        System.out.println("Losses: " + team.getLosses());
        System.out.println("Points Scored: " + team.getPointsScored());
        System.out.println("Points Allowed: " + team.getPointsAllowed());

        // Get updated wins
        int wins = Validation.getValidIntegerInput("Enter new wins value: ", 0, 100);

        // Get updated losses
        int losses = Validation.getValidIntegerInput("Enter new losses value: ", 0, 100);

        // Get updated points scored
        int pointsScored = Validation.getValidIntegerInput("Enter new points scored value: ", 0, 10000);

        // Get updated points allowed
        int pointsAllowed = Validation.getValidIntegerInput("Enter new points allowed value: ", 0, 10000);

        updateTeamStatsByIndex(teamIndex, wins, losses, pointsScored, pointsAllowed);

        System.out.println("Team stats updated successfully!");
    }

     // Updates the statistics for a selected player.
    public void updatePlayerStats(Scanner scanner) {
        if (players.isEmpty()) {
            System.out.println("No players to update. Please add players first.");
            return;
        }

        // Display all players to the user
        displayPlayers();

        // Get the player to update
        int playerIndex = Validation.getValidIntegerInput("Enter the number of the player to update: ",
                1, players.size()) - 1;

        Player player = getPlayerByIndex(playerIndex);
        if (player == null) {
            System.out.println("Invalid player selection.");
            return;
        }

        if (player instanceof BasketballPlayer) {
            updateBasketballPlayerStats(playerIndex, (BasketballPlayer) player, scanner);
        } else if(player instanceof HockeyPlayer) {
            updateHockeyPlayerStats(playerIndex, (HockeyPlayer) player, scanner);
        } else {
            System.out.println("Unsupported player type.");
        }
    }

    // Updates statistics for a basketball player.
    private void updateBasketballPlayerStats(int playerIndex, BasketballPlayer player, Scanner scanner) {
        // Display current stats
        System.out.println("\nCurrent stats for " + player.getPlayerName() + ":");
        System.out.println("Points per game: " + player.getPointsPerGame());
        System.out.println("Rebounds per game: " + player.getReboundsPerGame());
        System.out.println("Assists per game: " + player.getAssistsPerGame());

        // Get updated points per game
        double pointsPerGame = Validation.getValidDouble(scanner, "Enter new points per game value: ");

        // Get updated rebounds per game
        double reboundsPerGame = Validation.getValidDouble(scanner, "Enter new rebounds per game value: ");

        // Get updated assists per game
        double assistsPerGame = Validation.getValidDouble(scanner, "Enter new assists per game value: ");

        updateBasketballPlayerStatsByIndex(playerIndex, pointsPerGame, reboundsPerGame, assistsPerGame);

        System.out.println("Player stats updated successfully!");
    }

    // Updates statistics for a Hockey player.
    private void updateHockeyPlayerStats(int playerIndex, HockeyPlayer player, Scanner scanner) {
        // Display current stats
        System.out.println("\nCurrent stats for " + player.getPlayerName() + ":");
        System.out.println("Points per game: " + player.getPointsPerGame());
        System.out.println("Assists per game: " + player.getAssistsPerGame());

        // Get updated points per game
        double pointsPerGame = Validation.getValidDouble(scanner, "Enter new points per game value: ");

        // Get updated assists per game
        double assistsPerGame = Validation.getValidDouble(scanner, "Enter new assists per game value: ");

        updateHockeyPlayerStatsByIndex(playerIndex, pointsPerGame, assistsPerGame);

        System.out.println("Player stats updated successfully!");
    }

    // Display all bets.
    private void displayBets() {
        System.out.println("\nAll Bets:");
        int i = 1;
        for (Bet bet : bets) {
            System.out.printf("%d. %s vs %s (%s) - %s, Amount: $%.2f, Odds: %.2f, Outcome: %s%n",
                    i++, bet.getTeam1(), bet.getTeam2(), bet.getLeague(),
                    bet.getBetType().getDisplayName(), bet.getAmountWagered(),
                    bet.getOdds(), bet.getOutcome().getDisplayName());
        }
    }

    // Display all teams.
    private void displayTeams() {
        System.out.println("\nAll Teams:");
        int i = 1;
        for (Team team : teams) {
            System.out.printf("%d. %s (%s) - W: %d, L: %d, PF: %d, PA: %d%n",
                    i++, team.getTeamName(), team.getConference(),
                    team.getWins(), team.getLosses(),
                    team.getPointsScored(), team.getPointsAllowed());
        }
    }

     // Display all players.
    private void displayPlayers() {
        System.out.println("\nAll Players:");
        int i = 1;
        for (Player player : players) {
            if (player instanceof BasketballPlayer) {
                BasketballPlayer bbPlayer = (BasketballPlayer) player;
                System.out.printf("%d. %s (%s) - %s, PPG: %.1f, RPG: %.1f, APG: %.1f%n",
                        i++, bbPlayer.getPlayerName(), bbPlayer.getTeamName(),
                        bbPlayer.getPosition(), bbPlayer.getPointsPerGame(),
                        bbPlayer.getReboundsPerGame(), bbPlayer.getAssistsPerGame());
            } else if (player instanceof HockeyPlayer) {
                HockeyPlayer hockeyPlayer = (HockeyPlayer) player;
                System.out.printf("%d. %s (%s) - %s, PPG: %.1f, APG: %.1f%n",
                        i++, hockeyPlayer.getPlayerName(), hockeyPlayer.getTeamName(),
                        hockeyPlayer.getPosition(), hockeyPlayer.getPointsPerGame(),
                        hockeyPlayer.getAssistsPerGame());
            } else {System.out.printf("%d. %s (%s) - %s%n",
                        i++, player.getPlayerName(), player.getTeamName(), player.getPosition());
            }
        }
    }
}