package rw.app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import rw.data.Team;
import rw.shell.Main;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.util.Objects;
import java.util.Optional;

public class AddTeamController implements SceneController {

    private static Team team;

    @FXML
    private Button addTeam;

    @FXML
    private TextField conference;

    @FXML
    private TextField league;

    @FXML
    private Label statusLabelL;

    @FXML
    private Label statusLabelR;

    @FXML
    private TextField losses;

    @FXML
    private TextField pointsAllowed;

    @FXML
    private TextField pointsScored;

    @FXML
    private TextField teamName;

    @FXML
    private TextField wins;

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
    void about(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Add Team");
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Instructions for Add Team: \n");
        stringBuffer.append("Fill out all fields with accurate information to add a new tracked team.\n");
        stringBuffer.append("\nExample:\n");
        stringBuffer.append("Team Name: Calgary Flames\n");
        stringBuffer.append("League: NHL\n");
        stringBuffer.append("Conference: Western\n");
        stringBuffer.append("Wins: 38\n");
        stringBuffer.append("Losses: 41\n");
        stringBuffer.append("Points Scored: 210\n");
        stringBuffer.append("Points Allowed: 231");

        alert.setContentText(stringBuffer.toString());

        ButtonType button = ButtonType.OK;
        alert.getButtonTypes().setAll(button);

        alert.showAndWait();
    }

    @FXML
    void addTeam(ActionEvent event) {
        statusLabelL.setTextFill(Color.BLACK);
        statusLabelL.setText("");

        if (fieldsEmpty()) {
            statusLabelL.setTextFill(Color.RED);
            statusLabelL.setText("You must complete all fields before adding a new team");
            return;
        }

        try {
            int win = Integer.parseInt(wins.getText());
            int loss = Integer.parseInt(losses.getText());
            int ps = Integer.parseInt(pointsScored.getText());
            int pa = Integer.parseInt(pointsAllowed.getText());
            String l = league.getText();
            String c = conference.getText();
            String tName = teamName.getText();

            if (checkLeague(l)) {
                if (Objects.equals(c, "Eastern") || Objects.equals(c, "Western")) {
                    if (checkTeam(tName)) {
                        team = new Team(tName, c, win, loss, ps, pa);
                        MainController.addNewTeam(team);
                        statusLabelL.setTextFill(Color.GREEN);
                        statusLabelL.setText(String.format("%s added successfully! Click Save to Continue!", teamName.getText()));
                        statusLabelR.setTextFill(Color.BLACK);
                        statusLabelR.setText("Don't forget to save your data");
                    } else {
                        statusLabelL.setTextFill(Color.RED);
                        statusLabelL.setText(String.format("Invalid Team: %s, for league %s conference %s", teamName.getText(), league.getText(), conference.getText()));
                    }
                } else {
                    statusLabelL.setTextFill(Color.RED);
                    statusLabelL.setText(String.format("Invalid conference: %s", conference.getText()));
                }
            } else {
                statusLabelL.setTextFill(Color.RED);
                statusLabelL.setText(String.format("Invalid league: %s", league.getText()));
            }
        } catch (NumberFormatException e) {
            statusLabelL.setTextFill(Color.RED);
            statusLabelL.setText(String.format("Failed to parse integer wins from %s, losses from %s, points scored from %s, or points allowed from %s", wins.getText(), losses.getText(), pointsScored.getText(), pointsAllowed.getText()));
        }
    }

    private Boolean fieldsEmpty() {
        return wins.getText().isEmpty() || losses.getText().isEmpty()
                || pointsScored.getText().isEmpty() || pointsAllowed.getText().isEmpty()
                || teamName.getText().isEmpty() || conference.getText().isEmpty()
                || league.getText().isEmpty();
    }

    private Boolean checkLeague(String input) {
        // Checks if input is a part of leagueList.
        for (String league : rw.shell.Main.leagueList) {
            if (league.equalsIgnoreCase(input)) {
                return true;
            }
        }
        return false;
    }

    private Boolean checkTeam(String input) {
        String leagueCheck = league.getText();
        // Checks if league to check is NBA.
        if (leagueCheck.equalsIgnoreCase("NBA")) {
            // Checks if any team in NBA matches input.
            for (String team : rw.shell.Main.nbaTeams) {
                if (team.equalsIgnoreCase(input)) {
                    return true;
                }
            }
        }
        // Checks if league to check is NHL.
        else if (leagueCheck.equalsIgnoreCase("NHL")) {
            // Checks if any team in NHL matches input.
            for (String team : Main.nhlTeams) {
                if (team.equalsIgnoreCase(input)) {
                    return true;
                }
            }
        }
        return false;
    }

    @FXML
    void close(ActionEvent event) {
        sceneManager.switchToScene("Main");
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
        statusLabelR.setText("Enter info to add new team");
    }

    @Override
    public void onSceneDisplayed() {
        about(null);
    }
    @FXML
    void saveTeam(ActionEvent event) {
        // First check if there's a team to save
        if (team == null) {
            statusLabelL.setTextFill(Color.RED);
            statusLabelL.setText("No team data to save. Please add a team first.");
            return;
        }

        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Save Team");
        confirmDialog.setHeaderText("Team saved successfully");
        confirmDialog.setContentText("What would you like to do next?");

        ButtonType addAnotherButton = new ButtonType("Add Another Team");
        ButtonType returnToMainButton = new ButtonType("Return to Main Page");

        confirmDialog.getButtonTypes().setAll(addAnotherButton, returnToMainButton);

        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (result.get() == addAnotherButton) {
            // Clear the form for a new team
            teamName.clear();
            conference.clear();
            league.clear();
            wins.clear();
            losses.clear();
            pointsScored.clear();
            pointsAllowed.clear();
            team = null;
            statusLabelL.setText("Enter information for a new team");
        } else {
            // Return to main page
            sceneManager.switchToScene("Main");
        }
    }
}