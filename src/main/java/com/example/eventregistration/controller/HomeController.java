package com.example.eventregistration.controller;

import com.example.eventregistration.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final EventService eventService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("featuredEvents", eventService.findPublishedUpcomingEvents(null).stream().limit(3).toList());
        return "index";
    }
}
