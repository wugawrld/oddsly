package rw.data;

/**
 * CPSC 233 Project MainTest class ...
 * @author Jarod Rideout, Risha Vaghani, Sardar Waheed
 * @email jarod.rideout@ucalgary.ca, risha.vaghani@ucalgary.ca, sardar.waheed@ucalgary.ca
 * @tutorial Tutorial 1 Tuesdays 11:00 - 13:00
 * @version 3.0
 */

import rw.enums.BetOutcome;
import rw.enums.BetType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class MainTest {
    // Sources used during the development of testcases; https://www.youtube.com/watch?v=flpmSXVTqBI; https://stackoverflow.com/questions/6415728/junit-testing-with-simulated-user-input

    // create a new DataManager instance before each test to ensure we have a clean state
    private DataManager dataManager;
    @BeforeEach
    public void start() {
        dataManager = new DataManager();
    }
    // Source: https://junit.org/junit5/docs/5.0.2/api/org/junit/jupiter/api/BeforeEach.html

    // test 1: add a new bet and update its outcome (verification of payout and profit/loss)
    @Test
    public void testAddingBetAndUpdatingOutcome() {
        // here we add a new bet (outcome is pending)
        Bet bet = dataManager.addBet("2025-03-06", "Atlanta Hawks", "Boston Celtics", BetType.MONEYLINE, 50, 1.5, "NBA");
        // compare values
        assertNotNull(bet);
        assertEquals("2025-03-06", bet.getFormattedGameDate());
        assertEquals("Atlanta Hawks", bet.getTeam1());
        assertEquals("Boston Celtics", bet.getTeam2());
        assertEquals(BetType.MONEYLINE, bet.getBetType());
        assertEquals(50, bet.getAmountWagered(), 0.001);
        assertEquals(1.5, bet.getOdds(), 0.001);
        assertEquals(BetOutcome.PENDING, bet.getOutcome());

        // here we update the outcome to wn and verify the payout
        boolean update = dataManager.updateBetOutcome(bet.getId(),BetOutcome.WIN);
        assertTrue(update);
        Bet updatedBetOutcome = dataManager.getBetById(bet.getId());
        assertEquals(BetOutcome.WIN, updatedBetOutcome.getOutcome());
        double determinedPayout = 50 * 1.5;
        assertEquals(determinedPayout, updatedBetOutcome.getPayout(), 0.001);
        double determinedProfit = determinedPayout - 50;
        assertEquals(determinedProfit, updatedBetOutcome.getProfitLoss(), 0.001);
        assertTrue(updatedBetOutcome.isProfitable());
    }

    // test 2: retrieve bets by team (filtered by team)
    @Test
    public void testRetrieveBetByTeam() {
        // create three bets (two involve same team - "C")
        Bet bet1 = dataManager.addBet("2025-04-07", "Team C", "Team B", BetType.OVER_UNDER, 100, 2.0, "NBA");
        Bet bet2 = dataManager.addBet("2025-04-08", "Team C", "Team D", BetType.MONEYLINE, 75, 1.8, "NBA");
        Bet bet3 = dataManager.addBet("2025-04-09", "Team D", "Team A", BetType.POINT_SPREAD, 50, 1.5, "NBA");


        // compare values
        List<Bet> teamABets = dataManager.getBetsByTeam("Team C");
        assertNotNull(teamABets);
        assertEquals(2, teamABets.size());
        assertTrue(teamABets.contains(bet1));
        assertTrue(teamABets.contains(bet2));
    }

    // test 3: retrieve bets by date (game date)
    @Test
    public void testRetrieveByGameDate() {
        Bet bet1 = dataManager.addBet("2025-07-06", "Team C", "Team D", BetType.OVER_UNDER, 100, 2.0, "NBA");
        Bet bet2 = dataManager.addBet("2025-05-15", "Team D", "Team F", BetType.POINT_SPREAD, 80, 1.9, "NBA");
        Bet bet3 = dataManager.addBet("2025-07-06", "Team E", "Team F", BetType.MONEYLINE, 60, 1.7, "NBA");

        List<Bet> betsJuly06 = dataManager.getBetsByDate("2025-07-06");
        assertNotNull(betsJuly06);
        assertEquals(2, betsJuly06.size());
        for (Bet bet : betsJuly06) {
            assertEquals("2025-07-06", bet.getFormattedGameDate());
    }

}
    // test 4: test converting BasketBallPlayer object to csv; parse and recreate from CSV (verification of consistency)
    @Test
    public void testObjectBasketBallPlayerCSV() {
        // player object
        BasketballPlayer originalPlayerAsset = new BasketballPlayer("LeThatsMyFavouriteSaying James", "Los Angeles Lakers", "Forward", 28.8, 8.2, 8.7);
        // CSV asset
        String CSV = originalPlayerAsset.toCsv();
        BasketballPlayer csvPlayerAsset = BasketballPlayer.fromCsv(CSV);

        // verification of CSV fields
        assertEquals(originalPlayerAsset.getPlayerName(), csvPlayerAsset.getPlayerName());
        assertEquals(originalPlayerAsset.getTeamName(), csvPlayerAsset.getTeamName());
        assertEquals(originalPlayerAsset.getPosition(), csvPlayerAsset.getPosition());
        assertEquals(originalPlayerAsset.getPointsPerGame(), csvPlayerAsset.getPointsPerGame(), 0.001);
        assertEquals(originalPlayerAsset.getReboundsPerGame(), csvPlayerAsset.getReboundsPerGame(), 0.001);
        assertEquals(originalPlayerAsset.getAssistsPerGame(), csvPlayerAsset.getAssistsPerGame(), 0.001);
    }

    // test 5: test converting Bet object to csv; parse and recreate from CSV (verification of consistency)
    @Test
    public void testObjectBetCSV() {
        // bet object
        Bet originalBetAsset = new Bet("bet1", "2025-06-11", "Team C", "Team D",
                BetType.OVER_UNDER, 100, 1.5, BetOutcome.WIN, "NBA");
        // CSV asset
        String CSV = originalBetAsset.toCsv();
        Bet csvBetAsset = Bet.fromCsv(CSV);

        // verification of CSV fields
        assertEquals(originalBetAsset.getId(), csvBetAsset.getId());
        assertEquals(originalBetAsset.getFormattedGameDate(), csvBetAsset.getFormattedGameDate());
        assertEquals(originalBetAsset.getTeam1(), csvBetAsset.getTeam1());
        assertEquals(originalBetAsset.getTeam2(), csvBetAsset.getTeam2());
        assertEquals(originalBetAsset.getBetType(), csvBetAsset.getBetType());
        assertEquals(originalBetAsset.getAmountWagered(), csvBetAsset.getAmountWagered(), 0.001);
        assertEquals(originalBetAsset.getOdds(), csvBetAsset.getOdds(), 0.001);
        assertEquals(originalBetAsset.getOutcome(), csvBetAsset.getOutcome());
        assertEquals(originalBetAsset.getLeague(), csvBetAsset.getLeague());
    }

    // test 6: determine profit/loss + most profitable by bet type
    @Test
    public void testDetermineProfitLossCalculation() {
        // add losing bet
        Bet bet1 = dataManager.addBet("2025-03-11", "Team A", "Team E", BetType.OVER_UNDER, 50, 1.5, "NBA");
        dataManager.updateBetOutcome(bet1.getId(), BetOutcome.LOSS);
        // add winning bet
        Bet bet2 = dataManager.addBet("2025-04-11", "Team B", "Team A", BetType.OVER_UNDER, 100, 2.0, "NBA");
        dataManager.updateBetOutcome(bet2.getId(), BetOutcome.WIN);

        // verify win profit and loss
        Map<BetType, Double> profitByType = dataManager.getProfitLossByBetType();
        assertTrue(profitByType.containsKey(BetType.OVER_UNDER));
        double expectedProfit = 100 - 50;
        assertEquals(expectedProfit, profitByType.get(BetType.OVER_UNDER), 0.001);

        BetType mostProfitable = dataManager.getMostProfitableBetType();
        assertEquals(BetType.OVER_UNDER, mostProfitable);
    }

    // test 7: team statistics and verification for updated values
    @Test
    public void testStatsUpdateForTeam() {
        Team teamAsset = dataManager.addTeam("Team C", "West");
        // initialize all stats (at 0)
        assertEquals(0, teamAsset.getWins());
        assertEquals(0, teamAsset.getLosses());
        assertEquals(0, teamAsset.getPointsScored());
        assertEquals(0, teamAsset.getPointsAllowed());

        // update team stats
        boolean updateTeamStats = dataManager.updateTeamStats("Team C", 15, 3, 15000, 1300);
        assertTrue(updateTeamStats);

        Team updatedTeamStats = dataManager.getTeamByName("Team C");
        assertNotNull(updatedTeamStats);
        assertEquals(15, updatedTeamStats.getWins());
        assertEquals(3, updatedTeamStats.getLosses());
        assertEquals(15000, updatedTeamStats.getPointsScored());
        assertEquals(1300, updatedTeamStats.getPointsAllowed());
        assertEquals(15000 - 1300, updatedTeamStats.getPointsDifferential());
    }

    // test 8: create basketball player and update player stats
    @Test
    public void testCreateBasketballPlayerAndUpdateStats() {
        // create player asset
        BasketballPlayer playerAsset = dataManager.addBasketballPlayer("LeThatsMyFavouriteSaying James",
                "Los Angeles Lakers", "Forward", 28.7, 8.2, 8.8);
        assertNotNull(playerAsset);

        // verify entered fields
        assertEquals("LeThatsMyFavouriteSaying James", playerAsset.getPlayerName());
        assertEquals("Los Angeles Lakers", playerAsset.getTeamName());
        assertEquals("Forward", playerAsset.getPosition());
        assertEquals(8.8, playerAsset.getAssistsPerGame(), 0.001);

        // update stats
        boolean updatedPlayerAsset = dataManager.updateBasketballPlayerStats("LeThatsMyFavouriteSaying James", "Los Angeles Lakers",
                31.2, 11.4, 15.4);

        // verify updated fields
        assertTrue(updatedPlayerAsset);
        BasketballPlayer updatedPlayer = (BasketballPlayer) dataManager.getPlayerByNameAndTeam("LeThatsMyFavouriteSaying James", "Los Angeles Lakers");
        assertNotNull(updatedPlayer);
        assertEquals(31.2, updatedPlayer.getPointsPerGame(), 0.001);
        assertEquals(11.4, updatedPlayer.getReboundsPerGame(), 0.001);
        assertEquals(15.4, updatedPlayer.getAssistsPerGame(), 0.001);
    }

    // test 9: create hockey player and update player stats
    @Test
    public void testCreateHockeyPlayerAndUpdateStats() {
        // create player asset
        HockeyPlayer playerAsset = dataManager.addHockeyPlayer("Connor McDavid",
                "Edmonton Oilers", "Center", 0.42, 1.02);
        assertNotNull(playerAsset);

        // verify entered fields
        assertEquals("Connor McDavid", playerAsset.getPlayerName());
        assertEquals("Edmonton Oilers", playerAsset.getTeamName());
        assertEquals("Center", playerAsset.getPosition());
        assertEquals(1.02, playerAsset.getAssistsPerGame(), 0.001);

        // update stats
        boolean updatedPlayerAsset = dataManager.updateHockeyPlayerStats("Connor McDavid", "Edmonton Oilers",
                0.5, 0.99);

        // verify updated fields
        assertTrue(updatedPlayerAsset);
        HockeyPlayer updatedPlayer = (HockeyPlayer) dataManager.getPlayerByNameAndTeam("Connor McDavid", "Edmonton Oilers");
        assertNotNull(updatedPlayer);
        assertEquals(0.5, updatedPlayer.getPointsPerGame(), 0.001);
        assertEquals(0.99, updatedPlayer.getAssistsPerGame(), 0.001);
    }

    // test 10: Adding Bets then saving to CSV file and loading from CSV file

    @Test
    public void betAddingSavingLoadingFromCSV() throws IOException {
        // Initial bet1 is created and saved to a file.
        Bet bet1 = dataManager.addBet("2025-07-06", "Team C", "Team D", BetType.OVER_UNDER, 100, 2.0, "NBA");
        dataManager.saveToFiles();
        // Two additional bets are created and saved to the save file (should be appended after the original saved bet)
        Bet bet2 = dataManager.addBet("2025-05-15", "Team D", "Team F", BetType.POINT_SPREAD, 80, 1.9, "NBA");
        Bet bet3 = dataManager.addBet("2025-07-06", "Team E", "Team F", BetType.MONEYLINE, 60, 1.7, "NBA");
        dataManager.saveToFiles();
        // bets are all loaded back from the file
        dataManager.loadFromFiles();

        // ensure bet1 is not overwritten and bet2 and bet3 are successfully loaded with proper ID updates.
        assertEquals("bet1", dataManager.getBetById("bet1").getId());
        assertEquals("bet2", dataManager.getBetById("bet2").getId());
        assertEquals("bet3", dataManager.getBetById("bet3").getId());
    }
}

