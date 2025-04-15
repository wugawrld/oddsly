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

public class AddPlayerController implements SceneController {
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
    void close(ActionEvent event) {
        sceneManager.switchToScene("Main");
    }

    @FXML
    void addBasketballStats(ActionEvent event) {
        statusLabelL.setTextFill(Color.GREEN);
        statusLabelL.setText("Basketball player chosen");

        statsField.getChildren().clear();

        Label pointsLabel = new Label("Points Per Game");
        Label reboundsLabel = new Label("Rebounds Per Game");
        Label assistsLabel = new Label("Assists Per Game");

        pointsLabel.setMinHeight(30);
        pointsLabel.setMinWidth(120);
        reboundsLabel.setMinHeight(30);
        reboundsLabel.setMinWidth(120);
        assistsLabel.setMinHeight(30);
        assistsLabel.setMinWidth(120);

        statsField.add(pointsLabel, 0, 0);
        statsField.add(reboundsLabel, 1, 0);
        statsField.add(assistsLabel, 2, 0);

        pointsField = new TextField();
        reboundsField = new TextField();
        assistsField = new TextField();

        pointsField.setPrefHeight(30);
        pointsField.setPrefWidth(120);
        reboundsField.setPrefHeight(30);
        reboundsField.setPrefWidth(120);
        assistsField.setPrefHeight(30);
        assistsField.setPrefWidth(120);

        statsField.add(pointsField, 0,1);
        statsField.add(reboundsField, 1,1);
        statsField.add(assistsField, 2,1);

        statsField.setHgap(20);
        statsField.setVgap(15);
    }

    @FXML
    void addHockeyStats(ActionEvent event) {
        statusLabelL.setTextFill(Color.GREEN);
        statusLabelL.setText("Hockey player chosen");

        statsField.getChildren().clear();

        Label pointsLabel = new Label("Points Per Game");
        Label assistsLabel = new Label("Assists Per Game");

        pointsLabel.setMinHeight(30);
        pointsLabel.setMinWidth(120);
        assistsLabel.setMinHeight(30);
        assistsLabel.setMinWidth(120);

        statsField.add(pointsLabel, 0, 0);
        statsField.add(assistsLabel, 1, 0);

        pointsField = new TextField();
        assistsField = new TextField();

        pointsField.setPrefHeight(30);
        pointsField.setPrefWidth(120);
        assistsField.setPrefHeight(30);
        assistsField.setPrefWidth(120);

        statsField.add(pointsField, 0,1);
        statsField.add(assistsField, 1,1);

        statsField.setHgap(20);
        statsField.setVgap(15);
    }

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

    private Boolean fieldsEmpty() {
        return playerName.getText().isEmpty() || teamName.getText().isEmpty()
                || position.getText().isEmpty() || pointsField.getText().isEmpty()
                || (assistsField != null && assistsField.getText().isEmpty());
    }

    @FXML
    void about(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Add Player");
        StringBuffer stringBuffer = new StringBuffer();
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

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setMinHeight(400);
        dialogPane.setMinWidth(300);

        alert.setContentText(stringBuffer.toString());

        ButtonType button = ButtonType.OK;
        alert.getButtonTypes().setAll(button);

        alert.showAndWait();
    }

    @FXML
    void addNewPlayer(ActionEvent event) {
        try {
            System.out.println("addNewPlayer method called");
            statusLabelL.setTextFill(Color.BLACK);
            statusLabelL.setText("");

        if (fieldsEmpty()) {
            statusLabelL.setTextFill(Color.RED);
            statusLabelL.setText("You must complete all fields before adding a new player");
            return;
        }

        try {
            String playerN = playerName.getText();
            String teamN = teamName.getText();
            String p = position.getText();
            double ppg = Double.parseDouble(pointsField.getText());
            double apg = Double.parseDouble(assistsField.getText());
            String league;

            if (basketballButton.isSelected()) {
                try {
                    league = "NBA";
                    double rbg = Double.parseDouble(reboundsField.getText());
                    if (checkTeam(teamN, league)) {
                        player = new BasketballPlayer(playerN, teamN, p, ppg, rbg, apg);
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
                        statusLabelL.setText(String.format("Invalid Team: %s, for league %s", teamName.getText(), league));
                    }
                } catch (NumberFormatException e3) {
                    statusLabelL.setTextFill(Color.RED);
                    statusLabelL.setText(String.format("Failed to parse double Rebounds Per Game from %s", reboundsField.getText()));
                }
            } else if (hockeyButton.isSelected()) {
                league = "NHL";
                if (checkTeam(teamN, league)) {
                    player = new HockeyPlayer(playerN, teamN, p, ppg, apg);
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
            statusLabelL.setTextFill(Color.RED);
            statusLabelL.setText(String.format("Failed to parse double Points Per Game from %s%n or Assists Per Game from %s",
                    (pointsField != null ? pointsField.getText() : "null"),
                    (assistsField != null ? assistsField.getText() : "null")));
        }

        } catch (Exception e) {
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
    public void initialize() {
        statusLabelL.setTextFill(Color.BLACK);
        statusLabelL.setText("");

        statusLabelR.setTextFill(Color.BLACK);
        statusLabelR.setText("Enter info to add new player.");

        ToggleGroup toggleGroup = new ToggleGroup();
        basketballButton.setToggleGroup(toggleGroup);
        hockeyButton.setToggleGroup(toggleGroup);
    }

    @Override
    public void onSceneDisplayed() {
        // Clear all form fields
        clearPlayerForm();

        // Reset labels
        statusLabelL.setTextFill(Color.BLACK);
        statusLabelL.setText("");

        statusLabelR.setTextFill(Color.BLACK);
        statusLabelR.setText("Enter info to add new player.");

        about(null);
    }
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