package rw.app;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.web.WebView;
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

    @FXML
    private WebView standingsWebView;

    @FXML
    private RadioButton viewBetButton;

    @FXML
    private RadioButton viewPlayersButton;

    @FXML
    private RadioButton viewTeamsButton;

    @FXML
    private RadioButton deleteDataToggle;

    @FXML
    private RadioButton editDataToggle;


    @Override
    public void setSceneManager(SceneManager manager) {
        this.sceneManager = manager;
    }

    @Override
    public void initialize() {
        ToggleGroup viewGroup = new ToggleGroup();
        viewBetButton.setToggleGroup(viewGroup);
        viewTeamsButton.setToggleGroup(viewGroup);
        viewPlayersButton.setToggleGroup(viewGroup);

        ToggleGroup changeGroup = new ToggleGroup();
        deleteDataToggle.setToggleGroup(changeGroup);
        editDataToggle.setToggleGroup(changeGroup);
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
    void quit(ActionEvent event) {
        Platform.exit();
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
