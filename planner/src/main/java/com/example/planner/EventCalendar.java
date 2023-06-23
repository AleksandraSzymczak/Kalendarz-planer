package com.example.planner;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EventCalendar {
    private LocalDate formDate;
    private String opis;
    private String tytul;

    public EventCalendar(LocalDate formDate, String opis, String tytul) {
        this.opis = opis;
        this.tytul = tytul;
        this.formDate = formDate;
    }
    public LocalDate getFormDate() {
        return formDate;
    }

    public void setFormDate(LocalDate formDate) {
        this.formDate = formDate;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getTytul() {
        return tytul;
    }

    public void setTytul(String tytul) {
        this.tytul = tytul;
    }
    public void saveToDatabase() {
        String url = "jdbc:mysql://localhost:3306/calendar";
        String username = "root";
        String password = "1234";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String sql = "INSERT INTO events (date, opis, tytul) VALUES (?, ?, ?)";


            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setDate(1, Date.valueOf(this.formDate));
                statement.setString(2, this.opis);
                statement.setString(3, this.tytul);

                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Event saved successfully.");
                } else {
                    System.out.println("Failed to save event.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<EventCalendar> getEventsForDate(LocalDate date) {
        String url = "jdbc:mysql://localhost:3306/calendar";
        String username = "root";
        String password = "1234";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String sql = "SELECT * FROM events WHERE date = ?";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setDate(1, java.sql.Date.valueOf(date));
                List<EventCalendar> listOfEvents = new ArrayList<>();
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    LocalDate eventDate = resultSet.getDate("date").toLocalDate();
                    String description = resultSet.getString("opis");
                    String title = resultSet.getString("tytul");

                    System.out.println("Event Date: " + eventDate);
                    System.out.println("Description: " + description);
                    System.out.println("Title: " + title);
                    System.out.println("-----------------------------");
                    listOfEvents.add(new EventCalendar(eventDate,description,title));
                }
                return  listOfEvents;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static boolean hasEventsForDate(LocalDate date) {
        String url = "jdbc:mysql://localhost:3306/calendar";
        String username = "root";
        String password = "1234";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String sql = "SELECT COUNT(*) FROM events WHERE date = ?";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setDate(1, java.sql.Date.valueOf(date));

                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
    public static void deleteEventsForDate(LocalDate date) {
        String url = "jdbc:mysql://localhost:3306/calendar";
        String username = "root";
        String password = "1234";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String sql = "DELETE FROM events WHERE date = ?";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setDate(1, java.sql.Date.valueOf(date));
                int rowsAffected = statement.executeUpdate();
                System.out.println(rowsAffected + " event(s) deleted for date: " + date);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void deleteEventsForDateOpisTytul() {
        String url = "jdbc:mysql://localhost:3306/calendar";
        String username = "root";
        String password = "1234";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String sql = "DELETE FROM events WHERE date = ? AND opis = ? AND tytul = ?";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setDate(1, java.sql.Date.valueOf(this.formDate));
                statement.setString(2, this.opis);
                statement.setString(3, this.tytul);
                int rowsAffected = statement.executeUpdate();
                System.out.println(rowsAffected + " event(s) deleted for date: " + this.formDate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "CalenderEvent{" +
                "date=" + formDate +
                ", Tytul='" + tytul + '\'' +
                ", Opis=" + opis +
                '}';
    }
}
