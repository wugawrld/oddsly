package rw.app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import rw.data.Team;
import rw.shell.Main;

import java.util.Objects;

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
                        statusLabelL.setText("Bet added successfully!");
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
    }

    @Override
    public void onSceneDisplayed() {

    }
}