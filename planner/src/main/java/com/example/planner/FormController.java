package com.example.planner;

import javafx.fxml.FXML;
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
    public void initialize() {
        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAA");
        getWeather();
    }
    public void getWeather(){
        //private static final
        String API_KEY = "e044b28f8b16430b816232828232106";
        String API_URL = "http://api.weatherapi.com/v1/current.json?key={api_key}&q={location}";

        String location = "Warszawa"; // Replace with your desired location
        String apiUrl = API_URL.replace("{api_key}", API_KEY).replace("{location}", location);

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Parse and process the weather data from the response
            String weatherData = response.toString();
            System.out.println(weatherData);
            opis.setText(weatherData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
