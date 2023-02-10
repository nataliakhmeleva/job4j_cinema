package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;

import java.util.Collection;
import java.util.Optional;

public interface TicketRepository {

    Optional<Ticket> save(Ticket ticket);

    boolean findByRowNumberAndPlaceNumber(int sessionId, int rowNumber, int placeNumber);

    boolean deleteById(int id);

    Collection<Ticket> findAll();

    Optional<Ticket> findById(int id);
}
