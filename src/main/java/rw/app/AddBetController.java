package rw.app;

/**
 * CPSC 233 Project AddBetController ...
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
import rw.data.Bet;
import rw.enums.BetType;
import rw.enums.BetOutcome;
import rw.shell.Main;
import java.time.LocalDate;
import java.util.Optional;

// AddBetController class that implements interface SceneController. Used to control AddBet scene functionality.
public class AddBetController implements SceneController {
    // initialize Bet being created and keep track of betCounter for BetID
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
    // Creates a popup alert explaining how to add a bet and gives an example.
    void about(ActionEvent event) {
        // Create new alert with appropriate title and header.
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Add Bet");

        // Create a new string buffer to allow "gluing" of multi line strings together
        StringBuffer stringBuffer = new StringBuffer();
        // Give instructions for add bet and an example.
        stringBuffer.append("Instructions for Add Bet: \n");
        stringBuffer.append("Select the Bet Type and date of the game you are betting on. Then fill out the " +
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

        // Add contents to alert.
        alert.setContentText(stringBuffer.toString());

        // Add an OK button to take down pop up.
        ButtonType button = ButtonType.OK;
        alert.getButtonTypes().setAll(button);

        alert.showAndWait();
    }

    @FXML
    // Close AddBet scene and switch to Main scene if close is chosen from menu.
    void close(ActionEvent event) {
        sceneManager.switchToScene("Main");
    }

    // Check if the input for league is valid (NHL / NBA)
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

    // Check if the input for teams is valid (team in NHL / NBA)
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

    // Checks if any field is empty
    private Boolean fieldsEmpty() {
        return amountWagered.getText().isEmpty() || odds.getText().isEmpty()
                || team1.getText().isEmpty() || team2.getText().isEmpty()
                || league.getText().isEmpty() || gameDate.getValue() == null;
    }

    @FXML
    // Creates a new bet based on user input.
    void createNewBet(ActionEvent event) {
        try {
            // set status label to empty.
            System.out.println("createNewBet method called");
            statusLabelL.setTextFill(Color.BLACK);
            statusLabelL.setText("");

            // if any field is empty display to user that they must complete all fields.
            if (fieldsEmpty()) {
                statusLabelL.setTextFill(Color.RED);
                statusLabelL.setText("You must complete all fields before adding a new bet");
                return;
            }

            // try parsing double from wager and odds.
            double betAmount = Double.parseDouble(amountWagered.getText());
            double multiplier = Double.parseDouble(odds.getText());
            String date = gameDate.getValue().toString();
            BetType betType;

            // determine what bet type was selected and set value to appropriate type.
            if (moneyLineButton.isSelected()) {
                betType = BetType.MONEYLINE;
            } else if (overUnderButton.isSelected()) {
                betType = BetType.OVER_UNDER;
            } else {
                betType = BetType.POINT_SPREAD;
            }

            // create a unique bet ID based on betCounter numeric value
            String betID = "bet" + betCounter;
            // set all variable from their corresponding input
            String leagueBet = league.getText();
            String homeTeam = team1.getText();
            String awayTeam = team2.getText();
            BetOutcome betOutcome = BetOutcome.PENDING;

            // Check in order if league is correct, home team is valid, away team is valid. Else display appropriate
            // error message to user.
            if (checkLeague(leagueBet)) {
                if (checkTeam(homeTeam)) {
                    if (checkTeam(awayTeam)) {
                        // if all checks pass create new Bet from user inputs
                        bet = new Bet(betID, date, homeTeam, awayTeam, betType, betAmount, multiplier, betOutcome, leagueBet);
                        // if bet does not already exist add new Bet to bets and increase bet counter
                        if (!MainController.checkBets(bet)) {
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
                            statusLabelL.setText("Bet already exists, create new bet");
                        }
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
            // if incorrect data type is used display to user error message.
            statusLabelL.setTextFill(Color.RED);
            statusLabelL.setText(String.format("Failed to parse double wager from %s or multiplier from %s", amountWagered.getText(), odds.getText()));
            System.out.println("NumberFormatException in createNewBet: " + e.getMessage());
        } catch (Exception e) {
            // if other error is caught display generic error message to user.
            statusLabelL.setTextFill(Color.RED);
            statusLabelL.setText("An error occurred while creating the bet");
            System.out.println("Exception in createNewBet: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // create and set instance of scene manager to allow for switching scenes.
    private SceneManager sceneManager;

    @Override
    public void setSceneManager(SceneManager manager) {
        this.sceneManager = manager;
    }

    @Override
    // initialization of the AddBet scene
    public void initialize() {
        System.out.println("AddBetController initialize() called");

        // set status labels to empty / info message
        statusLabelL.setTextFill(Color.BLACK);
        statusLabelL.setText("");

        statusLabelR.setTextFill(Color.BLACK);
        statusLabelR.setText("Enter info to create new bet.");

        // put the radio buttons for bet type into their own toggle group to avoid multi selecting
        ToggleGroup toggleGroup = new ToggleGroup();
        moneyLineButton.setToggleGroup(toggleGroup);
        overUnderButton.setToggleGroup(toggleGroup);
        pointSpreadButton.setToggleGroup(toggleGroup);

        // auto select money line bet type.
        moneyLineButton.setSelected(true);

        // disable date selection for any date prior to today's date. Bet must be for a future game.
        gameDate.setDayCellFactory(dp -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                setDisable(empty || date.isBefore(today));}
        });

        // Set default value to today
        gameDate.setValue(LocalDate.now());
    }

    @Override
    // onSceneDisplayed gives priority to what is shown when AddBet scene is launched
    public void onSceneDisplayed() {
        System.out.println("AddBetController onSceneDisplayed() called");

        // Clear all form fields
        clearBetForm();

        // Reset labels
        statusLabelL.setTextFill(Color.BLACK);
        statusLabelL.setText("");

        statusLabelR.setTextFill(Color.BLACK);
        statusLabelR.setText("Enter info to create new bet.");

        // Open about popup.
        about(null);
    }

    // clears all fields from AddBet scene.
    private void clearBetForm() {
        team1.clear();
        team2.clear();
        amountWagered.clear();
        odds.clear();
        league.clear();
        gameDate.setValue(LocalDate.now());

        // Set default bet type
        moneyLineButton.setSelected(true);
        overUnderButton.setSelected(false);
        pointSpreadButton.setSelected(false);

        // Reset the bet object
        bet = null;
    }

    @FXML
    // saves bet
    void saveBet(ActionEvent event) {
        try {
            System.out.println("saveBet method called");

            // First check if there's a bet to save
            if (bet == null) {
                statusLabelL.setTextFill(Color.RED);
                statusLabelL.setText("No bet data to save. Please create a bet first.");
                return;
            }

            // Give alert confirming that bet was saved.
            Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDialog.setTitle("Save Bet");
            confirmDialog.setHeaderText("Bet saved successfully");
            confirmDialog.setContentText("What would you like to do next?");

            ButtonType addAnotherButton = new ButtonType("Add Another Bet");
            ButtonType returnToMainButton = new ButtonType("Return to Main Page");

            confirmDialog.getButtonTypes().setAll(addAnotherButton, returnToMainButton);

            Optional<ButtonType> result = confirmDialog.showAndWait();
            if (result.get() == addAnotherButton) {
                // clear the form for a new bet
                clearBetForm();
                statusLabelL.setText("Enter information for a new bet");
            } else {
                // return to main page
                sceneManager.switchToScene("Main");
            }
        } catch (Exception e) {
            // if error is caught display message to user.
            statusLabelL.setTextFill(Color.RED);
            statusLabelL.setText("An error occurred while saving the bet");
            System.out.println("Exception in saveBet: " + e.getMessage());
            e.printStackTrace();
        }
    }
}