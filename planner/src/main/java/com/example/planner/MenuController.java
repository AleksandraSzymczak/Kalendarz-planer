package com.example.planner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class MenuController {
    @FXML
    private Button addEvent;
    @FXML
    private TextArea events;
    private LocalDate formDate;

    public MenuController(LocalDate formDate) {
        this.formDate = formDate;
    }
    public void initialize() {
        addEvent.setOnAction(event -> {this.handleButtonAddEvent();});
        loadEvents();
    }
    public void handleButtonAddEvent(ActionEvent event){
        try {
            FormApplication formApp = new FormApplication(formDate);
            Stage stage = new Stage();
            formApp.start(stage);
        } catch (IOException e) {
            throw new RuntimeException("Error occurred while handling button click", e);
        }
    }
    public void loadEvents(){
        List<Event> eventsFromDb = Event.getEventsForDate(formDate);
        StringBuilder eventsText = new StringBuilder();
        if(eventsFromDb != null) {
            for (Event event : eventsFromDb) {
                LocalDate eventDate = event.getFormDate();
                String description = event.getOpis();
                String title = event.getTytul();

                eventsText.append("Event Date: ").append(eventDate).append("\n");
                eventsText.append("Description: ").append(description).append("\n");
                eventsText.append("Title: ").append(title).append("\n");
                eventsText.append("-----------------------------").append("\n");
            }
        }
        events.setText(eventsText.toString());
    }
}
