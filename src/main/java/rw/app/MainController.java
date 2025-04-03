package rw.app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class MainController implements SceneController {

    private SceneManager sceneManager;

    @Override
    public void setSceneManager(SceneManager manager) {
        this.sceneManager = manager;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void onSceneDisplayed() {

    }

    @FXML
    void addBet(ActionEvent event) {
        sceneManager.switchToScene("AddBet");
    }

    @FXML
    void addPlayer(ActionEvent event) {
        sceneManager.switchToScene("AddPlayer");
    }

    @FXML
    void addTeam(ActionEvent event) {
        sceneManager.switchToScene("AddTeam");
    }

    @FXML
    private Font x1;

    @FXML
    private Color x2;

    @FXML
    private Font x3;

    @FXML
    private Color x4;

}
