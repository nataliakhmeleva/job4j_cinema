package ru.job4j.cinema.service;

import ru.job4j.cinema.dto.FilmSessionDto;
import ru.job4j.cinema.model.FilmSession;

import java.util.Collection;
import java.util.Optional;

public interface FilmSessionService {
    Optional<FilmSessionDto> findByIdDto(int id);

    Optional<FilmSession> findById(int id);

    Collection<FilmSessionDto> findAll();
}
