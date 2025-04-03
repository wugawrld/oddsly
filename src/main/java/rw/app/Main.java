package rw.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static final String version = "1.0";

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Main.fxml"));
        // scene parameters are set to 1200 x 750 and scene is given title "Sports Betting Tracker v1.0".
        Scene scene = new Scene(fxmlLoader.load(), 1200, 750);
        stage.setTitle("Sports Betting Tracker v1.0");
        stage.setScene(scene);
        stage.show();
    }
}
