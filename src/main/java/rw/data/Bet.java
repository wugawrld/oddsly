package rw.data;

import rw.enums.BetOutcome;
import rw.enums.BetType;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
/**
 * CPSC 233 Project Bet class ...
 * @author Jarod Rideout, Risha Vaghani, Sardar Waheed
 * @email jarod.rideout@ucalgary.ca, risha.vaghani@ucalgary.ca, sardar.waheed@ucalgary.ca
 * @tutorial Tutorial 1 Tuesdays 11:00 - 13:00
 * @version 3.0
 * This class represents a sports bet with all relevant information.
  */

public class Bet implements Comparable<Bet>, Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private Date gameDate;
    private String team1;
    private String team2;
    private BetType betType;
    private double amountWagered;
    private double odds;
    private BetOutcome outcome;
    private double payout;
    private String league;


    // Constructor for a new bet.
    public Bet(String id, String gameDate, String team1, String team2, BetType betType,
               double amountWagered, double odds, BetOutcome outcome, String league) {
        this.id = id;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            this.gameDate = dateFormat.parse(gameDate);
        } catch (ParseException e) {
            this.gameDate = new Date(); // Default to current date if parsing fails
        }
        this.team1 = team1;
        this.team2 = team2;
        this.betType = betType;
        this.amountWagered = amountWagered;
        this.odds = odds;
        this.outcome = outcome;
        this.league = league;
        this.payout = calculatePayout();
    }

    // Calculate the payout based on the outcome and odds.
    public double calculatePayout() {
        if (outcome == BetOutcome.WIN) {
            return amountWagered * odds;
        } else {
            return 0;
        }
    }

    // Update the bet outcome and recalculate the payout.
    public void updateOutcome(BetOutcome outcome) {
        this.outcome = outcome;
        this.payout = calculatePayout();
    }

    // Update odds and recalculate payout
    public void updateOdds(double odds) {
        this.odds = odds;
        this.payout = calculatePayout();
    }

    // All the Getters
    public String getId() {
        return id;
    }

    public Date getGameDate() {
        return gameDate;
    }

    public String getFormattedGameDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(gameDate);
    }

    public String getTeam1() {
        return team1;
    }

    public String getTeam2() {
        return team2;
    }

    public BetType getBetType() {
        return betType;
    }

    public double getAmountWagered() {
        return amountWagered;
    }

    public double getOdds() {
        return odds;
    }

    public BetOutcome getOutcome() {
        return outcome;
    }

    public double getPayout() {
        return payout;
    }

    public String getLeague() {
        return league;
    }

    // Bet Setters
    public void setId(String newID) {id = newID;}

    public void setFormattedGameDate(String newDate) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            gameDate = dateFormat.parse(newDate);
        } catch (ParseException e) {
            System.err.println("Invalid date format. Please use yyyy-MM-dd.");
        }
    }

    public void setTeam1(String newTeam1) {team1 = newTeam1;}

    public void setTeam2(String newTeam2) {team2 = newTeam2;}

    public void setBetType(BetType newBetType) {betType = newBetType;}

    public void setAmountWagered(double newAmountWagered) {amountWagered = newAmountWagered;}

    public void setLeague(String newLeague) {league = newLeague;}

    //  Determines whether a bet is profitable (has positive return).
    public boolean isProfitable() {
        return payout > amountWagered;
    }

    // Calculates the profit/loss amount.
    public double getProfitLoss() {
        if (outcome == BetOutcome.WIN) {
            return payout - amountWagered;
        } else if (outcome == BetOutcome.LOSS) {
            return -amountWagered;
        } else {
            return 0; // Pending
        }
    }

    // Implements Comparable to sort bets by date (most recent first).
    @Override
    public int compareTo(Bet other) {
        return other.gameDate.compareTo(this.gameDate); // Descending order
    }


     // Overrides equals to consider two bets equal if they have the same ID.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bet bet = (Bet) o;
        return Objects.equals(id, bet.id);
    }


     // Overrides hashCode to be consistent with equals.
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


     // Returns a string representation of this bet.
    @Override
    public String toString() {
        return String.format("Bet #%s: %s vs %s (%s) - %s, Amount: $%.2f, Odds: %.2f, Outcome: %s, Payout: $%.2f",
                id, team1, team2, league, betType.getDisplayName(), amountWagered, odds,
                outcome.getDisplayName(), payout);
    }


     // Converts the bet to a CSV string for file storage.
    public String toCsv() {
        return String.format("%s,%s,%s,%s,%s,%.2f,%.2f,%s,%.2f,%s",
                id, getFormattedGameDate(), team1, team2, betType.getDisplayName(),
                amountWagered, odds, outcome.getDisplayName(), payout, league);
    }



    // Create a Bet object from a CSV line.
    public static Bet fromCsv(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length < 10) {
            throw new IllegalArgumentException("Invalid CSV format for Bet");
        }

        String id = parts[0];
        String gameDate = parts[1];
        String team1 = parts[2];
        String team2 = parts[3];
        BetType betType = BetType.fromDisplayName(parts[4]);
        double amountWagered = Double.parseDouble(parts[5]);
        double odds = Double.parseDouble(parts[6]);
        BetOutcome outcome = BetOutcome.fromDisplayName(parts[7]);
        String league = parts[9];

        return new Bet(id, gameDate, team1, team2, betType, amountWagered, odds, outcome, league);
    }

}
