package com.example.planner.application;

import com.example.planner.controllers.FormController;
import com.example.planner.controllers.HelloController;
import com.example.planner.controllers.MenuController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class FormApplication extends Application{
    private LocalDate formDate;
    private HelloController helloController;
    private MenuController menuController;
    public FormApplication(LocalDate formDate, HelloController helloController,
                           MenuController menuController) {
        this.formDate = formDate;
        this.helloController = helloController;
        this.menuController = menuController;
        System.out.println(formDate);
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FormApplication.class.getResource("/com/example/planner/form-view.fxml"));
        FormController controller = new FormController(formDate, this.helloController,
                this.menuController);
        fxmlLoader.setController(controller);
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("form");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
