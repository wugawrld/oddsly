package rw.app;

import javafx.fxml.FXML;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class AddBetController implements SceneController {

    @FXML
    private Font x1;

    @FXML
    private Color x2;

    @FXML
    private Font x3;

    @FXML
    private Color x4;

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
}