package rw.data;
/**
 * CPSC 233 Project UserInput class
 * @author Jarod Rideout, Risha Vaghani, Sardar Waheed
 * @email jarod.rideout@ucalgary.ca, risha.vaghani@ucalgary.cra, sardar.waheed@ucalgary.ca
 * @tutorial Tutorial 1 Tuesdays 11:00 - 13:00
 * @version 3.0
 * This class is meant to take in initial data from the user for their bet.
 */

import rw.enums.BetType;
import rw.shell.Main;

import java.util.Arrays;
import java.util.Scanner;

public class UserInput {
    private DataManager dataManager;
    private Scanner scanner;

    // Constructor initializes a new UserInput with the provided DataManager.
    public UserInput(DataManager dataManager) {
        this.dataManager = dataManager;
        this.scanner = new Scanner(System.in);
    }

    // This method collects user input for new bets, teams, and players
    // and also provides access to calculation functions.
    public void collectInput() {

        boolean running = true;

        while (running) {
            System.out.println("\n Data Input Menu ");
            System.out.println("1. Enter a new bet");
            System.out.println("2. Enter a new team");
            System.out.println("3. Enter a new player");
            System.out.println("4. View profit/loss calculations");
            System.out.println("5. Return to main menu");
            System.out.println();

            int choice = Validation.getValidIntegerInput("Enter your choice (1-5): ", 1, 5);

            switch (choice) {
                case 1:
                    collectBetInput();
                    break;
                case 2:
                    collectTeamInput();
                    break;
                case 3:
                    collectPlayerInput();
                    break;
                case 4:
                    viewCalculations();
                    break;
                case 5:
                    running = false;
                    break;
            }
        }
    }

    // Collect input for new bet.
    private void collectBetInput() {
        int betCount = Validation.getValidIntegerInput("How many bets do you want to add? ", 0, 100);

        for (int i = 0; i < betCount; i++) {
            System.out.println("\nAdding bet " + (i + 1) + ": ");
            System.out.println("Enter the following information, hitting ENTER after each answer: \n");

            System.out.println("What bet type will you be making?");
            System.out.println("(1) Moneyline (2) Point Spread (3) Over/Under");
            int betTypeChoice = Validation.getValidIntegerInput("Enter your choice (1-3): ", 1, 3);
            rw.enums.BetType betType = BetType.fromChoice(betTypeChoice);

            // This will get league information. NEEDS VALIDATION WITH ESPN API
            String league = Validation.checkLeague(scanner, "What sport league is the game a part of?");

            // This will get home team information. NEEDS VALIDATION WITH ESPN API
            String homeTeam = Validation.checkTeam(scanner, "Who is the home team?", league);


            // This will get away team information. NEEDS VALIDATION WITH ESPN API
            String awayTeam = Validation.checkTeam(scanner, "Who is the away team?", league);


            // This will get date information. NEEDS VALIDATION WITH ESPN API
            String date = Validation.getValidDate(scanner, "What date does the game take place? (YYYY-MM-DD): ");

            // get bet amount
            double betAmount = Validation.getValidDouble(scanner, "How much are you betting?");

            // Get odds (default to 1.5 if not specified)
            System.out.println("Please use the following information before entering your odds multiplier:" +
                    "\nFor Positive Odds: (|Odds| / 100) + 1" +
                    "\nFor Negative Odds: (100 / |Odds|) + 1" +
                    "\nHit ENTER after you know your multiplier.");
            scanner.nextLine();
            double odds = Validation.getValidDouble(scanner, "Enter multiplier (e.g: 1.5 for -200): ");

            // Confirm information
            System.out.println("\nBet Details:");
            System.out.println("Bet Type: " + betType.getDisplayName());
            System.out.println("League: " + league);
            System.out.println("Home Team: " + homeTeam);
            System.out.println("Away Team: " + awayTeam);
            System.out.println("Date: " + date);
            System.out.println("Bet Amount: $" + betAmount);
            System.out.println("Odds: " + odds);


            System.out.println("\nIs this information correct? Yes / No");
            String confirmation = scanner.nextLine().trim().toLowerCase();
            if (confirmation.equals("yes") || confirmation.equals("y")) {
                Bet bet = dataManager.addBet(date, homeTeam, awayTeam, betType, betAmount, odds, league);
                System.out.println("Bet " + bet.getId() + " added successfully.");
            } else {
                System.out.println("Information is not correct; clearing collection.");
            }
        }
    }

    // Collects input for a new team.
    private void collectTeamInput() {
        int teamCount = Validation.getValidIntegerInput("How many teams do you want to add? ", 0, 100);

        for (int i = 0; i < teamCount; i++) {
            System.out.println("\nAdding team " + (i + 1) + ": ");

            System.out.println("\nEnter the following information for the team:");
            System.out.println("Team name:");
            String teamName = scanner.nextLine().trim();

            System.out.println("Conference:");
            String conference = scanner.nextLine().trim();

            Team team = dataManager.addTeam(teamName, conference);

            System.out.println("\nTeam Details:");
            System.out.println("Team Name: " + team.getTeamName());
            System.out.println("Conference: " + team.getConference());
            System.out.println("Team " + teamName + " added successfully.");
        }
    }


    // Collects input for a new player.
    private void collectPlayerInput() {
        int playerCount = Validation.getValidIntegerInput("How many players do you want to track? ", 0, 100);
        for (int i = 0; i < playerCount; i++) {
            System.out.println("\nAdding player " + (i + 1) + ": ");
            System.out.println("Enter the following information for the player:");
            System.out.println("Enter player name: ");
            String playerName = scanner.nextLine().trim();

            String leagueName = Validation.checkLeague(scanner, "Enter league name:");

            String teamName = Validation.checkTeam(scanner, "Enter team name:", leagueName);

            System.out.println("Enter player position: ");
            String position = scanner.nextLine().trim();

            if (Arrays.asList(Main.nbaTeams).contains(teamName)) {

                double pointsPerGame = Validation.getValidDouble(scanner, "Enter points per game: ");
                double reboundsPerGame = Validation.getValidDouble(scanner, "Enter rebounds per game: ");
                double assistsPerGame = Validation.getValidDouble(scanner, "Enter assists per game: ");

                BasketballPlayer player = dataManager.addBasketballPlayer(
                        playerName, teamName, position, pointsPerGame, reboundsPerGame, assistsPerGame);

                System.out.println("\nPlayer Details:");
                System.out.println("Player Name: " + player.getPlayerName());
                System.out.println("Team Name: " + player.getTeamName());
                System.out.println("Position: " + player.getPosition());
                System.out.println("Player " + playerName + " added successfully.");
            } else if (Arrays.asList(Main.nhlTeams).contains(teamName)) {

                double pointsPerGame = Validation.getValidDouble(scanner, "Enter points per game: ");
                double assistsPerGame = Validation.getValidDouble(scanner, "Enter assists per game: ");

                HockeyPlayer player = dataManager.addHockeyPlayer(
                        playerName, teamName, position, pointsPerGame, assistsPerGame);

                System.out.println("\nPlayer Details:");
                System.out.println("Player Name: " + player.getPlayerName());
                System.out.println("Team Name: " + player.getTeamName());
                System.out.println("Position: " + player.getPosition());
                System.out.println("Player " + playerName + " added successfully.");
            }
        }
    }

    // Displays the profit/loss calculations and most profitable bet type
    private void viewCalculations() {
        if (dataManager.getAllBets().isEmpty()) {
            System.out.println("No bets to analyze.");
            return;
        }

        System.out.println("\nProfit/Loss Calculations.");

        // Display total profit/loss
        double totalProfit = dataManager.calculateTotalProfitLoss();
        System.out.println("Total profit/loss: $" + String.format("%.2f", totalProfit));

        // Display profit by bet type
        System.out.println("\nProfit/Loss by Bet Type:");
        for (BetType type : BetType.values()) {
            double profit = dataManager.getProfitLossByBetType().getOrDefault(type, 0.0);
            System.out.println(type.getDisplayName() + ": $" + String.format("%.2f", profit));
        }

        // Display most profitable bet type
        BetType mostProfitable = dataManager.getMostProfitableBetType();
        if (mostProfitable != null) {
            System.out.println("\nMost profitable bet type: " + mostProfitable.getDisplayName());
        } else {
            System.out.println("\nNo profitable bet types found.");
        }

        // wait for user to press enter
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
}
