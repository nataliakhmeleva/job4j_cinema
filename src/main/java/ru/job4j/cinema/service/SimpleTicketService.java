package ru.job4j.cinema.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Hall;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.repository.TicketRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@ThreadSafe
@Service
public class SimpleTicketService implements TicketService {
    private final TicketRepository ticketRepository;

    public SimpleTicketService(TicketRepository sql2oTicketRepository) {
        this.ticketRepository = sql2oTicketRepository;
    }

    @Override
    public Optional<Ticket> save(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    @Override
    public List<Integer> getRows(Optional<Hall> hall) {
        return IntStream.rangeClosed(1, hall.get().getRowCount()).boxed().toList();
    }

    @Override
    public List<Integer> getPlaces(Optional<Hall> hall) {
        return IntStream.rangeClosed(1, hall.get().getPlaceCount()).boxed().toList();
    }
}
