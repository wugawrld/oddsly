module mvh.app {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    opens rw.app to javafx.fxml;
    exports rw.app;
}