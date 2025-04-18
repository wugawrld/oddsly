package rw.shell;



import java.util.Scanner;
import java.io.IOException;
import java.util.Map;
import rw.enums.*;
import rw.data.*;

/**
 * CPSC 233 Project Menu class ...
 * @author Jarod Rideout, Risha Vaghani, Sardar Waheed
 * @email jarod.rideout@ucalgary.ca, risha.vaghani@ucalgary.ca, sardar.waheed@ucalgary.ca
 * @tutorial Tutorial 1 Tuesdays 11:00 - 13:00
 * @version 3.0
 * This class handles the menu system for the sports betting application.
 */

public class Menu {
    private final Scanner scanner;
    private final DataManager dataManager;
    private final UserInput userInput;
    private final Main main;

    // Constructor for menu.
    public Menu(DataManager dataManager) {
        this.scanner = new Scanner(System.in);
        this.dataManager = dataManager;
        this.userInput = new UserInput(dataManager);
        this.main = new Main();
    }

    // Start the main menu.
    public void startMenu() {
        boolean running = true;

        while (running) {
            displayMainMenu();
            int menuChoice = Validation.getValidIntegerInput("Enter your choice (1-6): ", 1, 6);

            switch (menuChoice) {
                case 1:
                    userInput.collectInput();
                    break;
                case 2:
                    handleDataUpdates();
                    break;
                case 3:
                    viewData();
                    break;
                case 4:
                    viewCalculationsMenu(dataManager, scanner);
                    break;
                case 5:
                    handleFileSaving();
                    break;
                case 6:
                    running = false;
                    System.out.println("Thank you for using the Sports Betting Tracker. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid input please enter (1-6).");
            }
        }
        // close scanner as last action
        scanner.close();
    }

    // Display the main menu options.
    private void displayMainMenu() {
        System.out.println("\n Welcome to Sports Betting Tracker System");
        System.out.println("1. Data Entry (Add bets, teams, players)");
        System.out.println("2. Data Updates (Update outcomes, stats)");
        System.out.println("3. View Data (Bets, teams, players)");
        System.out.println("4. Calculations & Analysis");
        System.out.println("5. Save/Load Data");
        System.out.println("6. Exit");
        System.out.println();
    }

    // Dedicated menu for calculations and analysis.
    public void viewCalculationsMenu(DataManager dataManager, Scanner scanner) {
        boolean calculationsMenu = true;

        while (calculationsMenu) {
            System.out.println("\n Calculations & Analysis Menu ");
            System.out.println("1. Overall Profit/Loss Summary");
            System.out.println("2. Profit/Loss by Bet Type");
            System.out.println("3. Most Profitable Bet Type Analysis");
            System.out.println("4. Team Performance Analysis");
            System.out.println("5. Return to Main Menu");
            System.out.println();

            int choice = Validation.getValidIntegerInput("Enter your choice (1-5): ", 1, 5);

            switch (choice) {
                case 1:
                    main.displayOverallProfitLoss(dataManager);
                    break;
                case 2:
                    main.displayProfitByBetType(dataManager);
                    break;
                case 3:
                    main.displayMostProfitableBetType(dataManager);
                    break;
                case 4:
                    main.displayTeamPerformance(dataManager, scanner);
                    break;
                case 5:
                    calculationsMenu = false;
                    break;
            }
        }
    }

    // Handle data updates.
    void handleDataUpdates() {
        boolean updateMenu = true;

        while (updateMenu) {
            System.out.println("\n===== Update Data Menu =====");
            System.out.println("1. Update bet outcomes");
            System.out.println("2. Edit bet inputs");
            System.out.println("3. Update team stats");
            System.out.println("4. Update player stats");
            System.out.println("5. Return to main menu");
            System.out.println("============================");

            int choice = Validation.getValidIntegerInput("Enter your choice (1-4): ", 1, 5);

            switch (choice) {
                case 1:
                    dataManager.updateBetOutcome(scanner);
                    break;
                case 2:
                    dataManager.editBet();
                    break;
                case 3:
                    dataManager.updateTeamStats(scanner);
                    break;
                case 4:
                    dataManager.updatePlayerStats(scanner);
                    break;
                case 5:
                    updateMenu = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // View data menu.
    public void viewData() {
        boolean viewDataMenu = true;

        while (viewDataMenu) {
            System.out.println("\n What would you like to view:");
            System.out.println("1. View all Bets");
            System.out.println("2. View all Teams");
            System.out.println("3. View all Players");
            System.out.println("4. View profit/loss summary");
            System.out.println("5. View bets by team");
            System.out.println("6. View bets by date");
            System.out.println("7. Return to main menu");

            int viewDataMenuChoice = Validation.getValidIntegerInput("Enter your choice (1-7): ", 1,7);

            switch (viewDataMenuChoice) {
                case 1:
                    viewBets();
                    break;
                case 2:
                    viewTeams();
                    break;
                case 3:
                    viewPlayers();
                    break;
                case 4:
                    viewProfitLoss();
                    break;
                case 5:
                    viewBetsByTeam();
                    break;
                case 6:
                    viewBetsByDate();
                    break;
                case 7:
                    viewDataMenu = false;
                    break;
                default:
                    System.out.println("Enter a valid input (1-7).");
            }
        }
    }

    // View all bets.
    private void viewBets() {
        if (dataManager.getAllBets().isEmpty()) {
            System.out.println("No bets to display.");
            return;
        }

        System.out.println("\n===== All Bets =====");
        for (Bet bet : dataManager.getAllBets()) {
            System.out.println(bet.toString());
        }
    }

    // View all teams.
    private void viewTeams() {
        if (dataManager.getAllTeams().isEmpty()) {
            System.out.println("No teams to display.");
            return;
        }

        System.out.println("\n===== All Teams =====");
        for (Team team : dataManager.getAllTeams()) {
            System.out.println(team.toString());
        }
    }

    // View all players.
    private void viewPlayers() {
        if (dataManager.getAllPlayers().isEmpty()) {
            System.out.println("No players to display.");
            return;
        }

        System.out.println("\n===== All Players =====");
        for (Player player : dataManager.getAllPlayers()) {
            System.out.println(player.toString());
        }
    }


    // View profit/loss summary.
    private void viewProfitLoss() {
        if (dataManager.getAllBets().isEmpty()) {
            System.out.println("No bets to analyze.");
            return;
        }

        System.out.println("\n===== Profit/Loss Summary =====");

        int totalBets = dataManager.getAllBets().size();
        int winningBets = 0;
        int losingBets = 0;
        int pendingBets = 0;

        for (Bet bet : dataManager.getAllBets()) {
            if (bet.getOutcome() == BetOutcome.WIN) {
                winningBets++;
            } else if (bet.getOutcome() == BetOutcome.LOSS) {
                losingBets++;
            } else {
                pendingBets++;
            }
        }

        double totalProfit = dataManager.calculateTotalProfitLoss();

        System.out.println("Total bets: " + totalBets);
        System.out.println("Winning bets: " + winningBets);
        System.out.println("Losing bets: " + losingBets);
        System.out.println("Pending bets: " + pendingBets);
        System.out.println("Total profit/loss: $" + String.format("%.2f", totalProfit));

        // Print profit by bet type
        Map<BetType, Double> profitByType = dataManager.getProfitLossByBetType();

        System.out.println("\nProfit/Loss by Bet Type:");
        for (BetType type : BetType.values()) {
            double profit = profitByType.getOrDefault(type, 0.0);
            System.out.println(type.getDisplayName() + ": $" + String.format("%.2f", profit));
        }

        // Most profitable bet type
        BetType mostProfitable = dataManager.getMostProfitableBetType();
        if (mostProfitable != null) {
            System.out.println("\nMost profitable bet type: " + mostProfitable.getDisplayName());
        } else {
            System.out.println("\nNo profitable bet types found.");
        }
    }

    // View bets by team.
    private void viewBetsByTeam() {
        System.out.println("Enter team name to filter bets:");
        String teamName = scanner.nextLine().trim();

        System.out.println("\n===== Bets for " + teamName + " =====");
        for (Bet bet : dataManager.getBetsByTeam(teamName)) {
            System.out.println(bet.toString());
        }
    }

    // View bets by date.
    private void viewBetsByDate() {
        String date = Validation.getValidDate(scanner, "Enter date (YYYY-MM-DD) to filter bets: ");

        System.out.println("\n===== Bets for " + date + " =====");
        for (Bet bet : dataManager.getBetsByDate(date)) {
            System.out.println(bet.toString());
        }
    }


    // Handle file saving and loading.
    void handleFileSaving() {
        boolean fileMenu = true;

        while (fileMenu) {
            System.out.println("\n===== File Operations =====");
            System.out.println("1. Save data to files");
            System.out.println("2. Load data from files");
            System.out.println("3. Return to main menu");
            System.out.println("==========================");

            int choice = Validation.getValidIntegerInput("Enter your choice (1-3): ", 1, 3);

            switch (choice) {
                case 1:
                    try {
                        dataManager.saveToFiles();
                    } catch (IOException e) {
                        System.out.println("Error saving data: " + e.getMessage());
                    }
                    break;
                case 2:
                    try {
                        dataManager.loadFromFiles();
                    } catch (IOException e) {
                        System.out.println("Error loading data: " + e.getMessage());
                    }
                    break;
                case 3:
                    fileMenu = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
