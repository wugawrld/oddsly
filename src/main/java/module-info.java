module com.example.sportsbettrackergui {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.sportsbettrackergui to javafx.fxml;
    exports com.example.sportsbettrackergui;
}