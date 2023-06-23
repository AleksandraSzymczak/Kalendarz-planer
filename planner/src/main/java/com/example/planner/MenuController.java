package com.example.planner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class MenuController {
    @FXML
    private Button addEvent;
    @FXML
    private Button deleteEvents;
    @FXML
    private VBox events;
    private LocalDate formDate;
    private HelloController helloController;

    public MenuController(LocalDate formDate, HelloController helloController) {
        this.formDate = formDate;
        this.helloController = helloController;
    }
    public void initialize() {
        addEvent.setOnAction(this::handleButtonAddEvent);
        deleteEvents.setOnAction(this::handleButtonDelete);
        loadEvents();
    }
    private void handleButtonAddEvent(ActionEvent event){
        try {
            FormApplication formApp = new FormApplication(formDate, this.helloController,this);
            Stage stage = new Stage();
            formApp.start(stage);
        } catch (IOException e) {
            throw new RuntimeException("Error occurred while handling button click", e);
        }
    }
    private void handleButtonDelete(ActionEvent event){
        EventCalendar.deleteEventsForDate(this.formDate);
        this.helloController.cleanCalendar();;
        this.helloController.rysujKalendarz();
    }
    public void loadEvents(){

        List<EventCalendar> eventsFromDb = EventCalendar.getEventsForDate(formDate);

        events.setSpacing(10);
        events.setPadding(new Insets(10));

        if (eventsFromDb != null) {
            for (EventCalendar eventCalendar : eventsFromDb) {
                LocalDate eventDate = eventCalendar.getFormDate();
                String description = eventCalendar.getOpis();
                String title = eventCalendar.getTytul();

                Label titleLabel = new Label(title);
                titleLabel.setStyle("-fx-font-weight: bold;");

                Label descriptionLabel = new Label(description);
                descriptionLabel.setWrapText(true);

                VBox eventContainer = new VBox(titleLabel, descriptionLabel);
                eventContainer.setSpacing(5);
                eventContainer.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-padding: 5;");
                eventContainer.setOnMouseClicked(event -> {
                    eventCalendar.deleteEventsForDateOpisTytul();
                    events.getChildren().clear();
                    loadEvents();
                    this.helloController.cleanCalendar();
                    this.helloController.rysujKalendarz();
                });
                events.getChildren().add(eventContainer);
            }
            ScrollPane scrollPane = new ScrollPane(events);
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);
        }
    }
}
