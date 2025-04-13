package rw.app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import rw.data.Bet;
import rw.enums.BetType;
import rw.enums.BetOutcome;
import rw.shell.Main;
import java.time.LocalDate;
import java.util.Optional;

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
    private Button saveButton;

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
    void about(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Add Bet");
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Instructions for Add Bet: \n");
        stringBuffer.append("Select the Bet Type and date of the game you are betting on. Then fill out the" +
                "information for league, home / away teams, wager, and multiplier.\n");
        stringBuffer.append("\nExample:\n");
        stringBuffer.append("*MoneyLine Selected*\n");
        stringBuffer.append("Game Date: 2025-04-13\n");
        stringBuffer.append("League: NHL\n");
        stringBuffer.append("Home Team: Calgary Flames\n");
        stringBuffer.append("Away Team: San Jose Sharks\n");
        stringBuffer.append("Wager: 27.33\n");
        stringBuffer.append("Odds Multiplier: 1.37\n");
        stringBuffer.append("\nPlease use the following information before entering your odds multiplier:" +
                "\nFor Positive Odds: (|Odds| / 100) + 1" +
                "\nFor Negative Odds: (100 / |Odds|) + 1");

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setMinHeight(500);
        dialogPane.setMinWidth(350);

        alert.setContentText(stringBuffer.toString());

        ButtonType button = ButtonType.OK;
        alert.getButtonTypes().setAll(button);

        alert.showAndWait();
    }

    @FXML
    void close(ActionEvent event) {
        sceneManager.switchToScene("Main");
    }

    private Boolean checkLeague(String input) {
        // Checks if input is a part of leagueList.
        try {
            for (String league : Main.leagueList) {
                if (league.equalsIgnoreCase(input)) {
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println("Error in checkLeague: " + e.getMessage());
            // if there's an error, we'll return false for safety
        }
        return false;
    }

    private Boolean checkTeam(String input) {
        try {
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
        } catch (Exception e) {
            System.out.println("Error in checkTeam: " + e.getMessage());
        }
        return false;
    }

    private Boolean fieldsEmpty() {
        return amountWagered.getText().isEmpty() || odds.getText().isEmpty()
                || team1.getText().isEmpty() || team2.getText().isEmpty()
                || league.getText().isEmpty();
    }

    @FXML
    void createNewBet(ActionEvent event) {
        try {
            System.out.println("createNewBet method called");
            statusLabelL.setTextFill(Color.BLACK);
            statusLabelL.setText("");

        if (fieldsEmpty()) {
            statusLabelL.setTextFill(Color.RED);
            statusLabelL.setText("You must complete all fields before adding a new bet");
            return;
        }

            double betAmount = Double.parseDouble(amountWagered.getText());
            double multiplier = Double.parseDouble(odds.getText());
            String date = gameDate.getValue().toString();
            BetType betType;

            if (moneyLineButton.isSelected()) {
                betType = BetType.MONEYLINE;
            } else if (overUnderButton.isSelected()) {
                betType = BetType.OVER_UNDER;
            } else {
                betType = BetType.POINT_SPREAD;
            }
            String betID = "bet" + betCounter;
            String leagueBet = league.getText();
            String homeTeam = team1.getText();
            String awayTeam = team2.getText();
            BetOutcome betOutcome = BetOutcome.PENDING;

            if (checkLeague(leagueBet)) {
                if (checkTeam(homeTeam)) {
                    if (checkTeam(awayTeam)) {
                        bet = new Bet(betID, date, homeTeam, awayTeam, betType, betAmount, multiplier, betOutcome, leagueBet);
                        MainController.addNewBet(bet);
                        betCounter++;

                        // show success notification
                        statusLabelL.setTextFill(Color.GREEN);
                        statusLabelL.setText("Bet created successfully! Click Save to Continue.");

                        //show a notification dialog
                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                        successAlert.setTitle("Success");
                        successAlert.setHeaderText(null);
                        successAlert.setContentText("Created bet successfully!");
                        successAlert.showAndWait();

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
        } catch (NumberFormatException e) {
            statusLabelL.setTextFill(Color.RED);
            statusLabelL.setText(String.format("Failed to parse double wager from %s or multiplier from %s", amountWagered.getText(), odds.getText()));
            System.out.println("NumberFormatException in createNewBet: " + e.getMessage());
        } catch (Exception e) {
            statusLabelL.setTextFill(Color.RED);
            statusLabelL.setText("An error occurred while creating the bet");
            System.out.println("Exception in createNewBet: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private SceneManager sceneManager;

    @Override
    public void setSceneManager(SceneManager manager) {
        this.sceneManager = manager;
    }

    @Override
    public void initialize() {
        System.out.println("AddBetController initialize() called");

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
        System.out.println("AddBetController onSceneDisplayed() called");
        about(null);
    }

    @FXML
    void saveBet(ActionEvent event) {
        try {
            System.out.println("saveBet method called");

            // First check if there's a bet to save
            if (bet == null) {
                statusLabelL.setTextFill(Color.RED);
                statusLabelL.setText("No bet data to save. Please create a bet first.");
                return;
            }

            Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDialog.setTitle("Save Bet");
            confirmDialog.setHeaderText("Bet saved successfully");
            confirmDialog.setContentText("What would you like to do next?");

            ButtonType addAnotherButton = new ButtonType("Add Another Bet");
            ButtonType returnToMainButton = new ButtonType("Return to Main Page");

            confirmDialog.getButtonTypes().setAll(addAnotherButton, returnToMainButton);

            Optional<ButtonType> result = confirmDialog.showAndWait();
            if (result.get() == addAnotherButton) {
                // Clear the form for a new bet
                team1.clear();
                team2.clear();
                amountWagered.clear();
                odds.clear();
                league.clear();
                gameDate.setValue(LocalDate.now());
                moneyLineButton.setSelected(true);
                bet = null;
                statusLabelL.setText("Enter information for a new bet");
            } else {
                // Return to main page
                sceneManager.switchToScene("Main");
            }
        } catch (Exception e) {
            statusLabelL.setTextFill(Color.RED);
            statusLabelL.setText("An error occurred while saving the bet");
            System.out.println("Exception in saveBet: " + e.getMessage());
            e.printStackTrace();
        }
    }
}