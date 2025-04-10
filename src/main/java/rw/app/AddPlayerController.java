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

        statsField.add(pointsLabel, 0, 0);
        statsField.add(reboundsLabel, 1, 0);
        statsField.add(assistsLabel, 2, 0);

        TextField pointsField = new TextField();
        TextField reboundsField = new TextField();
        TextField assistsField = new TextField();

        pointsField.prefHeight(30);
        pointsField.prefWidth(100);
        reboundsField.prefHeight(30);
        reboundsField.prefWidth(100);
        assistsField.prefHeight(30);
        assistsField.prefWidth(100);

        statsField.add(pointsField, 0,1);
        statsField.add(reboundsField, 1,1);
        statsField.add(assistsField, 2,1);

        statsField.setHgap(10);
        statsField.setVgap(5);
    }

    @FXML
    void addHockeyStats(ActionEvent event) {
        statusLabelL.setTextFill(Color.GREEN);
        statusLabelL.setText("Hockey player chosen");

        statsField.getChildren().clear();

        Label pointsLabel = new Label("Points Per Game");
        Label assistsLabel = new Label("Assists Per Game");

        statsField.add(pointsLabel, 0, 0);
        statsField.add(assistsLabel, 1, 0);

        TextField pointsField = new TextField();
        TextField assistsField = new TextField();

        pointsField.prefHeight(30);
        pointsField.prefWidth(100);
        assistsField.prefHeight(30);
        assistsField.prefWidth(100);

        statsField.add(pointsField, 0,1);
        statsField.add(assistsField, 1,1);

        statsField.setHgap(10);
        statsField.setVgap(5);
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