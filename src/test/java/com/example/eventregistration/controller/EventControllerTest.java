package com.example.eventregistration.controller;

import com.example.eventregistration.model.Event;
import com.example.eventregistration.model.EventStatus;
import com.example.eventregistration.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EventRepository eventRepository;

    private Event event;

    @BeforeEach
    void setUp() {
        eventRepository.deleteAll();
        LocalDateTime now = LocalDateTime.now();
        event = eventRepository.save(Event.builder()
                .title("Controller Test Event")
                .description("Description")
                .location("Lab")
                .category("Testing")
                .eventDate(now.plusDays(7))
                .registrationDeadline(now.plusDays(3))
                .capacity(30)
                .status(EventStatus.PUBLISHED)
                .build());
    }

    @Test
    void eventsPageLoadsForPublicUsers() throws Exception {
        mockMvc.perform(get("/events"))
                .andExpect(status().isOk())
                .andExpect(view().name("events/list"));
    }

    @Test
    void eventDetailsPageLoadsForPublicUsers() throws Exception {
        mockMvc.perform(get("/events/" + event.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("events/detail"));
    }

    @Test
    void adminDashboardRedirectsAnonymousUsersToLogin() throws Exception {
        mockMvc.perform(get("/admin/dashboard"))
                .andExpect(status().is3xxRedirection());
    }
}
