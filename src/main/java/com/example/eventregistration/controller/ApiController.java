package com.example.eventregistration.controller;

import com.example.eventregistration.dto.EventResponseDto;
import com.example.eventregistration.model.Event;
import com.example.eventregistration.service.EventService;
import com.example.eventregistration.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {

    private final EventService eventService;
    private final RegistrationService registrationService;

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "UP", "service", "EventHub API");
    }

    @GetMapping("/events")
    public List<EventResponseDto> events(@RequestParam(required = false) String keyword) {
        return eventService.findPublishedUpcomingEvents(keyword).stream()
                .map(event -> EventResponseDto.from(event, registrationService.countRegisteredForEvent(event)))
                .toList();
    }

    @GetMapping("/events/{id}")
    public EventResponseDto event(@PathVariable Long id) {
        Event event = eventService.findById(id);
        return EventResponseDto.from(event, registrationService.countRegisteredForEvent(event));
    }
}
