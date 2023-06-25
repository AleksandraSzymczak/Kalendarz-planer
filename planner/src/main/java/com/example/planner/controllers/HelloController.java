package com.example.planner.controllers;

import com.example.planner.database.EventCalendar;
import com.example.planner.application.MenuApplication;
import com.example.planner.data.ChurchHolidays;
import com.example.planner.remote.Api;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.layout.FlowPane;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.time.LocalTime;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HelloController {
    @FXML
    private Label localTime;
    @FXML
    private Label localization;
    @FXML
    private Label temp;
    @FXML
    private Label rok;

    @FXML
    private Label miesiac;

    @FXML
    private FlowPane kalendarz;

    @FXML
    private Button left;

    @FXML
    private Button right;
    @FXML
    private ImageView pogoda;
    private ScheduledExecutorService scheduler;
    LocalDate currentDate;
    LocalDate realCurrentDate;
    private ChurchHolidays churchHolidays;
    public void initialize() {
        SetUp();
        rysujKalendarz();
    }
    public void SetUp(){
        left.setOnAction(this::handleButtonActionLeft);
        right.setOnAction(this::handleButtonActionRight);
        currentDate = LocalDate.now();
        realCurrentDate = LocalDate.now();
        this.setCurrDateInCalendar();
        this.churchHolidays = Api.getHoliday(String.valueOf(currentDate.getYear()));
        startWeatherUpdates();
    }
    public void startWeatherUpdates() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(this::pogoda, 0, 20, TimeUnit.SECONDS);
    }
    public void pogoda() {
        Map<String, String> apiValues = Api.getWeather();
        if(apiValues!=null){
            System.out.println(apiValues.get("Img"));
            LocalTime currentTime = LocalTime.now();
            Platform.runLater(() -> {
                setWeatherImage(apiValues.get("Img"));
                localization.setText(apiValues.get("Localization"));
                temp.setText(apiValues.get("Temp") + " °C");
                localTime.setText(Integer.toString(currentTime.getHour())
                        +" : "+Integer.toString(currentTime.getMinute()));
            });
        }
    }
    public void setWeatherImage(String imageUrl) {
        Image image = new Image("https:"+imageUrl);
        pogoda.setImage(image);
    }
    private void handleButtonActionLeft(ActionEvent event) {
        currentDate = currentDate.minusMonths(1);
        if(realCurrentDate.getYear() != currentDate.getYear())
        {
            this.churchHolidays = Api.getHoliday(String.valueOf(currentDate.getYear()));
        }
        kalendarz.getChildren().clear();
        this.rysujKalendarz();
    }

    private void handleButtonActionRight(ActionEvent event) {
        currentDate = currentDate.plusMonths(1);
        if(realCurrentDate.getYear() != currentDate.getYear())
        {
            this.churchHolidays = Api.getHoliday(String.valueOf(currentDate.getYear()));
        }
        kalendarz.getChildren().clear();
        this.rysujKalendarz();
    }
    public void cleanCalendar(){
        kalendarz.getChildren().clear();
    }

    public void setCurrDateInCalendar() {
        String currentYear = String.valueOf(currentDate.getYear());
        int currentMonthValue = currentDate.getMonthValue();
        String currentMonth = getPolishMonth(currentMonthValue);

        rok.setText(currentYear);
        miesiac.setText(currentMonth);
    }
    private static String getPolishMonth(int monthValue) {
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", new Locale("pl"));
        Date date = new Date();
        date.setMonth(monthValue - 1);
        return monthFormat.format(date);
    }
    public void rysujKalendarz() {
        kalendarz.setPadding(new Insets(5));
        kalendarz.setHgap(0);
        kalendarz.setVgap(0);

        int numRows = 5;
        int numColumns = 7;

        double squareSize = 90;
        setCurrDateInCalendar();
        int daysInMonth = currentDate.lengthOfMonth();

        LocalDate firstDayOfMonth = currentDate.withDayOfMonth(1);
        DayOfWeek dayOfWeek = firstDayOfMonth.getDayOfWeek();

        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numColumns; col++) {
                Rectangle rectangle = new Rectangle(squareSize, squareSize);
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setStroke(Color.BLACK);
                rectangle.setStrokeWidth(1);
                if(dayOfWeek.getValue() > row * numColumns + col + 1)//tu jeszcze sie nie zaczal nasz miesciac
                {

                    StackPane stackPane = new StackPane(rectangle);
                    stackPane.setAlignment(Pos.CENTER);

                    kalendarz.getChildren().add(stackPane);
                }
                else{//tu sie juz zaczal
                    int dayOfMonth = row * numColumns + col + 2-dayOfWeek.getValue();
                    if ( (dayOfMonth > daysInMonth)) {
                        break;
                    }
                    Text text = new Text(Integer.toString(dayOfMonth));
                    text.maxWidth(70);
                    text.setWrappingWidth(70);
                    text.setTextAlignment(TextAlignment.CENTER);
                    if(dayOfMonth==realCurrentDate.getDayOfMonth() &&
                            currentDate.getMonth()==realCurrentDate.getMonth() &&
                            currentDate.getYear() == realCurrentDate.getYear())
                    {
                        rectangle.setStrokeWidth(3);
                    }
                    if(EventCalendar.hasEventsForDate(LocalDate.of(currentDate.getYear() , currentDate.getMonthValue() , dayOfMonth))){
                        Color pastelColor = Color.rgb(100, 150, 230);
                        rectangle.setFill(pastelColor);
                    }
                    String swieto = churchHolidays.czyTowieto(LocalDate.of(currentDate.getYear() , currentDate.getMonthValue() , dayOfMonth));
                    if(swieto!=null){
                        Color pastelColor = Color.rgb(134, 208, 101);
                        rectangle.setFill(pastelColor);
                        String concatenatedText = swieto+"\n"+text.getText();
                        Text context = new Text(concatenatedText);
                        context.maxWidth(70);
                        context.setWrappingWidth(70);
                        context.setTextAlignment(TextAlignment.CENTER);
                        StackPane stackPane = new StackPane();
                        stackPane.getChildren().addAll(rectangle, context);
                        stackPane.setAlignment(Pos.CENTER);
                        rectangle.setOnMouseClicked(event -> handleRectangleClick(dayOfMonth));
                        kalendarz.getChildren().add(stackPane);
                    }
                    else{
                        StackPane stackPane = new StackPane();
                        stackPane.getChildren().addAll(rectangle, text);
                        stackPane.setAlignment(Pos.CENTER);
                        rectangle.setOnMouseClicked(event -> handleRectangleClick(dayOfMonth));
                        kalendarz.getChildren().add(stackPane);
                    }

                }
            }
        }
    }
    private void handleRectangleClick(int dayOfMonth) {
        // Handle the rectangle click event here
        System.out.println("Rectangle clicked!");
        System.out.println("Day of Month: " + dayOfMonth);
        try {
            LocalDate dateForm = LocalDate.of(currentDate.getYear() , currentDate.getMonthValue() , dayOfMonth);
            MenuApplication formApp = new MenuApplication(dateForm, this);
            Stage stage = new Stage();
            formApp.start(stage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}