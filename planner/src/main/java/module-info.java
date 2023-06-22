module com.example.planner {
    requires javafx.controls;
    requires javafx.fxml;
            
            requires com.dlsc.formsfx;
                    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.example.planner to javafx.fxml;
    exports com.example.planner;
}