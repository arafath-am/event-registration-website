package com.example.eventregistration.dto;

import com.example.eventregistration.model.Registration;
import com.example.eventregistration.model.RegistrationStatus;

import java.time.LocalDateTime;

public record RegistrationResponseDto(
        Long id,
        Long eventId,
        String eventTitle,
        String userEmail,
        RegistrationStatus status,
        LocalDateTime registeredAt
) {
    public static RegistrationResponseDto from(Registration registration) {
        return new RegistrationResponseDto(
                registration.getId(),
                registration.getEvent().getId(),
                registration.getEvent().getTitle(),
                registration.getUser().getEmail(),
                registration.getStatus(),
                registration.getRegisteredAt()
        );
    }
}
