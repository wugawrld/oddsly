package rw.app;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
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
import rw.enums.BetType;

import java.util.*;

import java.io.*;

public class MainController implements SceneController {

    private static List<Bet> bets = new ArrayList<>();
    private static List<Player> players = new ArrayList<>();
    private static List<Team> teams = new ArrayList<>();
    private Bet selectedBet;

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
    private Label statusLabelL;

    @FXML
    private Label statusLabelR;

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
        statusLabelL.setTextFill(Color.BLACK);
        statusLabelL.setText("");

        statusLabelR.setTextFill(Color.BLACK);
        statusLabelR.setText("Welcome to the Sports Bet Tracker");

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
        if (selectedBet == null) {
            statusLabelL.setTextFill(Color.RED);
            statusLabelL.setText("Please select a bet to edit");
            return;
        }

        try {

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Edit Bet");
            dialog.setHeaderText("Edit bet: " + selectedBet.getId());

            ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

            GridPane editBetGrid = new GridPane();
            editBetGrid.setHgap(10);
            editBetGrid.setVgap(10);
            editBetGrid.setPadding(new Insets(20, 150, 10, 10));

            TextField team1Input = new TextField(selectedBet.getTeam1());
            TextField team2Input = new TextField(selectedBet.getTeam2());
            ComboBox<BetOutcome> outcomeCombo = new ComboBox<>();
            outcomeCombo.getItems().addAll(BetOutcome.values());
            outcomeCombo.setValue(selectedBet.getOutcome());
            TextField wagerInput = new TextField(String.valueOf(selectedBet.getAmountWagered()));
            TextField oddsInput = new TextField(String.valueOf(selectedBet.getOdds()));

            editBetGrid.add(new Label("Home Team:"), 0, 0);
            editBetGrid.add(team1Input, 1, 0);
            editBetGrid.add(new Label("Away Team:"), 0, 1);
            editBetGrid.add(team2Input, 1, 1);
            editBetGrid.add(new Label("Outcome:"), 0, 2);
            editBetGrid.add(outcomeCombo, 1, 2);
            editBetGrid.add(new Label("Wager:"), 0, 3);
            editBetGrid.add(wagerInput, 1, 3);
            editBetGrid.add(new Label("Odds:"), 0, 4);
            editBetGrid.add(oddsInput, 1, 4);

            dialog.getDialogPane().setContent(editBetGrid);

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == saveButtonType) {
                selectedBet.setTeam1(team1Input.getText());
                selectedBet.setTeam2(team2Input.getText());
                selectedBet.setOutcome(outcomeCombo.getValue());

                try {
                    double wager = Double.parseDouble(wagerInput.getText());
                    double odds = Double.parseDouble(oddsInput.getText());
                    selectedBet.setAmountWagered(wager);
                    selectedBet.setOdds(odds);
                } catch (NumberFormatException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Input Error");
                    alert.setHeaderText("Invalid Number Format");
                    alert.setContentText("Please enter valid numbers for wager and odds.");
                    alert.showAndWait();
                    return;
                }

                viewBets();

                statusLabelL.setTextFill(Color.GREEN);
                statusLabelL.setText("Your bet has been updated successfully!");
            }
        } catch (Exception e) {
            statusLabelL.setTextFill(Color.RED);
            statusLabelL.setText("Error with edit bet");
        }
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
            final Bet currentBet = bet;

            button.setOnAction(event -> {
                selectedBet = currentBet;
                statusLabelL.setText("Selected bet: " + selectedBet.getId());
                statusLabelL.setTextFill(Color.BLACK);
            button.setToggleGroup(toggleGroup);});

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
        BetType mostProfitable = getMostProfitableBetType();
        if (mostProfitable != null) {
            clearDataView();
            displayMessage("Most profitable bet type: " + mostProfitable.getDisplayName());
        } else {
            clearDataView();
            displayMessage("No profitable bet types found.");
        }
    }

    @FXML
    void profitLossSummary(ActionEvent event) {
        // If no bets are available display "No bets to analyze".
        if (bets.isEmpty()) {
            clearDataView();
            displayMessage("No bets to analyze");
            return;
        }

        try {
            // From original profit loss summary calculations in Demo 2.
            int totalBets = bets.size();
            int winningBets = 0;
            int losingBets = 0;
            int pendingBets = 0;
            double totalProfit = 0;

            for (Bet bet : bets) {
                if (bet.getOutcome() == BetOutcome.WIN) {
                    winningBets++;
                    totalProfit += bet.getPayout() - bet.getAmountWagered();
                } else if (bet.getOutcome() == BetOutcome.LOSS) {
                    losingBets++;
                    totalProfit -= bet.getAmountWagered();
                } else {
                    pendingBets++;
                }
            }

            // Create new stage / pop up to display data.
            Stage summaryStage = new Stage();
            summaryStage.setTitle("Profit / Loss Summary");

            TabPane tabPane = new TabPane();

            Tab textTab = new Tab("Summary");
            textTab.setClosable(false);

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("===== PROFIT/LOSS SUMMARY =====\n\n");
            stringBuilder.append("Total bets: ").append(totalBets).append("\n");
            stringBuilder.append("Winning bets: ").append(winningBets).append("\n");
            stringBuilder.append("Losing bets: ").append(losingBets).append("\n");
            stringBuilder.append("Pending bets: ").append(pendingBets).append("\n\n");
            stringBuilder.append("Total profit/loss: $").append(String.format("%.2f", totalProfit)).append("\n");

            double winRate = totalBets > 0 ? (double) winningBets / totalBets * 100 : 0;
            stringBuilder.append("Win rate: ").append(String.format("%.1f%%", winRate));

            TextArea textArea = new TextArea(stringBuilder.toString());
            textArea.setEditable(false);
            textArea.setPrefWidth(400);
            textArea.setPrefHeight(300);
            textTab.setContent(textArea);

            Tab chartTab = new Tab("Visual Chart");
            chartTab.setClosable(false);

            PieChart pieChart = new PieChart();
            pieChart.setTitle("Bet Outcomes");

            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                    new PieChart.Data("Wins", winningBets),
                    new PieChart.Data("Losses", losingBets),
                    new PieChart.Data("Pending", pendingBets)
            );
            pieChart.setData(pieChartData);
            chartTab.setContent(pieChart);

            tabPane.getTabs().addAll(textTab, chartTab);

            Scene scene = new Scene(tabPane, 500, 400);
            summaryStage.setScene(scene);
            summaryStage.show();

        } catch (Exception e) {
            statusLabelL.setTextFill(Color.RED);
            statusLabelL.setText("Error showing data analysis");
        }
    }

    @FXML
    void profitLossByType(ActionEvent event) {
        Map<BetType, Double> profitByType = getProfitLossByBetType();
        try {
            Stage summaryStage = new Stage();

            summaryStage.setTitle("Profit/Loss by Bet Type");

            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append("\nProfit/Loss by Bet Type:");
            for (BetType type : BetType.values()) {
                double profit = profitByType.getOrDefault(type, 0.0);
                stringBuilder.append(type.getDisplayName()).append(": $").append(String.format("%.2f", profit));
                stringBuilder.append("\n");
            }

            TextArea textArea = new TextArea(stringBuilder.toString());
            textArea.setEditable(false);
            textArea.setPrefWidth(400);
            textArea.setPrefHeight(300);

            Scene scene = new Scene(textArea, 500, 400);
            summaryStage.setScene(scene);
            summaryStage.show();
        } catch (Exception e) {
            statusLabelL.setTextFill(Color.RED);
            statusLabelL.setText("Error showing data analysis");
        }
    }

    // Get profit/loss by bet type.
    public Map<BetType, Double> getProfitLossByBetType() {
        Map<BetType, Double> profitByType = new HashMap<>();
        Map<BetType, Integer> countByType = new HashMap<>();

        for (Bet bet : bets) {
            if (bet.getOutcome() != BetOutcome.PENDING) {
                BetType type = bet.getBetType();
                double currentProfit = profitByType.getOrDefault(type, 0.0);
                int currentCount = countByType.getOrDefault(type, 0);

                if (bet.getOutcome() == BetOutcome.WIN) {
                    currentProfit += bet.getPayout() - bet.getAmountWagered();
                } else {
                    currentProfit -= bet.getAmountWagered();
                }

                profitByType.put(type, currentProfit);
                countByType.put(type, currentCount + 1);
            }
        }
        return profitByType;
    }

    // Find the most profitable bet type.
    public BetType getMostProfitableBetType() {
        Map<BetType, Double> profitByType = getProfitLossByBetType();
        BetType mostProfitable = null;
        double highestProfit = Double.NEGATIVE_INFINITY;

        for (Map.Entry<BetType, Double> entry : profitByType.entrySet()) {
            if (entry.getValue() > highestProfit) {
                highestProfit = entry.getValue();
                mostProfitable = entry.getKey();
            }
        }
        return mostProfitable;
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
