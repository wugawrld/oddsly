package rw.app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import rw.data.Bet;
import rw.data.Player;
import rw.data.Team;

import java.util.ArrayList;
import java.util.List;

public class MainController implements SceneController {

    private static List<Bet> bets = new ArrayList<>();
    private static List<Player> players = new ArrayList<>();
    private static List<Team> teams = new ArrayList<>();


    public static void addNewBet(Bet bet) {
        bets.add(bet);
    }

    public static void addNewPlayer(Player player) {
        players.add(player);
    }

    public static void addNewTeam(Team team) {
        teams.add(team);
    }

    private SceneManager sceneManager;

    @Override
    public void setSceneManager(SceneManager manager) {
        this.sceneManager = manager;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void onSceneDisplayed() {

    }

    @FXML
    void addBet(ActionEvent event) {
        sceneManager.switchToScene("AddBet");
    }

    @FXML
    void addPlayer(ActionEvent event) {
        sceneManager.switchToScene("AddPlayer");
    }

    @FXML
    void addTeam(ActionEvent event) {
        sceneManager.switchToScene("AddTeam");
    }

    @FXML
    private Font x1;

    @FXML
    private Color x2;

    @FXML
    private Font x3;

    @FXML
    private Color x4;

}
