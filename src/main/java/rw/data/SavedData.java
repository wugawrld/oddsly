package rw.data;

import java.io.Serializable;
import java.util.List;

/**
 * CPSC 233 Project SavedData class ...
 * @author Jarod Rideout, Risha Vaghani, Sardar Waheed
 * @email jarod.rideout@ucalgary.ca, risha.vaghani@ucalgary.ca, sardar.waheed@ucalgary.ca
 * @tutorial Tutorial 1 Tuesdays 11:00 - 13:00
 * @version 3.0
 *
 * Container class for serializing all application data.
 * This class holds lists of bets, players, and teams for saving and loading.
 */
public class SavedData implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Bet> bets;
    private List<Player> players;
    private List<Team> teams;

    public SavedData(List<Bet> bets, List<Player> players, List<Team> teams) {
        this.bets = bets;
        this.players = players;
        this.teams = teams;
    }

    public List<Bet> getBets() {
        return bets;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<Team> getTeams() {
        return teams;
    }
}