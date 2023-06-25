module com.example.planner {

    requires javafx.controls;
    requires javafx.fxml;
            
            requires com.dlsc.formsfx;
                    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires com.google.gson;
    requires java.net.http;

    opens com.example.planner to javafx.fxml, com.google.gson;
    exports com.example.planner;
    exports com.example.planner.remote;
    opens com.example.planner.remote to com.google.gson, javafx.fxml;
    exports com.example.planner.data;
    opens com.example.planner.data to com.google.gson, javafx.fxml;
    exports com.example.planner.controllers;
    opens com.example.planner.controllers to com.google.gson, javafx.fxml;
    exports com.example.planner.application;
    opens com.example.planner.application to com.google.gson, javafx.fxml;
    exports com.example.planner.database;
    opens com.example.planner.database to com.google.gson, javafx.fxml;
}