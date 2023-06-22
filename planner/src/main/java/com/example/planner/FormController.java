package com.example.planner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
    public FormController(LocalDate Formdate) {
        this.Formdate = Formdate;
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
    }
}
