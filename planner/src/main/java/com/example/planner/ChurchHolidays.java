package com.example.planner;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class ChurchHolidays {
    public Map<String, String> holidays;

    public ChurchHolidays(Map<String, String> holidays) {
        this.holidays = holidays;
    }
    public String czyTowieto(LocalDate date){
        String dateString = date.toString();
        try {
            return holidays.get(dateString);
        } catch (Exception exception) {
            return "";
        }
    }
}
