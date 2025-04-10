module mvh.app {
    requires javafx.fxml;
    requires javafx.web;
    requires java.net.http;
    opens rw.app to javafx.fxml;
    exports rw.app;
}