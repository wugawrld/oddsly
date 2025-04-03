package rw.app;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static final String version = "1.0";

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage stage) throws IOException {

        SceneManager sceneManager = new SceneManager();
        sceneManager.setMainStage(stage);
        sceneManager.addScene("Main", "/rw/app/Main.fxml");
        sceneManager.addScene("AddBet", "/rw/app/AddBet.fxml");
        sceneManager.addScene("AddPlayer", "/rw/app/AddPlayer.fxml");
        sceneManager.addScene("AddTeam", "/rw/app/AddTeam.fxml");
        // scene parameters are set to 1200 x 750 and scene is given title "Sports Betting Tracker v1.0".
        stage.setTitle("Sports Betting Tracker v1.0");
        sceneManager.switchToScene("Main");
        stage.show();
    }
}
