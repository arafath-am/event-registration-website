package com.example.eventregistration.service;

import com.example.eventregistration.exception.BusinessRuleException;
import com.example.eventregistration.model.*;
import com.example.eventregistration.repository.RegistrationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final EventService eventService;
    private final UserService userService;

    @Transactional
    public Registration registerForEvent(Long eventId, String userEmail) {
        Event event = eventService.findById(eventId);
        AppUser user = userService.findByEmail(userEmail);

        if (!event.isRegistrationOpen()) {
            throw new BusinessRuleException("Registration is closed for this event.");
        }

        boolean alreadyRegistered = registrationRepository.existsByEventAndUserAndStatusIn(
                event,
                user,
                List.of(RegistrationStatus.REGISTERED, RegistrationStatus.WAITLISTED)
        );

        if (alreadyRegistered) {
            throw new BusinessRuleException("You already have an active registration for this event.");
        }

        long registeredCount = registrationRepository.countByEventAndStatus(event, RegistrationStatus.REGISTERED);
        RegistrationStatus status = registeredCount < event.getCapacity()
                ? RegistrationStatus.REGISTERED
                : RegistrationStatus.WAITLISTED;

        Registration registration = registrationRepository.findByEventAndUser(event, user)
                .orElseGet(() -> Registration.builder().event(event).user(user).build());

        registration.setStatus(status);
        registration.setRegisteredAt(LocalDateTime.now());
        registration.setCancelledAt(null);

        return registrationRepository.save(registration);
    }

    @Transactional
    public void cancelRegistration(Long registrationId, String userEmail) {
        Registration registration = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new BusinessRuleException("Registration not found."));

        if (!registration.getUser().getEmail().equalsIgnoreCase(userEmail)) {
            throw new BusinessRuleException("You can only cancel your own registrations.");
        }

        registration.setStatus(RegistrationStatus.CANCELLED);
        registration.setCancelledAt(LocalDateTime.now());
        registrationRepository.save(registration);
    }

    @Transactional(readOnly = true)
    public List<Registration> getRegistrationsForUser(String userEmail) {
        AppUser user = userService.findByEmail(userEmail);
        return registrationRepository.findByUserOrderByRegisteredAtDesc(user);
    }

    @Transactional(readOnly = true)
    public List<Registration> getRegistrationsForEvent(Long eventId) {
        Event event = eventService.findById(eventId);
        return registrationRepository.findByEventOrderByRegisteredAtDesc(event);
    }

    @Transactional(readOnly = true)
    public long countRegisteredForEvent(Event event) {
        return registrationRepository.countByEventAndStatus(event, RegistrationStatus.REGISTERED);
    }

    @Transactional(readOnly = true)
    public long countWaitlisted() {
        return registrationRepository.countByStatus(RegistrationStatus.WAITLISTED);
    }

    @Transactional(readOnly = true)
    public long countActiveRegistrations() {
        return registrationRepository.countByStatus(RegistrationStatus.REGISTERED);
    }
}
