package com.example.planner.controllers;

import com.example.planner.database.EventCalendar;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDate;

public class FormController {
    @FXML
    private TextArea opis;
    @FXML
    private TextField tytul;
    @FXML
    private Label date;
    @FXML
    private Button addEvent;
    private LocalDate Formdate;
    private HelloController helloController;
    private MenuController menuController;
    public FormController(LocalDate Formdate, HelloController helloController, MenuController menuController) {

        this.Formdate = Formdate;
        this.helloController = helloController;
        this.menuController = menuController;
    }

    public void initialize() {
        date.setText(Formdate.toString());
        addEvent.setOnAction(this::handleAddEvent);
    }
    private void handleAddEvent(ActionEvent event) {
        String opisText = opis.getText();
        String tytulText = tytul.getText();
        EventCalendar event1 = new EventCalendar(Formdate, opisText, tytulText);
        event1.saveToDatabase();
        this.menuController.loadEvents();
        this.helloController.cleanCalendar();
        this.helloController.rysujKalendarz();
    }
}
