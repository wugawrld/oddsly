package rw.shell;

/**
 * CPSC 233 Project Main class ...
 * @author Jarod Rideout, Risha Vaghani, Sardar Waheed
 * @email jarod.rideout@ucalgary.ca, risha.vaghani@ucalgary.ca, sardar.waheed@ucalgary.ca
 * @tutorial Tutorial 1 Tuesdays 11:00 - 13:00
 * @version 3.0
 * This class meant to be the main interface for a sports betting management system. It allows users to input and
 * store data related to their bets, teams and players. The program collects information such as bet type, league,
 * teams and player stats, validates the entries and stores them for future reference. Users can also view their
 * inputs and make multiple entries for each category. The system provides a basic view for tracking bets, teams,
 * and players within popular sports leagues.
 */

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.*;
import rw.enums.*;
import rw.data.*;
import java.io.IOException;

// Main class prompts user to input data regarding their sports bets, teams and players through the shell.
public class Main {

    // Final variable list of valid leagues for our betting tracker.
    public static final String[] leagueList = {"NBA", "NHL"};

    // Final variable list of valid NHL team names.
    public static final String[] nhlTeams = {"A", "B",
            "Anaheim Ducks", "Arizona Coyotes", "Boston Bruins", "Buffalo Sabres",
            "Calgary Flames", "Carolina Hurricanes", "Chicago Blackhawks", "Colorado Avalanche",
            "Columbus Blue Jackets", "Dallas Stars", "Detroit Red Wings", "Edmonton Oilers",
            "Florida Panthers", "Los Angeles Kings", "Minnesota Wild", "Montreal Canadiens",
            "Nashville Predators", "New Jersey Devils", "New York Islanders", "New York Rangers",
            "Ottawa Senators", "Philadelphia Flyers", "Pittsburgh Penguins", "San Jose Sharks",
            "Seattle Kraken", "St. Louis Blues", "Tampa Bay Lightning", "Toronto Maple Leafs",
            "Vancouver Canucks", "Vegas Golden Knights", "Washington Capitals", "Winnipeg Jets"};


    // Final variable list of valid NBA team names.
    public static final String[] nbaTeams = {
            "Atlanta Hawks", "Boston Celtics", "Brooklyn Nets", "Charlotte Hornets",
            "Chicago Bulls", "Cleveland Cavaliers", "Dallas Mavericks", "Denver Nuggets",
            "Detroit Pistons", "Golden State Warriors", "Houston Rockets", "Indiana Pacers",
            "Los Angeles Clippers", "Los Angeles Lakers", "Memphis Grizzlies", "Miami Heat",
            "Milwaukee Bucks", "Minnesota Timberwolves", "New Orleans Pelicans", "New York Knicks",
            "Oklahoma City Thunder", "Orlando Magic", "Philadelphia 76ers", "Phoenix Suns",
            "Portland Trail Blazers", "Sacramento Kings", "San Antonio Spurs", "Toronto Raptors",
            "Utah Jazz", "Washington Wizards"};


    // The main method creates data manager instance, initializes it and starts the menu system.
    public static void main(String[] args) {

        // Create instances of required classes
        DataManager dataManager = new DataManager();
        Menu menu = new Menu(dataManager);
        UserInput userInput = new UserInput(dataManager);

        // Handle command line arguments for data file loading
        if (args.length > 0) {
            try {
                // use the first argument as a data file path
                System.out.println("Loading data from command line argument...");
                dataManager.loadFromFiles();
                System.out.println("Data loaded successfully.");
            } catch (IOException e) {
                System.out.println("Error loading data from file: " + e.getMessage());
                System.out.println("Starting with empty data.");
            }
        }
        // Start Main Menu
        menu.startMenu();
    }


    // Display overall profit/loss
    public void displayOverallProfitLoss(DataManager dataManager) {
        System.out.println("\n===== Overall Profit/Loss =====");

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

        // Calculate Return on Investment.
        double totalInvestment = 0;
        for (Bet bet : dataManager.getAllBets()) {
            if (bet.getOutcome() != BetOutcome.PENDING) {
                totalInvestment += bet.getAmountWagered();
            }
        }

        if (totalInvestment > 0) {
            double roi = (totalProfit / totalInvestment) * 100;
            System.out.println("ROI (Return on Investment): " + String.format("%.2f", roi) + "%");
        }
    }


    // Display profit by bet type
    public void displayProfitByBetType(DataManager dataManager) {
        System.out.println("\n Profit/Loss by Bet Type");

        Map<BetType, Double> profitByType = dataManager.getProfitLossByBetType();
        Map<BetType, Integer> countByType = new HashMap<>();

        // count bets by type
        for (Bet bet : dataManager.getAllBets()) {
            if (bet.getOutcome() != BetOutcome.PENDING) {
                BetType type = bet.getBetType();
                countByType.put(type, countByType.getOrDefault(type, 0) + 1);
            }
        }

        // Display results
        for (BetType type : BetType.values()) {
            double profit = profitByType.getOrDefault(type, 0.0);
            int count = countByType.getOrDefault(type, 0);

            System.out.println(type.getDisplayName() + ":");
            System.out.println("  Total bets: " + count);
            System.out.println("  Profit/Loss: $" + String.format("%.2f", profit));

            if (count > 0) {
                System.out.println("  Average profit per bet: $" + String.format("%.2f", profit / count));
            }
            System.out.println();
        }
    }

    // Display most profitable bet type analysis.
    public void displayMostProfitableBetType(DataManager dataManager) {
        System.out.println("\n Most Profitable Bet Type Analysis ");

        BetType mostProfitable = dataManager.getMostProfitableBetType();

        if (mostProfitable != null) {
            Map<BetType, Double> profitByType = dataManager.getProfitLossByBetType();
            double profit = profitByType.getOrDefault(mostProfitable, 0.0);

            System.out.println("Most profitable bet type: " + mostProfitable.getDisplayName());
            System.out.println("Total profit: $" + String.format("%.2f", profit));

            // calculate success rate
            int wins = 0;
            int total = 0;

            for (Bet bet : dataManager.getAllBets()) {
                if (bet.getBetType() == mostProfitable && bet.getOutcome() != BetOutcome.PENDING) {
                    total++;
                    if (bet.getOutcome() == BetOutcome.WIN) {
                        wins++;
                    }
                }
            }

            if (total > 0) {
                double successRate = ((double) wins / total) * 100;
                System.out.println("Success rate: " + String.format("%.2f", successRate) + "%");
                System.out.println("Total " + mostProfitable.getDisplayName() + " bets: " + total);
                System.out.println("Winning bets: " + wins);
            }
        } else {
            System.out.println("No profitable bet types found or no completed bets.");
        }
    }


    // Display team performance analysis
    public void displayTeamPerformance(DataManager dataManager, Scanner scanner) {
        System.out.println("Enter team name to analyze:");
        String teamName = scanner.nextLine().trim();

        System.out.println("\n Team Performance Analysis: " + teamName + " ");

        List<Bet> teamBets = dataManager.getBetsByTeam(teamName);

        if (teamBets.isEmpty()) {
            System.out.println("No bets found for this team.");
            return;
        }

        int wins = 0;
        int losses = 0;
        int pending = 0;
        double totalProfit = 0;

        for (Bet bet : teamBets) {
            if (bet.getOutcome() == BetOutcome.WIN) {
                wins++;
                totalProfit += bet.getPayout() - bet.getAmountWagered();
            } else if (bet.getOutcome() == BetOutcome.LOSS) {
                losses++;
                totalProfit -= bet.getAmountWagered();
            } else {
                pending++;
            }
        }

        System.out.println("Total bets: " + teamBets.size());
        System.out.println("Winning bets: " + wins);
        System.out.println("Losing bets: " + losses);
        System.out.println("Pending bets: " + pending);
        System.out.println("Total profit/loss: $" + String.format("%.2f", totalProfit));

        if (wins + losses > 0) {
            double successRate = ((double) wins / (wins + losses)) * 100;
            System.out.println("Success rate: " + String.format("%.2f", successRate) + "%");
        }

        // Get team statistics if available
        Team team = dataManager.getTeamByName(teamName);
        if (team != null) {
            System.out.println("\nTeam Statistics:");
            System.out.println("Wins: " + team.getWins());
            System.out.println("Losses: " + team.getLosses());

            if (team.getWins() + team.getLosses() > 0) {
                double winPercentage = ((double) team.getWins() / (team.getWins() + team.getLosses())) * 100;
                System.out.println("Win percentage: " + String.format("%.2f", winPercentage) + "%");
            }

            System.out.println("Points scored: " + team.getPointsScored());
            System.out.println("Points allowed: " + team.getPointsAllowed());
            System.out.println("Point differential: " + team.getPointsDifferential());
        }
    }
}

/*
To Do List Moving Forward:
- Add unique elements to each betType including who the user thinks will win, how many points
  will be scored in the game etc.

- Add DisplayData class?

- Use JavaFX to build an interactive interface for a user (no longer should use command line arguments)
 */