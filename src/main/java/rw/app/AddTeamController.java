package rw.app;

/**
 * CPSC 233 Project AddTeamController ...
 * @author Jarod Rideout, Risha Vaghani, Sardar Waheed
 * @email jarod.rideout@ucalgary.ca, risha.vaghani@ucalgary.ca, sardar.waheed@ucalgary.ca
 * @tutorial Tutorial 1 Tuesdays 11:00 - 13:00
 * @version 3.0
 */

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

// AddTeamController class that implements interface SceneController. Used to control AddTeam scene functionality.
public class AddTeamController implements SceneController {

    // initialize Team being created
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
    // Creates a popup alert explaining how to add a team and gives an example.
    void about(ActionEvent event) {
        // Create new alert with appropriate title and header.
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Add Team");

        // Create a new string buffer to allow "gluing" of multi line strings together
        StringBuffer stringBuffer = new StringBuffer();
        // Give instructions for add team and an example.
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

        // Add contents to alert.
        alert.setContentText(stringBuffer.toString());

        // Add an OK button to take down pop up.
        ButtonType button = ButtonType.OK;
        alert.getButtonTypes().setAll(button);

        alert.showAndWait();
    }

    @FXML
    // Creates a new team based on user input.
    void addTeam(ActionEvent event) {
        try {
            // set status label to empty.
            System.out.println("addTeam method called");
            statusLabelL.setTextFill(Color.BLACK);
            statusLabelL.setText("");

            // if any field is empty display to user that they must complete all fields.
            if (fieldsEmpty()) {
                statusLabelL.setTextFill(Color.RED);
                statusLabelL.setText("You must complete all fields before adding a new team");
                return;
            }

        try {
            // set all variable from their corresponding input
            // try parsing Integer for win, loss, ps, pa
            int win = Integer.parseInt(wins.getText());
            int loss = Integer.parseInt(losses.getText());
            int ps = Integer.parseInt(pointsScored.getText());
            int pa = Integer.parseInt(pointsAllowed.getText());
            String l = league.getText();
            String c = conference.getText();
            String tName = teamName.getText();

            // Check in order if league is correct, conference is valid, team name is valid. Else display appropriate
            // error message to user.
            if (checkLeague(l)) {
                if (Objects.equals(c, "Eastern") || Objects.equals(c, "Western")) {
                    if (checkTeam(tName)) {
                        // if all checks pass create new Team from user inputs
                        team = new Team(tName, c, win, loss, ps, pa);
                        // if team does not already exist add new Team to teams
                        if (!MainController.checkTeams(tName)) {
                            MainController.addNewTeam(team);

                            // show success notification
                            statusLabelL.setTextFill(Color.GREEN);
                            statusLabelL.setText(String.format("%s added successfully! Click Save to Continue!", teamName.getText()));
                            statusLabelR.setTextFill(Color.BLACK);
                            statusLabelR.setText("Don't forget to save your data");

                            // Show success notification popup
                            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                            successAlert.setTitle("Success");
                            successAlert.setHeaderText(null);
                            successAlert.setContentText("Team created successfully!");
                            successAlert.showAndWait();
                        } else {
                            statusLabelL.setTextFill(Color.RED);
                            statusLabelL.setText(String.format("%s is already being tracked", tName));
                        }
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
            // if incorrect data type is used display to user error message.
            statusLabelL.setTextFill(Color.RED);
            statusLabelL.setText(String.format("Failed to parse integer wins from %s, losses from %s, points scored from %s, or points allowed from %s", wins.getText(), losses.getText(), pointsScored.getText(), pointsAllowed.getText()));
        }
        } catch (Exception e) {
            // if other error is caught display generic error message to user.
            statusLabelL.setTextFill(Color.RED);
            statusLabelL.setText("An error occurred while creating the team");
            System.out.println("Error in addTeam: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Checks if any field is empty
    private Boolean fieldsEmpty() {
        return wins.getText().isEmpty() || losses.getText().isEmpty()
                || pointsScored.getText().isEmpty() || pointsAllowed.getText().isEmpty()
                || teamName.getText().isEmpty() || conference.getText().isEmpty()
                || league.getText().isEmpty();
    }

    // Check if the input for league is valid (NHL / NBA)
    private Boolean checkLeague(String input) {
        // Checks if input is a part of leagueList.
        for (String league : rw.shell.Main.leagueList) {
            if (league.equalsIgnoreCase(input)) {
                return true;
            }
        }
        return false;
    }

    // Check if the input for teams is valid (team in NHL / NBA)
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
    // Close AddTeam scene and switch to Main scene if close is chosen from menu.
    void close(ActionEvent event) {
        sceneManager.switchToScene("Main");
    }

    @Override
    public void setSceneManager(SceneManager manager) {
        this.sceneManager = manager;
    }

    @Override
    // initialization of the AddTeam scene
    public void initialize() {
        // set status labels to empty / info message
        statusLabelL.setTextFill(Color.BLACK);
        statusLabelL.setText("");

        statusLabelR.setTextFill(Color.BLACK);
        statusLabelR.setText("Enter info to add new team");
    }

    @Override
    // onSceneDisplayed gives priority to what is shown when AddTeam scene is launched
    public void onSceneDisplayed() {
        // Clear all form fields
        clearTeamForm();

        // Reset labels
        statusLabelL.setTextFill(Color.BLACK);
        statusLabelL.setText("");

        statusLabelR.setTextFill(Color.BLACK);
        statusLabelR.setText("Enter info to add new team");

        // Open about popup.
        about(null);
    }

    // clears all fields from AddTeam scene.
    private void clearTeamForm() {
        teamName.clear();
        conference.clear();
        league.clear();
        wins.clear();
        losses.clear();
        pointsScored.clear();
        pointsAllowed.clear();

        // Reset the team object
        team = null;
    }

    @FXML
    // saves team
    void saveTeam(ActionEvent event) {
        try {
            System.out.println("saveTeam method called");
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
            clearTeamForm();
            statusLabelL.setText("Enter information for a new team");
        } else {
            // Return to main page
            sceneManager.switchToScene("Main");
        }
        } catch (Exception e) {
            statusLabelL.setTextFill(Color.RED);
            statusLabelL.setText("An error occurred while saving the team");
            System.out.println("Error in saveTeam: " + e.getMessage());
            e.printStackTrace();
        }
    }
}