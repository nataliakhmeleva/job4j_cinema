package ru.job4j.cinema.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.cinema.service.HallService;

@ThreadSafe
@Controller
@RequestMapping("/halls")
public class HallController {
    private final HallService hallService;

    public HallController(HallService hallService) {
        this.hallService = hallService;
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("halls", hallService.findAll());
        return "halls/list";
    }
}
