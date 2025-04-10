package rw.app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import java.time.LocalDate;

import java.util.Date;

public class AddBetController implements SceneController {

    @FXML
    private TextField amountWagered;

    @FXML
    private DatePicker gameDate;

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

    @FXML
    void createNewBet(ActionEvent event) {
        statusLabelL.setTextFill(Color.BLACK);
        statusLabelL.setText("");
        try {
            double betAmount = Double.parseDouble(amountWagered.getText());
            double multiplier = Double.parseDouble(odds.getText());

        } catch (NumberFormatException e) {
            statusLabelL.setTextFill(Color.RED);
            statusLabelL.setText(String.format("Failed to parse double wager from %s%n or multiplier from %s%n", amountWagered.getText(), odds.getText()));
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

        // Set up the date picker to only allow future dates
        gameDate.setDayCellFactory(dp -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                setDisable(empty || date.compareTo(today) < 0);
            }
        });

        // Set default value to today
        gameDate.setValue(LocalDate.now());
    }

    @Override
    public void onSceneDisplayed() {

    }
}