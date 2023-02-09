package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.model.Film;
import ru.job4j.cinema.repository.FilmRepository;
import ru.job4j.cinema.repository.GenreRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
public class SimpleFilmService implements FilmService {
    private final FilmRepository filmRepository;
    private final GenreRepository genreRepository;


    public SimpleFilmService(FilmRepository sql2oFilmRepository, GenreRepository sql2oGenreRepository) {
        this.filmRepository = sql2oFilmRepository;
        this.genreRepository = sql2oGenreRepository;

    }

    @Override
    public Optional<FilmDto> findById(int id) {
        var film = filmRepository.findById(id);
        if (film.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(new FilmDto(film.get().getId(), film.get().getName(), film.get().getDescription(),
                film.get().getYear(), film.get().getMinimalAge(), film.get().getDuration(),
                genreRepository.findById(film.get().getGenreId()).get().getName(), film.get().getFileId()));
    }

    @Override
    public Collection<FilmDto> findAll() {
        Collection<FilmDto> list = new ArrayList<>();
        for (Film film : filmRepository.findAll()) {
            var movie = new FilmDto(film.getId(), film.getName(), film.getDescription(), film.getYear(), film.getMinimalAge(), film.getDuration(),
                    genreRepository.findById(film.getGenreId()).get().getName(), film.getFileId());
            list.add(movie);
        }
        return list;
    }
}
