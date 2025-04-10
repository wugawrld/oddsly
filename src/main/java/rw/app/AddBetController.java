package rw.app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import rw.data.Bet;
import rw.enums.BetType;
import rw.shell.Main;
import java.time.LocalDate;

public class AddBetController implements SceneController {
    private static Bet bet;
    private static int betCounter = 1;

    @FXML
    private TextField amountWagered;

    @FXML
    private DatePicker gameDate;

    @FXML
    private TextField league;

    @FXML
    private RadioButton moneyLineButton;

    @FXML
    private Button newBet;

    @FXML
    private TextField odds;

    @FXML
    private RadioButton overUnderButton;

    @FXML
    private RadioButton pointSpreadButton;

    @FXML
    private Label statusLabelL;

    @FXML
    private Label statusLabelR;

    @FXML
    private TextField team1;

    @FXML
    private TextField team2;

    @FXML
    private Font x1;

    @FXML
    private Color x2;

    @FXML
    private Font x3;

    @FXML
    private Color x4;

    @FXML
    void close(ActionEvent event) {
        sceneManager.switchToScene("Main");
    }

    private Boolean checkLeague(String input) {
        // Checks if input is a part of leagueList.
        for (String league : Main.leagueList) {
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
            for (String team : Main.nbaTeams) {
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
    void createNewBet(ActionEvent event) {
        statusLabelL.setTextFill(Color.BLACK);
        statusLabelL.setText("");
        try {
            double betAmount = Double.parseDouble(amountWagered.getText());
            double multiplier = Double.parseDouble(odds.getText());
            String date = gameDate.getValue().toString();
            BetType betType;

            if (moneyLineButton.isSelected()) {
                betType = BetType.MONEYLINE;
            } else if (overUnderButton.isSelected()) {
                betType = BetType.OVER_UNDER;
            }
            else {
                betType = BetType.POINT_SPREAD;
            }
            String betID = "bet"+betCounter;
            String leagueBet = league.getText();
            String homeTeam = team1.getText();
            String awayTeam = team2.getText();
            if (checkLeague(leagueBet)) {
                if (checkTeam(homeTeam)) {
                    if (checkTeam(awayTeam)) {
                        bet = new Bet(betID, date, homeTeam, awayTeam, betType, betAmount, multiplier, null, leagueBet);
                        MainController.addNewBet(bet);
                        betCounter++;
                        statusLabelL.setTextFill(Color.GREEN);
                        statusLabelL.setText("Bet added successfully!");
                    } else {
                        statusLabelL.setTextFill(Color.RED);
                        statusLabelL.setText(String.format("Invalid Away Team: %s, for league %s", team2.getText(), league.getText()));
                    }
                } else {
                    statusLabelL.setTextFill(Color.RED);
                    statusLabelL.setText(String.format("Invalid Home Team: %s, for league %s", team1.getText(), league.getText()));
                }
            } else {
                statusLabelL.setTextFill(Color.RED);
                statusLabelL.setText(String.format("Invalid league: %s", league.getText()));
            }
            checkTeam(team1.getText());

        } catch (NumberFormatException e) {
            statusLabelL.setTextFill(Color.RED);
            statusLabelL.setText(String.format("Failed to parse double wager from %s or multiplier from %s", amountWagered.getText(), odds.getText()));
        }
    }

    private SceneManager sceneManager;

    @Override
    public void setSceneManager(SceneManager manager) {
        this.sceneManager = manager;
    }

    @Override
    public void initialize() {
        statusLabelL.setTextFill(Color.BLACK);
        statusLabelL.setText("");

        statusLabelR.setTextFill(Color.BLACK);
        statusLabelR.setText("Enter info to create new bet.");

        ToggleGroup toggleGroup = new ToggleGroup();
        moneyLineButton.setToggleGroup(toggleGroup);
        overUnderButton.setToggleGroup(toggleGroup);
        pointSpreadButton.setToggleGroup(toggleGroup);

        moneyLineButton.setSelected(true);

        gameDate.setDayCellFactory(dp -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                setDisable(empty || date.isBefore(today));
            }
        });

        // Set default value to today
        gameDate.setValue(LocalDate.now());
    }

    @Override
    public void onSceneDisplayed() {


    }
}