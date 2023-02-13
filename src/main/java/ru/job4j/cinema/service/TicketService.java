package ru.job4j.cinema.service;

import ru.job4j.cinema.model.Hall;
import ru.job4j.cinema.model.Ticket;

import java.util.List;
import java.util.Optional;

public interface TicketService {
    Optional<Ticket> save(Ticket ticket);

    List<Integer> getRows(Optional<Hall> hall);

    List<Integer> getPlaces(Optional<Hall> hall);
}
