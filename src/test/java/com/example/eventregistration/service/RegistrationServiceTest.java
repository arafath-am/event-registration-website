package com.example.eventregistration.service;

import com.example.eventregistration.model.*;
import com.example.eventregistration.repository.EventRepository;
import com.example.eventregistration.repository.RegistrationRepository;
import com.example.eventregistration.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
class RegistrationServiceTest {

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private AppUser user;

    @BeforeEach
    void setUp() {
        registrationRepository.deleteAll();
        eventRepository.deleteAll();
        userRepository.deleteAll();

        user = userRepository.save(AppUser.builder()
                .fullName("Test User")
                .email("test@example.com")
                .password(passwordEncoder.encode("password"))
                .role(Role.ROLE_USER)
                .build());
    }

    @Test
    void registerForEventCreatesConfirmedRegistrationWhenSeatsAreAvailable() {
        Event event = createEvent(2);

        Registration registration = registrationService.registerForEvent(event.getId(), user.getEmail());

        assertThat(registration.getStatus()).isEqualTo(RegistrationStatus.REGISTERED);
        assertThat(registrationService.countRegisteredForEvent(event)).isEqualTo(1);
    }

    @Test
    void registerForEventCreatesWaitlistWhenCapacityIsFull() {
        Event event = createEvent(1);
        registrationService.registerForEvent(event.getId(), user.getEmail());

        AppUser secondUser = userRepository.save(AppUser.builder()
                .fullName("Second User")
                .email("second@example.com")
                .password(passwordEncoder.encode("password"))
                .role(Role.ROLE_USER)
                .build());

        Registration registration = registrationService.registerForEvent(event.getId(), secondUser.getEmail());

        assertThat(registration.getStatus()).isEqualTo(RegistrationStatus.WAITLISTED);
    }

    @Test
    void registerForEventRejectsDuplicateActiveRegistration() {
        Event event = createEvent(2);
        registrationService.registerForEvent(event.getId(), user.getEmail());

        assertThatThrownBy(() -> registrationService.registerForEvent(event.getId(), user.getEmail()))
                .hasMessageContaining("already have an active registration");
    }

    private Event createEvent(int capacity) {
        LocalDateTime now = LocalDateTime.now();
        return eventRepository.save(Event.builder()
                .title("Test Event")
                .description("Test Description")
                .location("Room 101")
                .category("Testing")
                .eventDate(now.plusDays(10))
                .registrationDeadline(now.plusDays(5))
                .capacity(capacity)
                .status(EventStatus.PUBLISHED)
                .build());
    }
}
