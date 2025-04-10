package rw.app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class AddPlayerController implements SceneController {

    @FXML
    private GridPane statsField;


    @FXML
    private RadioButton basketballButton;

    @FXML
    private RadioButton hockeyButton;

    @FXML
    private Label statusLabelL;

    @FXML
    private Label statusLabelR;

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

        TextField pointsField = new TextField();
        TextField reboundsField = new TextField();
        TextField assistsField = new TextField();

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

        TextField pointsField = new TextField();
        TextField assistsField = new TextField();

        pointsField.setPrefHeight(30);
        pointsField.setPrefWidth(120);
        assistsField.setPrefHeight(30);
        assistsField.setPrefWidth(120);

        statsField.add(pointsField, 0,1);
        statsField.add(assistsField, 1,1);

        statsField.setHgap(20);
        statsField.setVgap(15);
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