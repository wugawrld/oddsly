package rw.app;
/**
 * CPSC 233 Project MainController ...
 * @author Jarod Rideout, Risha Vaghani, Sardar Waheed
 * @email jarod.rideout@ucalgary.ca, risha.vaghani@ucalgary.ca, sardar.waheed@ucalgary.ca
 * @tutorial Tutorial 1 Tuesdays 11:00 - 13:00
 * @version 3.0
 */

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
import rw.data.*;
import rw.enums.BetOutcome;
import rw.enums.BetType;

import java.util.*;

import java.io.*;

public class MainController implements SceneController {

    // private lists for bets, players, and teams. They can be added to view AddBet, AddTeam, and AddPlayer scenes
    // through the addNewX public methods.
    private static List<Bet> bets = new ArrayList<>();
    private static List<Player> players = new ArrayList<>();
    private static List<Team> teams = new ArrayList<>();

    // variables used to keep track of which Bet, Player, or Team is selected for editing or deleting purposes.
    private Bet selectedBet;
    private Player selectedPlayer;
    private Team selectedTeam;

    // public methods for add new bets, players, or teams.
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

    // an instance of SceneManager allowing for the switching between different scenes.
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
    // initialization of the Main scene
    public void initialize() {
        // set status labels to empty / welcome message
        statusLabelL.setTextFill(Color.BLACK);
        statusLabelL.setText("");

        statusLabelR.setTextFill(Color.BLACK);
        statusLabelR.setText("Welcome to the Sports Bet Tracker");

        // put the radio buttons for viewing data into their own toggle group to avoid multi selecting
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
    // switches scene to AddBet scene if clicked in menu.
    void addBet(ActionEvent event) {
        sceneManager.switchToScene("AddBet");
    }

    @FXML
    // switches scene to AddPlayer if clicked in menu.
    void addPlayer(ActionEvent event) {
        sceneManager.switchToScene("AddPlayer");
    }

    @FXML
    // switches scene to AddTeam if clicked in menu.
    void addTeam(ActionEvent event) {
        sceneManager.switchToScene("AddTeam");
    }

    @FXML
    // method that saves data
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
    // method that loads data
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
    // exits program if clicked in menu.
    void quit(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    // calls specified method for each data type to edit them if they are selected.
    void editData(ActionEvent event) {
        if (selectedBet != null) {
            editBet();
        } else if (selectedTeam != null) {
            editTeam();
        } else if (selectedPlayer != null) {
            editPlayer();
        } else {
            // else tells user to select something to edit
            statusLabelL.setTextFill(Color.RED);
            statusLabelL.setText("Please select an item to edit");
        }
    }

    // method for editing bets. Does this through a popup dialog scene.
    void editBet() {
        // if no bet is selected it prompts user to select something.
        if (selectedBet == null) {
            statusLabelL.setTextFill(Color.RED);
            statusLabelL.setText("Please select a bet to edit");
            return;
        }

        try {

            // create new dialog window with title and head indicating which bet is being edited.
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Edit Bet");
            dialog.setHeaderText("Edit bet: " + selectedBet.getId());

            // create a cancel and save button
            ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

            // create a new grid pane setting dimensions
            GridPane editBetGrid = new GridPane();
            editBetGrid.setHgap(10);
            editBetGrid.setVgap(10);
            editBetGrid.setPadding(new Insets(20, 150, 10, 10));

            // create new textboxes for user input and a ComboBox (selector box) with options for Bet Outcomes.
            TextField team1Input = new TextField(selectedBet.getTeam1());
            TextField team2Input = new TextField(selectedBet.getTeam2());
            ComboBox<BetOutcome> outcomeCombo = new ComboBox<>();
            outcomeCombo.getItems().addAll(BetOutcome.values());
            outcomeCombo.setValue(selectedBet.getOutcome());
            TextField wagerInput = new TextField(String.valueOf(selectedBet.getAmountWagered()));
            TextField oddsInput = new TextField(String.valueOf(selectedBet.getOdds()));

            // add each field to the grid pane.
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

            // add the grid pane to the dialog window
            dialog.getDialogPane().setContent(editBetGrid);

            // if user selects the save button, set information edited to the information of that bet.
            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == saveButtonType) {
                selectedBet.setTeam1(team1Input.getText());
                selectedBet.setTeam2(team2Input.getText());
                selectedBet.setOutcome(outcomeCombo.getValue());

                try {
                    // special case to check for number inputs
                    double wager = Double.parseDouble(wagerInput.getText());
                    double odds = Double.parseDouble(oddsInput.getText());
                    selectedBet.setAmountWagered(wager);
                    selectedBet.setOdds(odds);
                } catch (NumberFormatException e) {
                    // give pop up error alert if input is incorrect
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Input Error");
                    alert.setHeaderText("Invalid Number Format");
                    alert.setContentText("Please enter valid numbers for wager and odds.");
                    alert.showAndWait();
                    return;
                }

                // give an updated view of bets and give success status to user.
                viewBets();

                statusLabelL.setTextFill(Color.GREEN);
                statusLabelL.setText("Your bet has been updated successfully!");
            }
        } catch (Exception e) {
            statusLabelL.setTextFill(Color.RED);
            statusLabelL.setText("Error with edit bet");
        }
    }

    // method for editing teams. Does this through a popup dialog scene.
    void editTeam() {
        // if no team is selected it prompts user to select something.
        if (selectedTeam == null) {
            statusLabelL.setTextFill(Color.RED);
            statusLabelL.setText("Please select a team to edit");
            return;
        }

        try {
            // create new dialog window with title and head indicating which team is being edited.
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Edit Team");
            dialog.setHeaderText("Edit team: " + selectedTeam.getTeamName());

            // create a cancel and save button
            ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

            // create a new grid pane setting dimensions
            GridPane editTeamGrid = new GridPane();
            editTeamGrid.setHgap(10);
            editTeamGrid.setVgap(10);
            editTeamGrid.setPadding(new Insets(20, 150, 10, 10));

            // create new textboxes for user input
            TextField winsInput = new TextField(String.valueOf(selectedTeam.getWins()));
            TextField lossesInput = new TextField(String.valueOf(selectedTeam.getLosses()));
            TextField psInput = new TextField(String.valueOf(selectedTeam.getPointsScored()));
            TextField paInput = new TextField(String.valueOf(selectedTeam.getPointsAllowed()));

            // add each field to the grid pane.
            editTeamGrid.add(new Label("Wins:"), 0, 0);
            editTeamGrid.add(winsInput, 1, 0);
            editTeamGrid.add(new Label("Losses:"), 0, 1);
            editTeamGrid.add(lossesInput, 1, 1);
            editTeamGrid.add(new Label("Points Scored:"), 0, 2);
            editTeamGrid.add(psInput, 1, 2);
            editTeamGrid.add(new Label("Points Allowed:"), 0, 3);
            editTeamGrid.add(paInput, 1, 3);

            // add the grid pane to the dialog window
            dialog.getDialogPane().setContent(editTeamGrid);

            // if user selects the save button, set information edited to the information of that team.
            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == saveButtonType) {
                try {
                    selectedTeam.setWins(Integer.parseInt(winsInput.getText()));
                    selectedTeam.setLosses(Integer.parseInt(lossesInput.getText()));
                    selectedTeam.setPointsScored(Integer.parseInt(psInput.getText()));
                    selectedTeam.setPointsAllowed(Integer.parseInt(paInput.getText()));
                } catch (NumberFormatException e) {
                    // give pop up error alert if input is incorrect
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Input Error");
                    alert.setHeaderText("Invalid Number Format");
                    alert.setContentText("Please enter valid numbers for wins, losses, points scored, and points allowed.");
                    alert.showAndWait();
                    return;
                }

                // give an updated view of teams and give success status to user.
                viewTeams();

                statusLabelL.setTextFill(Color.GREEN);
                statusLabelL.setText("Your team has been updated successfully!");
            }
        } catch (Exception e) {
            statusLabelL.setTextFill(Color.RED);
            statusLabelL.setText("Error with edit team");
        }
    }

    // method for editing players. Does this through a popup dialog scene.
    void editPlayer() {
        // if no player is selected it prompts user to select something.
        if (selectedPlayer == null) {
            statusLabelL.setTextFill(Color.RED);
            statusLabelL.setText("Please select a player to edit");
            return;
        }

        try {
            // create new dialog window with title and head indicating which player is being edited.
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Edit Player");
            dialog.setHeaderText("Edit player: " + selectedPlayer.getPlayerName());

            // create a cancel and save button
            ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

            // create a new grid pane setting dimensions
            GridPane editBetGrid = new GridPane();
            editBetGrid.setHgap(10);
            editBetGrid.setVgap(10);
            editBetGrid.setPadding(new Insets(20, 150, 10, 10));

            // create new textboxes for user input
            TextField teamNameInput = new TextField(selectedPlayer.getTeamName());
            TextField positionInput = new TextField(selectedPlayer.getPosition());
            TextField rpgInput = new TextField(String.valueOf(selectedPlayer.getStat("reboundsPerGame")));
            TextField apgInput = new TextField(String.valueOf(selectedPlayer.getStat("assistsPerGame")));
            TextField ppgInput = new TextField(String.valueOf(selectedPlayer.getStat("pointsPerGame")));

            // add each field to the grid pane.
            editBetGrid.add(new Label("Team:"), 0, 0);
            editBetGrid.add(teamNameInput, 1, 0);
            editBetGrid.add(new Label("Position:"), 0, 1);
            editBetGrid.add(positionInput, 1, 1);
            editBetGrid.add(new Label("Rebounds Per Game:"), 0, 2);
            editBetGrid.add(rpgInput, 1, 2);
            editBetGrid.add(new Label("Assists Per Game:"), 0, 3);
            editBetGrid.add(apgInput, 1, 3);
            editBetGrid.add(new Label("Points Per Game:"), 0, 4);
            editBetGrid.add(ppgInput, 1, 4);

            // add the grid pane to the dialog window
            dialog.getDialogPane().setContent(editBetGrid);

            // if user selects the save button, set information edited to the information of that player.
            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == saveButtonType) {
                try {
                    Double rpg = Double.parseDouble(rpgInput.getText());
                    Double apg = Double.parseDouble(apgInput.getText());
                    Double ppg = Double.parseDouble(ppgInput.getText());

                    if (selectedPlayer.getPlayerType().equals("BasketballPlayer")) {
                        selectedPlayer.setStat("reboundsPerGame", rpg);
                        selectedPlayer.setStat("assistsPerGame", apg);
                        selectedPlayer.setStat("pointsPerGame", ppg);
                    } else {
                        selectedPlayer.setStat("assistsPerGame", apg);
                        selectedPlayer.setStat("pointsPerGame", ppg);
                    }
                } catch (NumberFormatException e) {
                    // give pop up error alert if input is incorrect
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Input Error");
                    alert.setHeaderText("Invalid Number Format");
                    alert.setContentText("Please enter valid numbers for wager and odds.");
                    alert.showAndWait();
                    return;
                }

                // give an updated view of players and give success status to user.
                viewPlayers();

                statusLabelL.setTextFill(Color.GREEN);
                statusLabelL.setText("Your player has been updated successfully!");
            }
        } catch (Exception e) {
            statusLabelL.setTextFill(Color.RED);
            statusLabelL.setText("Error with edit player");
        }
    }

    @FXML
    // calls specified method for each data type to delete them if they are selected.
    void deleteData(ActionEvent event) {
        if (selectedBet != null) {
            deleteBet();
        } else if (selectedTeam != null) {
            deleteTeam();
        } else if (selectedPlayer != null) {
            deletePlayer();
        } else {
            // else tells user to select something to delete
            statusLabelL.setTextFill(Color.RED);
            statusLabelL.setText("Please select an item to delete");
        }
    }

    // method for deleting bets.
    void deleteBet() {
        // removes selected bet from bets.
        bets.remove(selectedBet);
        // updates view of bets and give success message to user.
        viewBets();
        statusLabelL.setTextFill(Color.GREEN);
        statusLabelL.setText("Bet deleted successfully!");
    }

    // method for deleting teams.
    void deleteTeam() {
        // removes selected team from teams.
        teams.remove(selectedTeam);
        // updates view of teams and give success message to user.
        viewTeams();
        statusLabelL.setTextFill(Color.GREEN);
        statusLabelL.setText("Team deleted successfully!");
    }

    // method for deleting players.
    void deletePlayer() {
        // removes selected player from players.
        players.remove(selectedPlayer);
        // updates view of players and give success message to user.
        viewPlayers();
        statusLabelL.setTextFill(Color.GREEN);
        statusLabelL.setText("Player deleted successfully!");
    }

    @FXML
    // method for viewing bets.
    void viewBets() {
        // Clear any existing content
        clearDataView();

        System.out.println("viewBets method called, total bets: " + bets.size());

        if (bets.isEmpty()) {
            // if no bets, displays message to user.
            displayMessage("No bets to display");
            return;
        }

        // creates TextField headers for each component of bets.
        TextField idHeader = new TextField("ID");
        TextField dateHeader = new TextField("Date");
        TextField homeHeader = new TextField("Home Team");
        TextField awayHeader = new TextField("Away Team");
        TextField typeHeader = new TextField("Bet Type");
        TextField wagerHeader = new TextField("Wager");
        TextField oddsHeader = new TextField("Odds");
        TextField outComeHeader = new TextField("Outcome");

        // sets the style of the header to header field
        idHeader.getStyleClass().add("header-field");
        dateHeader.getStyleClass().add("header-field");
        homeHeader.getStyleClass().add("header-field");
        awayHeader.getStyleClass().add("header-field");
        typeHeader.getStyleClass().add("header-field");
        wagerHeader.getStyleClass().add("header-field");
        oddsHeader.getStyleClass().add("header-field");
        outComeHeader.getStyleClass().add("header-field");

        // sets each of the TextFields to un-editable to avoid user changing them through viewer.
        idHeader.setEditable(false);
        dateHeader.setEditable(false);
        homeHeader.setEditable(false);
        awayHeader.setEditable(false);
        typeHeader.setEditable(false);
        wagerHeader.setEditable(false);
        oddsHeader.setEditable(false);
        outComeHeader.setEditable(false);

        // add each header to the gridPane
        gridPane.add(idHeader,1,0);
        gridPane.add(dateHeader,2,0);
        gridPane.add(homeHeader,3,0);
        gridPane.add(awayHeader,4,0);
        gridPane.add(typeHeader,5,0);
        gridPane.add(wagerHeader,6,0);
        gridPane.add(oddsHeader,7,0);
        gridPane.add(outComeHeader,8,0);

        // for each bet add the corresponding data value under each header for each bet.
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
            // create a selector button for each bet.
            ToggleGroup toggleGroup = new ToggleGroup();
            RadioButton button = new RadioButton();
            final Bet currentBet = bet;

            // if button for a bet is selected it sets the selectedBet value to that bet.
            button.setOnAction(event -> {
                selectedBet = currentBet;
                statusLabelL.setText("Selected bet: " + selectedBet.getId());
                statusLabelL.setTextFill(Color.BLACK);});

            // sets each of the TextFields to un-editable to avoid user changing them through viewer.
            id.setEditable(false);
            date.setEditable(false);
            home.setEditable(false);
            away.setEditable(false);
            type.setEditable(false);
            wager.setEditable(false);
            odds.setEditable(false);
            outcome.setEditable(false);

            // add each field to grid pane
            gridPane.add(button,0,rowIndex);
            gridPane.add(id, 1, rowIndex);
            gridPane.add(date, 2, rowIndex);
            gridPane.add(home, 3, rowIndex);
            gridPane.add(away, 4, rowIndex);
            gridPane.add(type, 5, rowIndex);
            gridPane.add(wager, 6, rowIndex);
            gridPane.add(odds, 7, rowIndex);
            gridPane.add(outcome, 8, rowIndex);

            // move to the next row.
            rowIndex++;
        }
    }

    @FXML
    // method for viewing players.
    void viewPlayers() {
        // Clear any existing content
        clearDataView();

        System.out.println("viewPlayers method called, total players: " + players.size());

        if (players.isEmpty()) {
            // if no players, displays message to user.
            displayMessage("No players to display");
            return;
        }

        // creates TextField headers for each component of players.
        TextField playerNameHeader = new TextField("Name");
        TextField teamNameHeader = new TextField("Team");
        TextField positionHeader = new TextField("Position");
        TextField typeHeader = new TextField("Type");
        TextField ppgHeader = new TextField("PPG");
        TextField rpgHeader = new TextField("RPG");
        TextField apgHeader = new TextField("APG");

        // sets the style of the header to header field
        playerNameHeader.getStyleClass().add("header-field");
        teamNameHeader.getStyleClass().add("header-field");
        positionHeader.getStyleClass().add("header-field");
        typeHeader.getStyleClass().add("header-field");
        ppgHeader.getStyleClass().add("header-field");
        rpgHeader.getStyleClass().add("header-field");
        apgHeader.getStyleClass().add("header-field");

        // sets each of the TextFields to un-editable to avoid user changing them through viewer.
        playerNameHeader.setEditable(false);
        teamNameHeader.setEditable(false);
        positionHeader.setEditable(false);
        typeHeader.setEditable(false);
        ppgHeader.setEditable(false);
        rpgHeader.setEditable(false);
        apgHeader.setEditable(false);

        // add each header to the gridPane
        gridPane.add(playerNameHeader,1,0);
        gridPane.add(teamNameHeader,2,0);
        gridPane.add(positionHeader,3,0);
        gridPane.add(typeHeader,4,0);
        gridPane.add(ppgHeader,5,0);
        gridPane.add(rpgHeader,6,0);
        gridPane.add(apgHeader,7,0);

        // for each player add the corresponding data value under each header for each player.
        int rowIndex = 1;
        for (Player player : players) {
            TextField playerName = new TextField(player.getPlayerName());
            TextField teamName = new TextField(player.getTeamName());
            TextField position = new TextField(player.getPosition());
            TextField type = new TextField(player.getPlayerType());
            TextField ppg = new TextField(String.valueOf(player.getStat("pointsPerGame")));
            TextField rpg = new TextField(String.valueOf(player.getStat("reboundsPerGame")));
            TextField apg = new TextField(String.valueOf(player.getStat("assistsPerGame")));
            // create a selector button for each player.
            ToggleGroup toggleGroup = new ToggleGroup();
            RadioButton button = new RadioButton();
            button.setToggleGroup(toggleGroup);

            // if button for a player is selected it sets the selectedBet value to that player.
            final Player currentPlayer = player;
            button.setOnAction(event -> {
                selectedPlayer = currentPlayer;
                selectedBet = null;  // Clear other selections
                selectedTeam = null;
                statusLabelL.setText("Selected player: " + selectedPlayer.getPlayerName());
                statusLabelL.setTextFill(Color.BLACK);});

            // sets each of the TextFields to un-editable to avoid user changing them through viewer.
            playerName.setEditable(false);
            teamName.setEditable(false);
            position.setEditable(false);
            type.setEditable(false);
            ppg.setEditable(false);
            rpg.setEditable(false);
            apg.setEditable(false);

            // add each field to grid pane
            gridPane.add(button,0,rowIndex);
            gridPane.add(playerName, 1, rowIndex);
            gridPane.add(teamName, 2, rowIndex);
            gridPane.add(position, 3, rowIndex);
            gridPane.add(type, 4, rowIndex);
            gridPane.add(ppg, 5, rowIndex);
            gridPane.add(rpg, 6, rowIndex);
            gridPane.add(apg, 7, rowIndex);

            // move to the next row.
            rowIndex++;
        }
    }

    @FXML
    // method for viewing teams.
    void viewTeams() {
        // Clear any existing content
        clearDataView();

        System.out.println("viewTeams method called, total teams: " + teams.size());

        if (teams.isEmpty()) {
            // if no teams, displays message to user.
            displayMessage("No teams to display");
            return;
        }

        // creates TextField headers for each component of teams.
        TextField teamNameHeader = new TextField("Team");
        TextField conferenceHeader = new TextField("Conference");
        TextField winsHeader = new TextField("Wins");
        TextField lossesHeader = new TextField("Losses");
        TextField pointsScoredHeader = new TextField("Points Scored");
        TextField pointsAllowedHeader = new TextField("Points Allowed");

        // sets the style of the header to header field
        teamNameHeader.getStyleClass().add("header-field");
        conferenceHeader.getStyleClass().add("header-field");
        winsHeader.getStyleClass().add("header-field");
        lossesHeader.getStyleClass().add("header-field");
        pointsScoredHeader.getStyleClass().add("header-field");
        pointsAllowedHeader.getStyleClass().add("header-field");

        // sets each of the TextFields to un-editable to avoid user changing them through viewer.
        teamNameHeader.setEditable(false);
        conferenceHeader.setEditable(false);
        winsHeader.setEditable(false);
        lossesHeader.setEditable(false);
        pointsScoredHeader.setEditable(false);
        pointsAllowedHeader.setEditable(false);

        // add each header to the gridPane
        gridPane.add(teamNameHeader,1,0);
        gridPane.add(conferenceHeader,2,0);
        gridPane.add(winsHeader,3,0);
        gridPane.add(lossesHeader,4,0);
        gridPane.add(pointsScoredHeader,5,0);
        gridPane.add(pointsAllowedHeader,6,0);

        // for each team add the corresponding data value under each header for each team.
        int rowIndex = 1;
        for (Team team : teams) {
            TextField teamName = new TextField(team.getTeamName());
            TextField conference = new TextField(team.getConference());
            TextField wins = new TextField(String.valueOf(team.getWins()));
            TextField losses = new TextField(String.valueOf(team.getLosses()));
            TextField pS = new TextField(String.valueOf(team.getPointsScored()));
            TextField pA = new TextField(String.valueOf(team.getPointsAllowed()));
            // create a selector button for each team.
            ToggleGroup toggleGroup = new ToggleGroup();
            RadioButton button = new RadioButton();
            button.setToggleGroup(toggleGroup);

            // if button for a team is selected it sets the selectedBet value to that team.
            final Team currentTeam = team;
            button.setOnAction(event -> {
                selectedTeam = currentTeam;
                selectedBet = null;  // Clear other selections
                selectedPlayer = null;
                statusLabelL.setText("Selected team: " + selectedTeam.getTeamName());
                statusLabelL.setTextFill(Color.BLACK);});

            // sets each of the TextFields to un-editable to avoid user changing them through viewer.
            teamName.setEditable(false);
            conference.setEditable(false);
            wins.setEditable(false);
            losses.setEditable(false);
            pS.setEditable(false);
            pA.setEditable(false);

            // add each field to grid pane
            gridPane.add(button, 0, rowIndex);
            gridPane.add(teamName, 1, rowIndex);
            gridPane.add(conference, 2, rowIndex);
            gridPane.add(wins, 3, rowIndex);
            gridPane.add(losses, 4, rowIndex);
            gridPane.add(pS, 5, rowIndex);
            gridPane.add(pA, 6, rowIndex);

            // move to the next row.
            rowIndex++;
        }
    }

    // method for clearing data in Data Viewer pane.
    private void clearDataView() {
        gridPane.getChildren().clear();
    }

    // method for displaying message in Data Viewer pane.
    private void displayMessage(String message) {
        Label label = new Label(message);
        label.setMinWidth(400);
        gridPane.add(label,0,0);
    }

    @FXML
    // method that displays the most profitable Bet Type
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
    // method that displays profit / loss summary in pop up window
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

            // create new tab pane that allows different tabs of information.
            TabPane tabPane = new TabPane();

            // create a new summary tab.
            Tab textTab = new Tab("Summary");
            textTab.setClosable(false);

            // display key bet information. Same as in demo 2.
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("===== PROFIT/LOSS SUMMARY =====\n\n");
            stringBuilder.append("Total bets: ").append(totalBets).append("\n");
            stringBuilder.append("Winning bets: ").append(winningBets).append("\n");
            stringBuilder.append("Losing bets: ").append(losingBets).append("\n");
            stringBuilder.append("Pending bets: ").append(pendingBets).append("\n\n");
            stringBuilder.append("Total profit/loss: $").append(String.format("%.2f", totalProfit)).append("\n");

            double winRate = totalBets > 0 ? (double) winningBets / totalBets * 100 : 0;
            stringBuilder.append("Win rate: ").append(String.format("%.1f%%", winRate));

            // add information to TextArea and display it in the textTab
            TextArea textArea = new TextArea(stringBuilder.toString());
            textArea.setEditable(false);
            textArea.setPrefWidth(400);
            textArea.setPrefHeight(300);
            textTab.setContent(textArea);

            // create a chart tab to visualize the data
            Tab chartTab = new Tab("Visual Chart");
            chartTab.setClosable(false);

            // create a pie chart to display bet outcomes.
            PieChart pieChart = new PieChart();
            pieChart.setTitle("Bet Outcomes");

            // add number of bets for each outcome to chart.
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                    new PieChart.Data("Wins", winningBets),
                    new PieChart.Data("Losses", losingBets),
                    new PieChart.Data("Pending", pendingBets)
            );
            pieChart.setData(pieChartData);
            chartTab.setContent(pieChart);

            // add both tabs to tabPane
            tabPane.getTabs().addAll(textTab, chartTab);

            // create and launch scene pop up
            Scene scene = new Scene(tabPane, 500, 400);
            summaryStage.setScene(scene);
            summaryStage.show();

        } catch (Exception e) {
            statusLabelL.setTextFill(Color.RED);
            statusLabelL.setText("Error showing data analysis");
        }
    }

    @FXML
    // method that displays profit / loss by bet type in pop up window
    void profitLossByType(ActionEvent event) {
        Map<BetType, Double> profitByType = getProfitLossByBetType();
        try {
            Stage byTypeStage = new Stage();

            byTypeStage.setTitle("Profit/Loss by Bet Type");

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
            byTypeStage.setScene(scene);
            byTypeStage.show();
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

    @FXML
    void aboutSportsBetTracker(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About Sports Bet Tracker");
        alert.setHeaderText("Sports Bet Tracker v1.0");

        // Create a TextArea for the content (allows scrolling for longer text)
        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setText(
                "Overview\n" +
                        "Sports Bet Tracker is a comprehensive application designed to help you track and analyze your sports bets, teams, and player statistics. " +
                        "Whether you're a casual bettor or a serious sports enthusiast, this tool provides an organized way to monitor your betting performance and sports data.\n\n" +

                        "Features\n\n" +

                        "Bet Tracking\n" +
                        "• Record different bet types: Moneyline, Point Spread, and Over/Under\n" +
                        "• Track bets for NHL and NBA games\n" +
                        "• Monitor win/loss records and calculate profit/loss\n" +
                        "• Analyze betting performance by bet type\n\n" +

                        "Team Management\n" +
                        "• Store team information for NHL and NBA\n" +
                        "• Track team statistics including wins, losses, points scored, and points allowed\n" +
                        "• Organize teams by conference (Eastern/Western)\n\n" +

                        "Player Statistics\n" +
                        "• Track both basketball and hockey player statistics\n" +
                        "• Monitor key performance metrics like points, rebounds, and assists per game\n" +
                        "• Organize players by team and position\n\n" +

                        "Analysis Tools\n" +
                        "• Calculate overall profit/loss summaries\n" +
                        "• Determine your most profitable bet types\n" +
                        "• Access real-time standings through the API window\n" +
                        "• Generate visualizations of your betting performance\n\n" +

                        "Tips for Effective Use\n" +
                        "• Always validate team names against official league listings\n" +
                        "• Enter odds in decimal format (use the conversion guide for American odds)\n" +
                        "• Update bet outcomes promptly after games conclude\n" +
                        "• Use the visualization tools to identify betting patterns\n\n" +

                        "Developed for CPSC 233 Project\n" +
                        "Developed by Jarod Rideout, Risha Vaghani and Sardar Waheed\n\n" +
                        "For support,feedback or feature requests, please contact:\n" +
                        " •jarod.rideout@ucalgary.ca\n" +
                        " •risha.vaghani@ucalgary.ca\n" +
                        " •sardar.waheed@ucalgary.ca"

        );

        // Set preferred size
        textArea.setPrefHeight(400);
        textArea.setPrefWidth(600);

        // Add the TextArea to the dialog
        alert.getDialogPane().setContent(textArea);

        // Show the dialog
        alert.showAndWait();
    }
}
