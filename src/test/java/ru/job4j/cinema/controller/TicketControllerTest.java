package ru.job4j.cinema.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.cinema.dto.FilmSessionDto;
import ru.job4j.cinema.model.Hall;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.FilmSessionService;
import ru.job4j.cinema.service.HallService;
import ru.job4j.cinema.service.TicketService;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TicketControllerTest {
    private TicketService ticketService;
    private FilmSessionService filmSessionService;
    private HallService hallService;
    private TicketController ticketController;

    @BeforeEach
    public void initServices() {
        ticketService = mock(TicketService.class);
        filmSessionService = mock(FilmSessionService.class);
        hallService = mock(HallService.class);
        ticketController = new TicketController(ticketService, filmSessionService, hallService);
    }

    /**
     * Метод проверяет создание страницы для покупки билетов, вывод информации о сеансе, ряда и места в зале.
     */
    @Test
    public void whenCreatePageForBuyOfTicketThenGetNumbersOfRowAndPlace() {
        var session = mock(HttpSession.class);
        var user = new User(1, "Ivan", "1@ya.ru", "meow123");
        var hall = new Hall(1, "Красный зал", 12, 16, "Большой, комфортный зал .....");
        var filmSession = new FilmSessionDto(1, "Аватар: Путь воды", "Красный зал",
                LocalDateTime.parse("2023-03-01T22:00:00"), "300 руб.", 1);
        var ticket = new Ticket();
        int id = 1;

        when(filmSessionService.findById(id)).thenReturn(Optional.of(filmSession));
        when(hallService.findById(filmSessionService.findById(id).get().getHallId())).thenReturn(Optional.of(hall));
        when(session.getAttribute("user")).thenReturn(user);

        var model = new ConcurrentModel();
        ticket.setSessionId(filmSessionService.findById(id).get().getId());
        ticket.setUserId(user.getId());
        var view = ticketController.getCreationPageByBuy(model, id, session);
        var actualFilmSession = model.getAttribute("filmSession");
        var actualTicket = model.getAttribute("ticket");

        assertThat(view).isEqualTo("tickets/create");
        assertThat(actualFilmSession).isEqualTo(filmSession);
        assertThat(actualTicket).isEqualTo(ticket);

    }

    /**
     * Метод проверяет получение страницы об успешной покупке билета.
     */
    @Test
    public void whenSelectNumbersOfRowAndPlaceThenGetSuccess() {
        var ticket = new Ticket(1, 1, 1, 1, 1);
        var expectedMessage = "Вы успешно приобрели билет на 1 ряд 1 место.";
        when(ticketService.save(ticket)).thenReturn(Optional.of(ticket));

        var model = new ConcurrentModel();
        var view = ticketController.create(ticket, model);
        var actualMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("message/good");
        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    /**
     * Метод проверяет получение страницы о неудачной покупке билета.
     */
    @Test
    public void whenSelectNumbersOfRowAndPlaceThenGetFail() {
        var ticket1 = new Ticket(1, 1, 1, 1, 1);
        var ticket2 = new Ticket(2, 1, 1, 1, 2);
        var expectedMessage = "Не удалось приобрести билет на заданное место. Вероятно оно уже занято. "
                + "Перейдите на страницу бронирования билетов и попробуйте снова.";
        when(ticketService.save(ticket1)).thenReturn(Optional.of(ticket1));
        when(ticketService.save(ticket2)).thenReturn(Optional.empty());

        var model = new ConcurrentModel();
        var view = ticketController.create(ticket2, model);
        var actualMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("message/bad");
        assertThat(actualMessage).isEqualTo(expectedMessage);
    }
}