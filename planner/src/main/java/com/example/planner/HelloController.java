package com.example.planner;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Date;
import java.util.Locale;
import java.util.Map;

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
    LocalDate currentDate;
    LocalDate realCurrentDate;

    public void initialize() {
        left.setOnAction(this::handleButtonActionLeft);
        right.setOnAction(this::handleButtonActionRight);
        currentDate = LocalDate.now();
        realCurrentDate = LocalDate.now();
        this.setCurrDateInCalendar();
        pogoda();
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(30), event -> {
            pogoda();
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        rysujKalendarz();
    }
    public void pogoda() {
        Map<String, String> apiValues = Api.getWeather();
        if(apiValues!=null){
            System.out.println(apiValues.get("Img"));
            setWeatherImage(apiValues.get("Img"));
            Platform.runLater(() -> {
                localization.setText(apiValues.get("Localization"));
                temp.setText(apiValues.get("Temp") + " °C");
                localTime.setText(apiValues.get("LocalTime"));
            });
        }
    }
    public void setWeatherImage(String imageUrl) {
        Image image = new Image("https:"+imageUrl);
        pogoda.setImage(image);
    }
    private void handleButtonActionLeft(ActionEvent event) {
        currentDate = currentDate.minusMonths(1);
        kalendarz.getChildren().clear();
        this.rysujKalendarz();
    }

    private void handleButtonActionRight(ActionEvent event) {
        currentDate = currentDate.plusMonths(1);
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
                    StackPane stackPane = new StackPane(rectangle, text);
                    stackPane.setAlignment(Pos.CENTER);
                    if(dayOfMonth==realCurrentDate.getDayOfMonth() &&
                            currentDate.getMonth()==realCurrentDate.getMonth() &&
                            currentDate.getYear() == realCurrentDate.getYear())
                    {
                        Color pastelColor = Color.rgb(173, 216, 230);
                        rectangle.setFill(pastelColor);
                    }
                    if(Event.hasEventsForDate(LocalDate.of(currentDate.getYear() , currentDate.getMonthValue() , dayOfMonth))){
                        Color pastelColor = Color.rgb(100, 150, 230);
                        rectangle.setFill(pastelColor);
                    }
                    rectangle.setOnMouseClicked(event -> handleRectangleClick(dayOfMonth));
                    kalendarz.getChildren().add(stackPane);
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