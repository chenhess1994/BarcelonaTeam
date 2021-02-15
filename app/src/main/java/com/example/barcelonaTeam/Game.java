package com.example.barcelonaTeam;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * This Class represents the entity of a single match with details of who is home team ,away team  date and time of match.
 */
public class Game {
    private String home;
    private String away;
    private LocalDate date;
    private LocalTime time;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setStrDate(String date) {
        this.date = LocalDate.parse(date);
    }

    public void setStrTime(String time) {
        this.time = LocalTime.parse(time);
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getAway() {
        return away;
    }

    public void setAway(String away) {
        this.away = away;
    }

}
