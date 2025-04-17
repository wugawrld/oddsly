package rw.app;
/**
 * CPSC 233 Project Main class ...
 * @author Jarod Rideout, Risha Vaghani, Sardar Waheed
 * @email jarod.rideout@ucalgary.ca, risha.vaghani@ucalgary.ca, sardar.waheed@ucalgary.ca
 * @tutorial Tutorial 1 Tuesdays 11:00 - 13:00
 * @version 3.0
 */

import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;

// Main class under app. Used to add all scenes to sceneManager and launch program.
public class Main extends Application {

    public static final String version = "1.0";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {

        // create an instance of scene manager and add all scenes.
        SceneManager sceneManager = new SceneManager();
        sceneManager.setMainStage(stage);
        sceneManager.addScene("Main", "/rw/app/Main.fxml");
        sceneManager.addScene("AddBet", "/rw/app/AddBet.fxml");
        sceneManager.addScene("AddPlayer", "/rw/app/AddPlayer.fxml");
        sceneManager.addScene("AddTeam", "/rw/app/AddTeam.fxml");
        // scene is given title "Sports Betting Tracker v1.0".
        stage.setTitle("Sports Betting Tracker v1.0");
        // ensure Main scene is displayed first
        sceneManager.switchToScene("Main");
        stage.show();
    }
}
