package com.example.planner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class MenuApplication extends Application{
    private LocalDate formDate;
    public MenuApplication(LocalDate formDate) {
        this.formDate = formDate;
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FormApplication.class.getResource("form-menu.fxml"));
        MenuController controller = new MenuController(formDate);
        fxmlLoader.setController(controller);
        Parent root = fxmlLoader.load();

        //Api.getWeather();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle(formDate.toString());
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
