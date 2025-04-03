module com.example.sportsbettrackergui {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.junit.jupiter.api;


    opens com.example.sportsbettrackergui to javafx.fxml;
    exports com.example.sportsbettrackergui;
}