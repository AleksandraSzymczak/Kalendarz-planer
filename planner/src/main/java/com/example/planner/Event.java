package com.example.planner;

import java.sql.*;
import java.time.LocalDate;

public class Event {
    private LocalDate formDate;
    private String opis;
    private String tytul;

    public Event(LocalDate formDate, String opis, String tytul) {
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
            // Assuming the 'events' table exists in the database with appropriate columns
            String sql = "INSERT INTO events (date, opis, tytul) VALUES (?, ?, ?)";

            Event event = new Event(formDate, "Event description", "Event title");

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setDate(1, java.sql.Date.valueOf(event.getFormDate()));
                statement.setString(2, event.getOpis());
                statement.setString(3, event.getTytul());

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

    public void getEventsForDate(LocalDate date) {
        String url = "jdbc:mysql://localhost:3306/calendar";
        String username = "root";
        String password = "1234";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String sql = "SELECT * FROM events WHERE date = ?";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setDate(1, java.sql.Date.valueOf(date));

                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    LocalDate eventDate = resultSet.getDate("date").toLocalDate();
                    String description = resultSet.getString("opis");
                    String title = resultSet.getString("tytul");

                    System.out.println("Event Date: " + eventDate);
                    System.out.println("Description: " + description);
                    System.out.println("Title: " + title);
                    System.out.println("-----------------------------");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
    @Override
    public String toString() {
        return "CalenderEvent{" +
                "date=" + formDate +
                ", Tytul='" + tytul + '\'' +
                ", Opis=" + opis +
                '}';
    }
}
