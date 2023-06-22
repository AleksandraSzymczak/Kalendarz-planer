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
}