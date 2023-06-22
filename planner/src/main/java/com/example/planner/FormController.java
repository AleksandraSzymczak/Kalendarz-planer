package com.example.planner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.time.LocalDate;

public class FormController {
    @FXML
    private TextField opis;
    @FXML
    private TextField tytul;
    @FXML
    private Label date;
    @FXML
    private Button addEvent;
    private LocalDate Formdate;
    private HelloController helloController;
    public FormController(LocalDate Formdate, HelloController helloController) {

        this.Formdate = Formdate;
        this.helloController = helloController;
    }

    public void initialize() {
        date.setText(Formdate.toString());
        addEvent.setOnAction(this::handleAddEvent);
    }
    private void handleAddEvent(ActionEvent event) {
        String opisText = opis.getText();
        String tytulText = tytul.getText();
        Event event1 = new Event(Formdate, opisText, tytulText);
        event1.saveToDatabase();
        this.helloController.cleanCalendar();
        this.helloController.rysujKalendarz();
    }
}
