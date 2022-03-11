package com.example.eventregistration.controller;

import com.example.eventregistration.exception.BusinessRuleException;
import com.example.eventregistration.model.Event;
import com.example.eventregistration.service.EventService;
import com.example.eventregistration.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final RegistrationService registrationService;

    @GetMapping("/events")
    public String events(@RequestParam(required = false) String keyword, Model model) {
        model.addAttribute("events", eventService.findPublishedUpcomingEvents(keyword));
        model.addAttribute("keyword", keyword);
        return "events/list";
    }

    @GetMapping("/events/{id}")
    public String eventDetails(@PathVariable Long id, Model model) {
        Event event = eventService.findById(id);
        long registeredCount = registrationService.countRegisteredForEvent(event);
        model.addAttribute("event", event);
        model.addAttribute("registeredCount", registeredCount);
        model.addAttribute("availableSeats", Math.max(event.getCapacity() - registeredCount, 0));
        return "events/detail";
    }

    @PostMapping("/events/{id}/register")
    public String registerForEvent(
            @PathVariable Long id,
            Authentication authentication,
            RedirectAttributes redirectAttributes
    ) {
        try {
            var registration = registrationService.registerForEvent(id, authentication.getName());
            if (registration.getStatus().name().equals("WAITLISTED")) {
                redirectAttributes.addFlashAttribute("warningMessage", "Event is full. You have been added to the waitlist.");
            } else {
                redirectAttributes.addFlashAttribute("successMessage", "Registration successful.");
            }
        } catch (BusinessRuleException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/events/" + id;
    }

    @GetMapping("/my-registrations")
    public String myRegistrations(Authentication authentication, Model model) {
        model.addAttribute("registrations", registrationService.getRegistrationsForUser(authentication.getName()));
        return "registrations/my-registrations";
    }

    @PostMapping("/registrations/{id}/cancel")
    public String cancelRegistration(
            @PathVariable Long id,
            Authentication authentication,
            RedirectAttributes redirectAttributes
    ) {
        try {
            registrationService.cancelRegistration(id, authentication.getName());
            redirectAttributes.addFlashAttribute("successMessage", "Registration cancelled.");
        } catch (BusinessRuleException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/my-registrations";
    }
}
