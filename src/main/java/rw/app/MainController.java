package rw.app;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import rw.data.Bet;
import rw.data.Player;
import rw.data.SavedData;
import rw.data.Team;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import java.util.Optional;
import rw.data.BasketballPlayer;
import rw.data.HockeyPlayer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MainController implements SceneController {

    private static List<Bet> bets = new ArrayList<>();
    private static List<Player> players = new ArrayList<>();
    private static List<Team> teams = new ArrayList<>();

    public static void addNewBet(Bet bet) {
        bets.add(bet);
        System.out.println("Bet added to list: " + bet.toString() + ", Total bets: " + bets.size());
    }

    public static void addNewPlayer(Player player) {
        players.add(player);
        System.out.println("Player added to list: " + player.toString() + ", Total players: " + players.size());
    }

    public static void addNewTeam(Team team) {
        teams.add(team);
        System.out.println("Team added to list: " + team.toString() + ", Total teams: " + teams.size());
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

    @FXML
    private Font x1;

    @FXML
    private Color x2;

    @FXML
    private Font x3;

    @FXML
    private Color x4;

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

        // Add listeners for view radio buttons
        viewBetButton.setOnAction(event -> viewBets());
        viewTeamsButton.setOnAction(event -> viewTeams());
        viewPlayersButton.setOnAction(event -> viewPlayers());
    }

    @Override
    public void onSceneDisplayed() {
        System.out.println("Main scene displayed. Checking for data to display...");
        System.out.println("Bets: " + bets.size() + ", Teams: " + teams.size() + ", Players: " + players.size());

        // Priority is to show bets if they exist (especially after adding a new bet)
        if (!bets.isEmpty()) {
            viewBetButton.setSelected(true);
            viewBets();
            System.out.println("Showing bets view automatically");
        }
        // if no bets but teams exist, show teams
        else if (!teams.isEmpty()) {
            viewTeamsButton.setSelected(true);
            viewTeams();
        }
        // if no bets or teams but players exist, show players
        else if (!players.isEmpty()) {
            viewPlayersButton.setSelected(true);
            viewPlayers();
        }
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
    void saveData(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Bet Tracking Data");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Bet Tracker Files", "*.btd")
        );

        // Get the stage from the scene
        Stage stage = (Stage) viewBetButton.getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try (ObjectOutputStream out = new ObjectOutputStream(
                    new FileOutputStream(file))) {
                // Create a wrapper object to hold all data
                rw.data.SavedData savedData = new rw.data.SavedData(bets, players, teams);
                out.writeObject(savedData);

                // Show confirmation
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Save Successful");
                alert.setHeaderText(null);
                alert.setContentText("Betting data saved successfully!");
                alert.showAndWait();

            } catch (IOException ex) {
                showErrorAlert("Save Error", "Could not save data", ex);
            }
        }
    }

    @FXML
    void loadData(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Bet Tracking Data");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Bet Tracker Files", "*.btd")
        );

        // Get the stage from the scene
        Stage stage = (Stage) viewBetButton.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            try (ObjectInputStream in = new ObjectInputStream(
                    new FileInputStream(file))) {
                // Read the saved data wrapper object
                SavedData savedData = (SavedData) in.readObject();

                // update all data collections
                bets = savedData.getBets();
                players = savedData.getPlayers();
                teams = savedData.getTeams();

                // Show confirmation
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Load Successful");
                alert.setContentText("Data loaded successfully!");

                String contentText = String.format(
                        "Loaded: %d bets, %d players, and %d teams.\n\n" +
                                "What would you like to view?",
                        bets.size(), players.size(), teams.size()
                );
                alert.setContentText(contentText);

                ButtonType viewBetsButton = new ButtonType("View Bets");
                ButtonType viewPlayersButton = new ButtonType("View Players");
                ButtonType viewTeamsButton = new ButtonType("View Teams");
                ButtonType closeButton = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);

                alert.getButtonTypes().setAll(viewBetsButton, viewPlayersButton, viewTeamsButton, closeButton);

                Optional<ButtonType> result = alert.showAndWait();

                if (result.get() == viewBetsButton) {
                    viewBets();
                    this.viewBetButton.setSelected(true);
                } else if (result.get() == viewPlayersButton) {
                    viewPlayers();
                    this.viewPlayersButton.setSelected(true);
                } else if (result.get() == viewTeamsButton) {
                    viewTeams();
                    this.viewTeamsButton.setSelected(true);
                }

            } catch (IOException | ClassNotFoundException ex) {
                showErrorAlert("Load Error", "Could not load data", ex);
            }
        }
    }

    private void showErrorAlert(String title, String header, Exception ex) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText("Error details: " + ex.getMessage());
        alert.showAndWait();
    }

    @FXML
    void quit(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void viewBets() {
    }

    @FXML
    void viewPlayers() {
        }
    }

    @FXML
    void viewTeams() {
    }