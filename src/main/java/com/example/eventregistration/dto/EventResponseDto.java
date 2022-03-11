package com.example.eventregistration.dto;

import com.example.eventregistration.model.Event;
import com.example.eventregistration.model.EventStatus;

import java.time.LocalDateTime;

public record EventResponseDto(
        Long id,
        String title,
        String description,
        String location,
        String category,
        LocalDateTime eventDate,
        LocalDateTime registrationDeadline,
        int capacity,
        long registeredCount,
        long availableSeats,
        EventStatus status
) {
    public static EventResponseDto from(Event event, long registeredCount) {
        return new EventResponseDto(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getLocation(),
                event.getCategory(),
                event.getEventDate(),
                event.getRegistrationDeadline(),
                event.getCapacity(),
                registeredCount,
                Math.max(event.getCapacity() - registeredCount, 0),
                event.getStatus()
        );
    }
}
