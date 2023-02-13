package ru.job4j.cinema.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.FilmSessionService;
import ru.job4j.cinema.service.HallService;
import ru.job4j.cinema.service.TicketService;

import javax.servlet.http.HttpSession;
import java.util.stream.IntStream;

@ThreadSafe
@Controller
@RequestMapping("/tickets")
public class TicketController {
    private final TicketService ticketService;
    private final FilmSessionService filmSessionService;
    private final HallService hallService;

    public TicketController(TicketService ticketService, FilmSessionService filmSessionService, HallService hallService) {
        this.ticketService = ticketService;
        this.filmSessionService = filmSessionService;
        this.hallService = hallService;
    }

    @GetMapping("/{id}")
    public String getCreationPageByBuy(Model model, @PathVariable int id, HttpSession httpSession) {
        var filmSessionOptional = filmSessionService.findById(id);
        if (filmSessionOptional.isEmpty()) {
            model.addAttribute("message", "Сеанс не найден");
            return "errors/404";
        }
        var hall = hallService.findById(filmSessionOptional.get().getHallId());
        var user = (User) httpSession.getAttribute("user");

        var ticket = new Ticket();
        ticket.setSessionId(filmSessionOptional.get().getId());
        ticket.setUserId(user.getId());

        model.addAttribute("filmSession", filmSessionOptional.get());
        model.addAttribute("rows", ticketService.getRows(hall));
        model.addAttribute("places", ticketService.getPlaces(hall));
        model.addAttribute("ticket", ticket);
        return "tickets/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Ticket ticket, Model model) {
        try {
            var isCreated = ticketService.save(ticket);
            if (isCreated.isEmpty()) {
                model.addAttribute("message", "Не удалось приобрести билет на заданное место. "
                        + "Вероятно оно уже занято. Перейдите на страницу бронирования билетов и попробуйте снова.");
                return "message/bad";
            }

            model.addAttribute("message", "Вы успешно приобрели билет на " + ticket.getRowNumber()
                    + " ряд " + ticket.getPlaceNumber() + " место.");
            return "message/good";
        } catch (Exception exception) {
            model.addAttribute("message", exception.getMessage());
            return "errors/404";
        }
    }
}
