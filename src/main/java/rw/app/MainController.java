package rw.app;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import rw.data.Bet;
import rw.data.Player;
import rw.data.SavedData;
import rw.data.Team;
import rw.enums.BetOutcome;

import java.util.Optional;

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
    private RadioButton viewBetButton;

    @FXML
    private RadioButton viewPlayersButton;

    @FXML
    private RadioButton viewTeamsButton;

    @FXML
    private Button deleteDataButton;

    @FXML
    private Button editDataButton;

    @FXML
    private GridPane gridPane;

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
                alert.setContentText("Data saved successfully!");
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
                alert.setHeaderText("Data loaded successfully!");

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
    void editData(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void deleteData(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void viewBets() {
        // Clear any existing content
        clearDataView();

        System.out.println("viewBets method called, total bets: " + bets.size());

        if (bets.isEmpty()) {
            displayMessage("No bets to display");
            return;
        }

        TextField idHeader = new TextField("ID");
        TextField dateHeader = new TextField("Date");
        TextField homeHeader = new TextField("Home Team");
        TextField awayHeader = new TextField("Away Team");
        TextField typeHeader = new TextField("Bet Type");
        TextField wagerHeader = new TextField("Wager");
        TextField oddsHeader = new TextField("Odds");
        TextField outComeHeader = new TextField("Outcome");

        idHeader.getStyleClass().add("header-field");
        dateHeader.getStyleClass().add("header-field");
        homeHeader.getStyleClass().add("header-field");
        awayHeader.getStyleClass().add("header-field");
        typeHeader.getStyleClass().add("header-field");
        wagerHeader.getStyleClass().add("header-field");
        oddsHeader.getStyleClass().add("header-field");
        outComeHeader.getStyleClass().add("header-field");

        idHeader.setEditable(false);
        dateHeader.setEditable(false);
        homeHeader.setEditable(false);
        awayHeader.setEditable(false);
        typeHeader.setEditable(false);
        wagerHeader.setEditable(false);
        oddsHeader.setEditable(false);
        outComeHeader.setEditable(false);

        gridPane.add(idHeader,1,0);
        gridPane.add(dateHeader,2,0);
        gridPane.add(homeHeader,3,0);
        gridPane.add(awayHeader,4,0);
        gridPane.add(typeHeader,5,0);
        gridPane.add(wagerHeader,6,0);
        gridPane.add(oddsHeader,7,0);
        gridPane.add(outComeHeader,8,0);

        int rowIndex = 1;
        for (Bet bet : bets) {
            TextField id = new TextField(bet.getId());
            TextField date = new TextField(bet.getFormattedGameDate());
            TextField home = new TextField(bet.getTeam1());
            TextField away = new TextField(bet.getTeam2());
            TextField type = new TextField(String.valueOf(bet.getBetType()));
            TextField wager = new TextField(String.valueOf(bet.getAmountWagered()));
            TextField odds = new TextField(String.valueOf(bet.getOdds()));
            TextField outcome = new TextField(String.valueOf(bet.getOutcome()));
            ToggleGroup toggleGroup = new ToggleGroup();
            RadioButton button = new RadioButton();
            button.setToggleGroup(toggleGroup);

            id.setEditable(false);
            date.setEditable(false);
            home.setEditable(false);
            away.setEditable(false);
            type.setEditable(false);
            wager.setEditable(false);
            odds.setEditable(false);
            outcome.setEditable(false);

            gridPane.add(button,0,rowIndex);
            gridPane.add(id, 1, rowIndex);
            gridPane.add(date, 2, rowIndex);
            gridPane.add(home, 3, rowIndex);
            gridPane.add(away, 4, rowIndex);
            gridPane.add(type, 5, rowIndex);
            gridPane.add(wager, 6, rowIndex);
            gridPane.add(odds, 7, rowIndex);
            gridPane.add(outcome, 8, rowIndex);

            rowIndex++;
        }
    }

    @FXML
    void viewPlayers() {
        // Clear any existing content
        clearDataView();

        System.out.println("viewPlayers method called, total players: " + players.size());

        if (players.isEmpty()) {
            displayMessage("No players to display");
            return;
        }

        TextField playerNameHeader = new TextField("Name");
        TextField teamNameHeader = new TextField("Team");
        TextField positionHeader = new TextField("Position");
        TextField typeHeader = new TextField("Type");
        TextField ppgHeader = new TextField("PPG");
        TextField rpgHeader = new TextField("RPG");
        TextField apgHeader = new TextField("APG");

        playerNameHeader.getStyleClass().add("header-field");
        teamNameHeader.getStyleClass().add("header-field");
        positionHeader.getStyleClass().add("header-field");
        typeHeader.getStyleClass().add("header-field");
        ppgHeader.getStyleClass().add("header-field");
        rpgHeader.getStyleClass().add("header-field");
        apgHeader.getStyleClass().add("header-field");

        playerNameHeader.setEditable(false);
        teamNameHeader.setEditable(false);
        positionHeader.setEditable(false);
        typeHeader.setEditable(false);
        ppgHeader.setEditable(false);
        rpgHeader.setEditable(false);
        apgHeader.setEditable(false);

        gridPane.add(playerNameHeader,1,0);
        gridPane.add(teamNameHeader,2,0);
        gridPane.add(positionHeader,3,0);
        gridPane.add(typeHeader,4,0);
        gridPane.add(ppgHeader,5,0);
        gridPane.add(rpgHeader,6,0);
        gridPane.add(apgHeader,7,0);

        int rowIndex = 1;
        for (Player player : players) {
            TextField playerName = new TextField(player.getPlayerName());
            TextField teamName = new TextField(player.getTeamName());
            TextField position = new TextField(player.getPosition());
            TextField type = new TextField(player.getPlayerType());
            TextField ppg = new TextField(String.valueOf(player.getStat("pointsPerGame")));
            TextField rpg = new TextField(String.valueOf(player.getStat("reboundsPerGame")));
            TextField apg = new TextField(String.valueOf(player.getStat("assistsPerGame")));
            ToggleGroup toggleGroup = new ToggleGroup();
            RadioButton button = new RadioButton();
            button.setToggleGroup(toggleGroup);

            playerName.setEditable(false);
            teamName.setEditable(false);
            position.setEditable(false);
            type.setEditable(false);
            ppg.setEditable(false);
            rpg.setEditable(false);
            apg.setEditable(false);

            gridPane.add(button,0,rowIndex);
            gridPane.add(playerName, 1, rowIndex);
            gridPane.add(teamName, 2, rowIndex);
            gridPane.add(position, 3, rowIndex);
            gridPane.add(type, 4, rowIndex);
            gridPane.add(ppg, 5, rowIndex);
            gridPane.add(rpg, 6, rowIndex);
            gridPane.add(apg, 7, rowIndex);

            rowIndex++;
        }
    }

    @FXML
    void viewTeams() {
        // Clear any existing content
        clearDataView();

        System.out.println("viewTeams method called, total teams: " + teams.size());

        if (teams.isEmpty()) {
            displayMessage("No teams to display");
            return;
        }

        TextField teamNameHeader = new TextField("Team");
        TextField conferenceHeader = new TextField("Conference");
        TextField winsHeader = new TextField("Wins");
        TextField lossesHeader = new TextField("Losses");
        TextField pointsScoredHeader = new TextField("Points Scored");
        TextField pointsAllowedHeader = new TextField("Points Allowed");

        teamNameHeader.getStyleClass().add("header-field");
        conferenceHeader.getStyleClass().add("header-field");
        winsHeader.getStyleClass().add("header-field");
        lossesHeader.getStyleClass().add("header-field");
        pointsScoredHeader.getStyleClass().add("header-field");
        pointsAllowedHeader.getStyleClass().add("header-field");

        teamNameHeader.setEditable(false);
        conferenceHeader.setEditable(false);
        winsHeader.setEditable(false);
        lossesHeader.setEditable(false);
        pointsScoredHeader.setEditable(false);
        pointsAllowedHeader.setEditable(false);

        gridPane.add(teamNameHeader,1,0);
        gridPane.add(conferenceHeader,2,0);
        gridPane.add(winsHeader,3,0);
        gridPane.add(lossesHeader,4,0);
        gridPane.add(pointsScoredHeader,5,0);
        gridPane.add(pointsAllowedHeader,6,0);

        int rowIndex = 1;
        for (Team team : teams) {
            TextField teamName = new TextField(team.getTeamName());
            TextField conference = new TextField(team.getConference());
            TextField wins = new TextField(String.valueOf(team.getWins()));
            TextField losses = new TextField(String.valueOf(team.getLosses()));
            TextField pS = new TextField(String.valueOf(team.getPointsScored()));
            TextField pA = new TextField(String.valueOf(team.getPointsAllowed()));
            ToggleGroup toggleGroup = new ToggleGroup();
            RadioButton button = new RadioButton();
            button.setToggleGroup(toggleGroup);


            teamName.setEditable(false);
            conference.setEditable(false);
            wins.setEditable(false);
            losses.setEditable(false);
            pS.setEditable(false);
            pA.setEditable(false);

            gridPane.add(button, 0, rowIndex);
            gridPane.add(teamName, 1, rowIndex);
            gridPane.add(conference, 2, rowIndex);
            gridPane.add(wins, 3, rowIndex);
            gridPane.add(losses, 4, rowIndex);
            gridPane.add(pS, 5, rowIndex);
            gridPane.add(pA, 6, rowIndex);

            rowIndex++;
        }
    }

    private void clearDataView() {
        gridPane.getChildren().clear();
    }

    private void displayMessage(String message) {
        Label label = new Label(message);
        label.setMinWidth(400);
        gridPane.add(label,0,0);
    }

    @FXML
    void mostProfitable(ActionEvent event) {

    }

    @FXML
    void profitLossSummary(ActionEvent event) {
        clearDataView();

        if (bets.isEmpty()) {
            displayMessage("No bets to analyze");
            return;
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n===== Profit/Loss Summary =====");

        int totalBets = bets.size();
        int winningBets = 0;
        int losingBets = 0;
        int pendingBets = 0;

        for (Bet bet : bets) {
            if (bet.getOutcome() == BetOutcome.WIN) {
                winningBets++;
            } else if (bet.getOutcome() == BetOutcome.LOSS) {
                losingBets++;
            } else {
                pendingBets++;
            }
        }

        double total = 0;
        for (Bet bet : bets) {
            if (bet.getOutcome() == BetOutcome.WIN) {
                total += bet.getPayout() - bet.getAmountWagered();
            } else if (bet.getOutcome() == BetOutcome.LOSS) {
                total -= bet.getAmountWagered();
            }
        }

        stringBuilder.append("Total bets: " + totalBets);
        stringBuilder.append("Winning bets: " + winningBets);
        stringBuilder.append("Losing bets: " + losingBets);
        stringBuilder.append("Pending bets: " + pendingBets);
        stringBuilder.append("Total profit/loss: $" + String.format("%.2f", total));

        TextArea textArea = new TextArea(stringBuilder.toString());

        gridPane.add(textArea,0,0);
    }

    @FXML
    void profitLossByType(ActionEvent event) {

    }

    @FXML
    private void openAPIWindow(ActionEvent event) {
        try {
            // load API FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/rw/app/API.fxml"));
            Parent apiRoot = loader.load();

            // create new Stage (window) for the API
            Stage apiStage = new Stage();
            apiStage.setTitle("API Window");
            apiStage.setScene(new Scene(apiRoot));
            apiStage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
