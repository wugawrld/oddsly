package rw.app;
/**
 * CPSC 233 Project Main class ...
 * @author Jarod Rideout, Risha Vaghani, Sardar Waheed
 * @email jarod.rideout@ucalgary.ca, risha.vaghani@ucalgary.ca, sardar.waheed@ucalgary.ca
 * @tutorial Tutorial 1 Tuesdays 11:00 - 13:00
 * @version 3.0
 */

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SceneManager {
    // Initialize variables
    private Stage stage;
    private Map<String, Scene> scenes = new HashMap<>();
    private Map<String, Object> controllers = new HashMap<>();


    public void setMainStage(Stage stage) {
        this.stage = stage;
    }

    // addScene has two inputs: String name of scene and String fxml pathway (Example: "Main", "/rw/app/Main.fxml")
    public void addScene(String name, String fxmlPath) throws IOException {
        // Similar to a normal scene setup, create a loader with a fxmlPath
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
        // Create a scene using the fxmlPath
        Scene scene = new Scene(fxmlLoader.load());
        // Add new scene to scenes HashMap
        scenes.put(name, scene);

        // Add paired controller to controllers HashMap
        Object controller = fxmlLoader.getController();
        controllers.put(name, controller);

        // If controller is of correct structure (same as SceneController interface), initialize the scene.
        if (controller instanceof SceneController) {
            ((SceneController) controller).setSceneManager(this);
            ((SceneController) controller).initialize();
        }
    }

    // swtichToScene is a method that changes the scene to another scene of "name".
    public void switchToScene(String name) {
        if (scenes.containsKey(name)) {
            stage.setScene(scenes.get(name));

            // Call onSceneDisplayed if implemented
            Object controller = controllers.get(name);
            if (controller instanceof SceneController) {
                ((SceneController) controller).onSceneDisplayed();
            }
        } else {
            System.err.println("Scene not found: " + name);
        }
    }
}