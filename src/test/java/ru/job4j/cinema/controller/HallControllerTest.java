package ru.job4j.cinema.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.cinema.model.Hall;
import ru.job4j.cinema.service.HallService;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HallControllerTest {
    private HallService hallService;
    private HallController hallController;

    @BeforeEach
    public void initServices() {
        hallService = mock(HallService.class);
        hallController = new HallController(hallService);
    }

    /**
     * Метод проверяет запрос на получение списка залов, затем переходит на страницу с информацией о залах.
     */
    @Test
    public void whenRequestHallsListPageThenGetPageWithHalls() {
        var hall1 = new Hall(1, "Красный зал", 12, 16, "Большой, комфортный зал .....");
        var hall2 = new Hall(1, "Синий зал зал", 14, 18, "Большой, комфортный зал .....");
        var expectedHalls = List.of(hall1, hall2);
        when(hallService.findAll()).thenReturn(expectedHalls);

        var model = new ConcurrentModel();
        var view = hallController.getAll(model);
        var actualHalls = model.getAttribute("halls");

        assertThat(view).isEqualTo("halls/list");
        assertThat(actualHalls).isEqualTo(expectedHalls);
    }
}