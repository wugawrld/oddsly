package rw.app;

/**
 * CPSC 233 Project AddPlayerController ...
 * @author Jarod Rideout, Risha Vaghani, Sardar Waheed
 * @email jarod.rideout@ucalgary.ca, risha.vaghani@ucalgary.ca, sardar.waheed@ucalgary.ca
 * @tutorial Tutorial 1 Tuesdays 11:00 - 13:00
 * @version 3.0
 */

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import rw.data.BasketballPlayer;
import rw.data.HockeyPlayer;
import rw.data.Player;
import rw.shell.Main;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.util.Optional;

// AddPlayerController class that implements interface SceneController. Used to control AddPlayer scene functionality.
public class AddPlayerController implements SceneController {

    // initialize Player being created
    private static Player player;

    @FXML
    private GridPane statsField;

    @FXML
    private TextField playerName;

    @FXML
    private TextField position;

    @FXML
    private TextField teamName;

    @FXML
    private TextField pointsField;

    @FXML
    private TextField reboundsField;

    @FXML
    private TextField assistsField;

    @FXML
    private RadioButton basketballButton;

    @FXML
    private RadioButton hockeyButton;

    @FXML
    private Label statusLabelL;

    @FXML
    private Label statusLabelR;

    @FXML
    private Button addPlayerButton;

    @FXML
    private Button saveButton;

    @FXML
    private Font x1;

    @FXML
    private Color x2;

    @FXML
    private Font x3;

    @FXML
    private Color x4;

    private SceneManager sceneManager;

    @FXML
    // Close AddPlayer scene and switch to Main scene if close is chosen from menu.
    void close(ActionEvent event) {
        sceneManager.switchToScene("Main");
    }

    @FXML
    // Opens options for inputs that are specific to BasketBall Players if the basketballPlayerButton is selected.
    void addBasketballStats(ActionEvent event) {
        // display message that basketball player is chosen to user.
        statusLabelL.setTextFill(Color.GREEN);
        statusLabelL.setText("Basketball player chosen");

        // clear out any data there
        statsField.getChildren().clear();

        // create labels for data entry and set parameters
        Label pointsLabel = new Label("Points Per Game");
        Label reboundsLabel = new Label("Rebounds Per Game");
        Label assistsLabel = new Label("Assists Per Game");

        pointsLabel.setMinHeight(30);
        pointsLabel.setMinWidth(120);
        reboundsLabel.setMinHeight(30);
        reboundsLabel.setMinWidth(120);
        assistsLabel.setMinHeight(30);
        assistsLabel.setMinWidth(120);

        // add labels
        statsField.add(pointsLabel, 0, 0);
        statsField.add(reboundsLabel, 1, 0);
        statsField.add(assistsLabel, 2, 0);

        // create input text fields for each stat and set parameters.
        pointsField = new TextField();
        reboundsField = new TextField();
        assistsField = new TextField();

        pointsField.setPrefHeight(30);
        pointsField.setPrefWidth(120);
        reboundsField.setPrefHeight(30);
        reboundsField.setPrefWidth(120);
        assistsField.setPrefHeight(30);
        assistsField.setPrefWidth(120);

        // add text fields
        statsField.add(pointsField, 0,1);
        statsField.add(reboundsField, 1,1);
        statsField.add(assistsField, 2,1);

        statsField.setHgap(20);
        statsField.setVgap(15);
    }

    @FXML
    // Opens options for inputs that are specific to Hockey Players if the hockeyPlayerButton is selected.
    void addHockeyStats(ActionEvent event) {
        // display message that hockey player is chosen to user.
        statusLabelL.setTextFill(Color.GREEN);
        statusLabelL.setText("Hockey player chosen");

        // clear out any data there
        statsField.getChildren().clear();

        // create labels for data entry and set parameters
        Label pointsLabel = new Label("Points Per Game");
        Label assistsLabel = new Label("Assists Per Game");

        pointsLabel.setMinHeight(30);
        pointsLabel.setMinWidth(120);
        assistsLabel.setMinHeight(30);
        assistsLabel.setMinWidth(120);

        // add labels
        statsField.add(pointsLabel, 0, 0);
        statsField.add(assistsLabel, 1, 0);

        // create input text fields for each stat and set parameters.
        pointsField = new TextField();
        assistsField = new TextField();

        pointsField.setPrefHeight(30);
        pointsField.setPrefWidth(120);
        assistsField.setPrefHeight(30);
        assistsField.setPrefWidth(120);

        // add text fields
        statsField.add(pointsField, 0,1);
        statsField.add(assistsField, 1,1);

        statsField.setHgap(20);
        statsField.setVgap(15);
    }

    // Check if the input for teams is valid (team in NHL / NBA)
    private Boolean checkTeam(String input, String league) {
        // Checks if league to check is NBA.
        if (league.equalsIgnoreCase("NBA")) {
            // Checks if any team in NBA matches input.
            for (String team : rw.shell.Main.nbaTeams) {
                if (team.equalsIgnoreCase(input)) {
                    return true;
                }
            }
        }
        // Checks if league to check is NHL.
        else if (league.equalsIgnoreCase("NHL")) {
            // Checks if any team in NHL matches input.
            for (String team : Main.nhlTeams) {
                if (team.equalsIgnoreCase(input)) {
                    return true;
                }
            }
        }
        return false;
    }

    // Checks if any field is empty
    private Boolean fieldsEmpty() {
        return playerName.getText().isEmpty() || teamName.getText().isEmpty()
                || position.getText().isEmpty() ||
                (pointsField != null && pointsField.getText().isEmpty())
                || (assistsField != null && assistsField.getText().isEmpty()) ||
                (!basketballButton.isSelected() && !hockeyButton.isSelected());}

    @FXML
    // Creates a popup alert explaining how to add a player and gives an example.
    void about(ActionEvent event) {
        // Create new alert with appropriate title and header.
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Add Player");

        // Create a new string buffer to allow "gluing" of multi line strings together
        StringBuffer stringBuffer = new StringBuffer();
        // Give instructions for add player and an example.
        stringBuffer.append("Instructions for Add Player: \n");
        stringBuffer.append("Enter Player's name, team, and position. Then select the appropriate button for the player" +
                "and fill out the corresponding statistics.\n");
        stringBuffer.append("\nExample:\n");
        stringBuffer.append("Player Name: LeBron James\n");
        stringBuffer.append("Team Name: Los Angeles Lakers\n");
        stringBuffer.append("Position: Forward\n");
        stringBuffer.append("*With Basketball Player Selected*\n");
        stringBuffer.append("Points Per Game: 27\n");
        stringBuffer.append("Rebounds Per Game: 7.5\n");
        stringBuffer.append("Assists Per Game: 7.4");

        // Add contents to alert.
        alert.setContentText(stringBuffer.toString());

        // Add an OK button to take down pop up.
        ButtonType button = ButtonType.OK;
        alert.getButtonTypes().setAll(button);

        alert.showAndWait();
    }

    @FXML
    // Creates a new player based on user input.
    void addNewPlayer(ActionEvent event) {
        try {
            // set status label to empty.
            System.out.println("addNewPlayer method called");
            statusLabelL.setTextFill(Color.BLACK);
            statusLabelL.setText("");

            // if any field is empty display to user that they must complete all fields.
            if (fieldsEmpty()) {
                statusLabelL.setTextFill(Color.RED);
                statusLabelL.setText("You must complete all fields before adding a new player");
                return;
            }

            try {
                // set all variable from their corresponding input
                String playerN = playerName.getText();
                String teamN = teamName.getText();
                String p = position.getText();
                // try parsing double from ppg and apg.
                double ppg = Double.parseDouble(pointsField.getText());
                double apg = Double.parseDouble(assistsField.getText());
                String league;

                // if basketball player is selected set league to NBA and have user also input RPG.
                if (basketballButton.isSelected()) {
                    try {
                        league = "NBA";
                        double rbg = Double.parseDouble(reboundsField.getText());
                        // Check if team name is valid for chosen player league. If checks fail display appropriate
                        // error message.
                        if (checkTeam(teamN, league)) {
                            // if all checks pass create new Player from user inputs
                            player = new BasketballPlayer(playerN, teamN, p, ppg, rbg, apg);
                            // if player does not already exist add new Player to players
                            if (!MainController.checkPlayers(playerN)) {
                                MainController.addNewPlayer(player);

                                // Show success notification
                                statusLabelL.setTextFill(Color.GREEN);
                                statusLabelL.setText(String.format("%s added successfully! Click Save to Continue", playerName.getText()));

                                // Show success notification
                                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                                successAlert.setTitle("Success");
                                successAlert.setHeaderText(null);
                                successAlert.setContentText("Player created successfully!");
                                successAlert.showAndWait();
                            } else {
                                statusLabelL.setTextFill(Color.RED);
                                statusLabelL.setText(String.format("%s is already being tracked", playerN));
                            }
                        } else {
                            statusLabelL.setTextFill(Color.RED);
                            statusLabelL.setText(String.format("Invalid Team: %s, for league %s", teamName.getText(), league));
                        }
                    } catch (NumberFormatException e3) {
                        statusLabelL.setTextFill(Color.RED);
                        statusLabelL.setText(String.format("Failed to parse double Rebounds Per Game from %s", reboundsField.getText()));
                    }

                    // if hockey player is selected set league to NHL.
                } else if (hockeyButton.isSelected()) {
                    league = "NHL";
                    // Check in team name is valid for chosen player league. If checks fail display appropriate
                    // error message.
                    if (checkTeam(teamN, league)) {
                        // if all checks pass create new Player from user inputs
                        player = new HockeyPlayer(playerN, teamN, p, ppg, apg);
                        // if player does not already exist add new Player to players
                        if (!MainController.checkPlayers(playerN)) {
                            MainController.addNewPlayer(player);
                            statusLabelL.setTextFill(Color.GREEN);
                            statusLabelL.setText(String.format("%s added successfully! Click Save to Continue", playerName.getText()));

                            // Show success notification popup
                            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                            successAlert.setTitle("Success");
                            successAlert.setHeaderText(null);
                            successAlert.setContentText("Player created successfully!");
                            successAlert.showAndWait();
                        } else {
                            statusLabelL.setTextFill(Color.RED);
                            statusLabelL.setText(String.format("%s is already being tracked", playerN));
                        }
                    } else {
                        statusLabelL.setTextFill(Color.RED);
                        statusLabelL.setText(String.format("Invalid Team: %s, for league %s", teamName.getText(), league));
                    }
                } else {
                    statusLabelL.setTextFill(Color.RED);
                    statusLabelL.setText("You must select Basketball or Hockey Player");
                }
            } catch (NumberFormatException e1) {
                // if incorrect data type is used display to user error message.
                statusLabelL.setTextFill(Color.RED);
                statusLabelL.setText(String.format("Failed to parse double Points Per Game from %s%n or Assists Per Game from %s",
                        (pointsField != null ? pointsField.getText() : "null"),
                        (assistsField != null ? assistsField.getText() : "null")));
            }
        } catch (Exception e) {
            // if other error is caught display generic error message to user.
            statusLabelL.setTextFill(Color.RED);
            statusLabelL.setText("An error occurred while creating the player");
            System.out.println("Error in addNewPlayer: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void setSceneManager(SceneManager manager) {
        this.sceneManager = manager;
    }

    @Override
    // initialization of the AddPlayer scene
    public void initialize() {
        // set status labels to empty / info message
        statusLabelL.setTextFill(Color.BLACK);
        statusLabelL.setText("");

        statusLabelR.setTextFill(Color.BLACK);
        statusLabelR.setText("Enter info to add new player.");

        // put the radio buttons for player type into their own toggle group to avoid multi selecting
        ToggleGroup toggleGroup = new ToggleGroup();
        basketballButton.setToggleGroup(toggleGroup);
        hockeyButton.setToggleGroup(toggleGroup);
    }

    @Override
    // onSceneDisplayed gives priority to what is shown when AddPlayer scene is launched
    public void onSceneDisplayed() {
        // Clear all form fields
        clearPlayerForm();

        // Reset labels
        statusLabelL.setTextFill(Color.BLACK);
        statusLabelL.setText("");

        statusLabelR.setTextFill(Color.BLACK);
        statusLabelR.setText("Enter info to add new player.");

        // Open about popup.
        about(null);
    }

    // clears all fields from AddBet scene.
    private void clearPlayerForm() {
        playerName.clear();
        teamName.clear();
        position.clear();
        statsField.getChildren().clear();

        // Reset fields to null to avoid NullPointerException when checking if empty
        pointsField = null;
        reboundsField = null;
        assistsField = null;

        // Deselect radio buttons
        basketballButton.setSelected(false);
        hockeyButton.setSelected(false);

        // Reset the player object
        player = null;
    }

    @FXML
    // saves bet
    void savePlayer(ActionEvent event) {
        try {
            System.out.println("savePlayer method called");

            // First check if there's a player to save
            if (player == null) {
                statusLabelL.setTextFill(Color.RED);
                statusLabelL.setText("No player data to save. Please add a player first.");
                return;
            }

        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Save Player");
        confirmDialog.setHeaderText("Player saved successfully");
        confirmDialog.setContentText("What would you like to do next?");

        ButtonType addAnotherButton = new ButtonType("Add Another Player");
        ButtonType returnToMainButton = new ButtonType("Return to Main Page");

        confirmDialog.getButtonTypes().setAll(addAnotherButton, returnToMainButton);

        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (result.get() == addAnotherButton) {
            // Clear the form for a new player
            clearPlayerForm();
            statusLabelL.setText("Enter information for a new player");
        } else {
            // Return to main page
            sceneManager.switchToScene("Main");
        }
        } catch (Exception e) {
            statusLabelL.setTextFill(Color.RED);
            statusLabelL.setText("An error occurred while saving the player");
            System.out.println("Error in savePlayer: " + e.getMessage());
            e.printStackTrace();
        }
    }
}