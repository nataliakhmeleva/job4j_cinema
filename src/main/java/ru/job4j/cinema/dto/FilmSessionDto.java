package ru.job4j.cinema.dto;

import java.time.LocalDateTime;

public class FilmSessionDto {
    private int id;
    private String film;
    private String hall;
    private LocalDateTime startTime;
    private String price;

    public FilmSessionDto(int id, String film, String hall, LocalDateTime startTime, String price) {
        this.id = id;
        this.film = film;
        this.hall = hall;
        this.startTime = startTime;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilm() {
        return film;
    }

    public void setFilm(String film) {
        this.film = film;
    }

    public String getHall() {
        return hall;
    }

    public void setHall(String hall) {
        this.hall = hall;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
