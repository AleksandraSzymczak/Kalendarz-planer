package com.example.planner;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import com.google.gson.Gson;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class Api {
    public static Map<String, String> getWeather(){
        String API_KEY = "e044b28f8b16430b816232828232106";
        String API_URL = "http://api.weatherapi.com/v1/current.json?key={api_key}&q={location}";

        String location = "Warszawa"; // Replace with your desired location
        String apiUrl = API_URL.replace("{api_key}", API_KEY).replace("{location}", location);

        HttpClient httpClient = HttpClient.newHttpClient();
        Gson gson = new Gson();
        try {
            HttpRequest getRequest = HttpRequest.newBuilder()
                    .uri(new URI(apiUrl))
                    .GET()
                    .build();
            HttpResponse<String> getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());
            System.out.println("getResponse");

            Weather weather = gson.fromJson(getResponse.body(), Weather.class);
            Map<String, String> valueMap = new HashMap<>();
            valueMap.put("Temp", String.valueOf(weather.getCurrent().getTemp_c()));
            valueMap.put("Localization", weather.getLocation().getName());
            valueMap.put("LocalTime", weather.getLocation().getLocaltime());
            valueMap.put("Img", weather.getCurrent().getCondition().getIcon());

            return valueMap;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    public static ChurchHolidays getHoliday(String year){
        String apiUrl = String.format("https://date.nager.at/Api/v2/PublicHolidays/%s/PL", year);
        HttpClient httpClient = HttpClient.newHttpClient();

        try {
            HttpRequest getRequest = HttpRequest.newBuilder()
                    .uri(new URI(apiUrl))
                    .GET()
                    .build();

            HttpResponse<String> getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());
            System.out.println(getResponse.body());

            // Parse the response using Gson
            Gson gson = new Gson();
            ChurchHoliday[] holidays = gson.fromJson(getResponse.body(), ChurchHoliday[].class);

            // Create a map of holiday information
            Map<String, String> holidayMap = new HashMap<>();
            for (ChurchHoliday holiday : holidays) {
                holidayMap.put(holiday.getDate(), holiday.getLocalName());
            }

            // Example: Get holiday for a specific date
            String desiredDate = "2023-07-06";
            String holidayName = holidayMap.get(desiredDate);
            System.out.println("Holiday on " + desiredDate + ": " + holidayName);
            return new ChurchHolidays(holidayMap);
        } catch (IOException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }
}
