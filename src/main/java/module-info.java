module mvh.app {
    requires javafx.fxml;
    requires javafx.web;
    requires java.net.http;
    requires com.google.gson;
    opens rw.app to javafx.fxml, com.google.gson;
    exports rw.app;
    exports rw.data;
    exports rw.enums;
    exports rw.shell;
    exports rw.util;
}