package rw.data;

import rw.enums.*;
import rw.shell.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/**
 * CPSC 233 Project Validation class ...
 * @author Jarod Rideout, Risha Vaghani, Sardar Waheed
 * @email jarod.rideout@ucalgary.ca, risha.vaghani@ucalgary.ca, sardar.waheed@ucalgary.ca
 * @tutorial Tutorial 1 Tuesdays 11:00 - 13:00
 * @version 3.0

 * This class provides validation methods for user input.
 */

public class Validation {

    // Validates and gets an integer input within a specified range.
    public static int getValidIntegerInput(String prompt, int min, int max) {
        Scanner scanner = new Scanner(System.in);
        int value = 0;
        boolean validInput = false;

        while (!validInput) {
            System.out.print(prompt);
            try {
                value = Integer.parseInt(scanner.nextLine().trim());
                if (value >= min && value <= max) {
                    validInput = true;
                } else {
                    System.out.println("Please enter a number between " + min + " and " + max);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
        return value;
    }

    // This method will validate and get a double value from the user.
    public static double getValidDouble(Scanner scanner, String prompt) {
        double value = 0.0;
        boolean validInput = false;

        // It will loop until user enters a valid double value.
        while (!validInput) {
            System.out.println(prompt);
            try {
                value = Double.parseDouble(scanner.nextLine().trim());
                if (value >= 0) {
                    validInput = true;
                } else {
                    System.out.println("Please enter a positive number.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
        return value;
    }

    // CheckLeague tries to match inputted league against a permanent set of leagues.
    public static String checkLeague(Scanner scanner, String prompt) {
        // Prompts user for input.
        System.out.println(prompt);
        String input = scanner.nextLine().trim();

        // Checks if input is a part of leagueList.
        for (String league : Main.leagueList) {
            if (league.equalsIgnoreCase(input)) {
                return league;
            }
        }
        System.out.println(input + " league not found. Please enter a valid league.");
        return checkLeague(scanner, prompt);
    }

    // CheckTeam tries to match inputted team name against a permanent set of teams of leagues NBA and NHL.
    public static String checkTeam(Scanner scanner, String prompt, String league) {
        // Prompts user for input.
        System.out.println(prompt);
        String input = scanner.nextLine().trim();

        // Checks if league to check is NBA.
        if (league.equalsIgnoreCase("NBA")) {
            // Checks if any team in NBA matches input.
            for (String team : Main.nbaTeams) {
                if (team.equalsIgnoreCase(input)) {
                    return team;
                }
            }
        }
        // Checks if league to check is NHL.
        else if (league.equalsIgnoreCase("NHL")) {
            // Checks if any team in NHL matches input.
            for (String team : Main.nhlTeams) {
                if (team.equalsIgnoreCase(input)) {
                    return team;
                }
            }
        }
        // If no match is found, message stating to retry is given and checkTeam is run again.
        System.out.println("Team not in " + league + ". Enter correct team name.");
        return checkTeam(scanner, prompt, league);
    }

    // Validates date format and ensures the date is in the future.
    public static String getValidDate(Scanner scanner, String prompt) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        Date currentDate;
        try {
            currentDate = dateFormat.parse("2025-03-19"); // Just an example date
        } catch (ParseException e) {
            System.out.println("Error with current date");
            return null;
        }

        while (true) {
            System.out.println(prompt);
            String input = scanner.nextLine();

            try {
                Date date = dateFormat.parse(input);
                if (date.after(currentDate)) {
                    return dateFormat.format(date);
                } else {
                    System.out.println("Please enter a future date.");
                }

            } catch (ParseException e) {
                System.out.println("Enter a valid date in the format YYYY-MM-DD.");
            }
        }
    }

    // Validates that a bet outcome selection is valid and returns the corresponding BetOutcome.
    public static BetOutcome getValidBetOutcome(Scanner scanner, String prompt) {
        System.out.println(prompt);
        System.out.println("1. Win");
        System.out.println("2. Loss");
        System.out.println("3. Pending");

        int choice = getValidIntegerInput("Enter your choice (1-3): ", 1, 3);

        switch (choice) {
            case 1:
                return BetOutcome.WIN;
            case 2:
                return BetOutcome.LOSS;
            default:
                return BetOutcome.PENDING;
        }
    }


    // Validates a date range for filtering bets in calculations.
    public static String[] getValidDateRange(Scanner scanner) {
        String startDate = getValidDate(scanner, "Enter start date (YYYY-MM-DD): ");
        String endDate = getValidDate(scanner, "Enter end date (YYYY-MM-DD): ");

        return new String[] { startDate, endDate };
    }

     // Validates a minimum profit threshold for calculations.
    public static double getValidProfitThreshold(Scanner scanner) {
        return getValidDouble(scanner, "Enter minimum profit threshold: $");
    }

    //  Validates selection of betting analysis type.
    public static int getValidAnalysisType(Scanner scanner) {
        System.out.println("Choose analysis type:");
        System.out.println("1. Overall profit/loss");
        System.out.println("2. Profit/loss by bet type");
        System.out.println("3. Profit/loss by team");
        System.out.println("4. Profit/loss by date range");

        return getValidIntegerInput("Enter your choice (1-4): ", 1, 4);
    }
}
