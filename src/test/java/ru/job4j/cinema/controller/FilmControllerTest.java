package ru.job4j.cinema.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.service.FilmService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FilmControllerTest {
    private FilmService filmService;
    private FilmController filmController;

    @BeforeEach
    public void initServices() {
        filmService = mock(FilmService.class);
        filmController = new FilmController(filmService);
    }

    /**
     * Метод проверяет запрос на получение списка фильмов, затем переходит на страницу со списком фильмов.
     */
    @Test
    public void whenRequestFilmListPageThenGetPageWithFilms() {
        var film1 = new FilmDto(1, "Аватар: Путь воды", "Аватар: Путь воды......", 2022,
                12, 192, "Фантастика", 1);
        var film2 = new FilmDto(2, "Изумительный Морис", "У кота Мориса есть два таланта......", 2022,
                6, 93, "Мультфильм", 2);
        var expectedFilms = List.of(film1, film2);
        when(filmService.findAll()).thenReturn(expectedFilms);

        var model = new ConcurrentModel();
        var view = filmController.getAll(model);
        var actualFilms = model.getAttribute("films");

        assertThat(view).isEqualTo("films/list");
        assertThat(actualFilms).isEqualTo(expectedFilms);
    }

    /**
     * Метод проверяет поиск на странице со списком фильмов выбранного фильма,
     * затем переходит по ID на страницу с информацией об этом фильме.
     */
    @Test
    public void whenFindFilmByIdOnPageWithFilmsThenGetPageWithThisFilm() {
        var film1 = new FilmDto(1, "Аватар: Путь воды", "Аватар: Путь воды......", 2022,
                12, 192, "Фантастика", 1);
        var film2 = new FilmDto(2, "Изумительный Морис", "У кота Мориса есть два таланта......", 2022,
                6, 93, "Мультфильм", 2);
        var expectedFilms = List.of(film1, film2);
        when(filmService.findAll()).thenReturn(expectedFilms);
        when(filmService.findById(1)).thenReturn(Optional.of(film1));


        var model = new ConcurrentModel();
        var view = filmController.getInformPage(model, 1);
        var actualFilms = model.getAttribute("film");


        assertThat(view).isEqualTo("films/info");
        assertThat(actualFilms).isEqualTo(film1);
    }
}