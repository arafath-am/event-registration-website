package com.example.eventregistration.controller;

import com.example.eventregistration.dto.EventForm;
import com.example.eventregistration.exception.BusinessRuleException;
import com.example.eventregistration.model.EventStatus;
import com.example.eventregistration.service.EventService;
import com.example.eventregistration.service.RegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final EventService eventService;
    private final RegistrationService registrationService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("events", eventService.findAllEvents());
        model.addAttribute("publishedEventCount", eventService.countPublishedEvents());
        model.addAttribute("activeRegistrationCount", registrationService.countActiveRegistrations());
        model.addAttribute("waitlistedCount", registrationService.countWaitlisted());
        return "admin/dashboard";
    }

    @GetMapping("/events/new")
    public String newEvent(Model model) {
        model.addAttribute("eventForm", new EventForm());
        model.addAttribute("statuses", EventStatus.values());
        model.addAttribute("pageTitle", "Create Event");
        return "admin/event-form";
    }

    @PostMapping("/events")
    public String createEvent(
            @Valid @ModelAttribute EventForm eventForm,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("statuses", EventStatus.values());
            model.addAttribute("pageTitle", "Create Event");
            return "admin/event-form";
        }

        try {
            eventService.createEvent(eventForm);
            redirectAttributes.addFlashAttribute("successMessage", "Event created successfully.");
            return "redirect:/admin/dashboard";
        } catch (BusinessRuleException ex) {
            model.addAttribute("formError", ex.getMessage());
            model.addAttribute("statuses", EventStatus.values());
            model.addAttribute("pageTitle", "Create Event");
            return "admin/event-form";
        }
    }

    @GetMapping("/events/{id}/edit")
    public String editEvent(@PathVariable Long id, Model model) {
        model.addAttribute("eventForm", eventService.toForm(eventService.findById(id)));
        model.addAttribute("eventId", id);
        model.addAttribute("statuses", EventStatus.values());
        model.addAttribute("pageTitle", "Edit Event");
        return "admin/event-form";
    }

    @PostMapping("/events/{id}")
    public String updateEvent(
            @PathVariable Long id,
            @Valid @ModelAttribute EventForm eventForm,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("eventId", id);
            model.addAttribute("statuses", EventStatus.values());
            model.addAttribute("pageTitle", "Edit Event");
            return "admin/event-form";
        }

        try {
            eventService.updateEvent(id, eventForm);
            redirectAttributes.addFlashAttribute("successMessage", "Event updated successfully.");
            return "redirect:/admin/dashboard";
        } catch (BusinessRuleException ex) {
            model.addAttribute("formError", ex.getMessage());
            model.addAttribute("eventId", id);
            model.addAttribute("statuses", EventStatus.values());
            model.addAttribute("pageTitle", "Edit Event");
            return "admin/event-form";
        }
    }

    @PostMapping("/events/{id}/delete")
    public String deleteEvent(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        eventService.deleteEvent(id);
        redirectAttributes.addFlashAttribute("successMessage", "Event deleted successfully.");
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/events/{id}/registrations")
    public String eventRegistrations(@PathVariable Long id, Model model) {
        model.addAttribute("event", eventService.findById(id));
        model.addAttribute("registrations", registrationService.getRegistrationsForEvent(id));
        return "admin/event-registrations";
    }
}
