module org.example.demo1001 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;


    opens org.example.demo1001 to javafx.fxml;
    exports org.example.demo1001;
    exports org.example.demo1001.controller;
    opens org.example.demo1001.controller to javafx.fxml;
}