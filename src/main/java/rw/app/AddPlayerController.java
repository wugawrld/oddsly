package rw.app;

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
    private Button newPlayerButton;

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
                || assistsField.getText().isEmpty();
    }

    @FXML
    void addNewPlayer(ActionEvent event) {
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
                        statusLabelL.setTextFill(Color.GREEN);
                        statusLabelL.setText(String.format("%s added successfully!", playerName.getText()));

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
                    MainController.addNewPlayer(player);
                    statusLabelL.setTextFill(Color.GREEN);
                    statusLabelL.setText(String.format("%s added successfully!", playerName.getText()));

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
            statusLabelL.setText(String.format("Failed to parse double Points Per Game from %s%n or Assists Per Game from %s", pointsField.getText(), assistsField.getText()));
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

    }
}