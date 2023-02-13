package ru.job4j.cinema.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.FilmSessionDto;
import ru.job4j.cinema.model.FilmSession;
import ru.job4j.cinema.repository.FilmRepository;
import ru.job4j.cinema.repository.FilmSessionRepository;
import ru.job4j.cinema.repository.HallRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@ThreadSafe
@Service
public class SimpleFilmSessionService implements FilmSessionService {
    private final FilmRepository filmRepository;
    private final FilmSessionRepository filmSessionRepository;
    private final HallRepository hallRepository;

    public SimpleFilmSessionService(FilmRepository sql2oFilmRepository, FilmSessionRepository sql2oFilmSessionRepository, HallRepository sql2oHallRepository) {
        this.filmRepository = sql2oFilmRepository;
        this.filmSessionRepository = sql2oFilmSessionRepository;
        this.hallRepository = sql2oHallRepository;
    }

    @Override
    public Optional<FilmSessionDto> findById(int id) {
        var session = filmSessionRepository.findById(id);
        if (session.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(new FilmSessionDto(session.get().getId(), filmRepository.findById(session.get().getFilmId()).get().getName(),
                hallRepository.findById(session.get().getHallsId()).get().getName(), session.get().getStartTime(),
                session.get().getPrice(), session.get().getHallsId()));
    }

    @Override
    public Collection<FilmSessionDto> findAll() {
        Collection<FilmSessionDto> list = new ArrayList<>();
        for (FilmSession session : filmSessionRepository.findAll()) {
            var filmSession = new FilmSessionDto(session.getId(), filmRepository.findById(session.getFilmId()).get().getName(),
                    hallRepository.findById(session.getHallsId()).get().getName(), session.getStartTime(), session.getPrice(),
                    session.getHallsId());
            list.add(filmSession);
        }
        return list;
    }
}
