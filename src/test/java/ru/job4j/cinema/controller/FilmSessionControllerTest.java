package ru.job4j.cinema.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.cinema.dto.FilmSessionDto;
import ru.job4j.cinema.service.FilmSessionService;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FilmSessionControllerTest {
    private FilmSessionService filmSessionService;
    private FilmSessionController filmSessionController;

    @BeforeEach
    public void initServices() {
        filmSessionService = mock(FilmSessionService.class);
        filmSessionController = new FilmSessionController(filmSessionService);
    }

    /**
     * Метод проверяет запрос на получение расписания сеансов, затем переходит на страницу с расписанием.
     */
    @Test
    public void whenRequestFilmSessionListPageThenGetPageWithFilmSessions() {
        var created = LocalDateTime.parse("2023-03-01T22:00:00");
        var filmSession1 = new FilmSessionDto(1, "Аватар: Путь воды", "Красный зал", created,
                "300 руб.", 1);
        var filmSession2 = new FilmSessionDto(2, "Изумительный Морис", "Синий зал", created,
                "100 руб.", 1);
        var expectedFilmSessions = List.of(filmSession1, filmSession2);
        when(filmSessionService.findAll()).thenReturn(expectedFilmSessions);

        var model = new ConcurrentModel();
        var view = filmSessionController.getAll(model);
        var actualFilmSessions = model.getAttribute("filmSessions");

        assertThat(view).isEqualTo("filmSessions/list");
        assertThat(actualFilmSessions).isEqualTo(expectedFilmSessions);
    }
}