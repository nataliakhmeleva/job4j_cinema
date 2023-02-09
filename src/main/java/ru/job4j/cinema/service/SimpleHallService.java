package ru.job4j.cinema.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Hall;
import ru.job4j.cinema.repository.HallRepository;

import java.util.Collection;
import java.util.Optional;

@ThreadSafe
@Service
public class SimpleHallService implements HallService {
    private final HallRepository hallRepository;

    public SimpleHallService(HallRepository sql2oHallRepository) {
        this.hallRepository = sql2oHallRepository;
    }

    @Override
    public Optional<Hall> findById(int id) {
        return hallRepository.findById(id);
    }

    @Override
    public Collection<Hall> findAll() {
        return hallRepository.findAll();
    }

}
