package com.example.eventregistration.dto;

import com.example.eventregistration.model.EventStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EventForm {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Location is required")
    private String location;

    @NotBlank(message = "Category is required")
    private String category;

    @Future(message = "Event date must be in the future")
    @NotNull(message = "Event date is required")
    private LocalDateTime eventDate;

    @NotNull(message = "Registration deadline is required")
    private LocalDateTime registrationDeadline;

    @Min(value = 1, message = "Capacity must be at least 1")
    private int capacity;

    private String imageUrl;

    @NotNull(message = "Status is required")
    private EventStatus status = EventStatus.PUBLISHED;
}
