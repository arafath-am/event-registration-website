package com.example.eventregistration.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 150)
    private String title;

    @NotBlank
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @NotBlank
    @Column(nullable = false, length = 120)
    private String location;

    @NotBlank
    @Column(nullable = false, length = 80)
    private String category;

    @Future
    @NotNull
    @Column(nullable = false)
    private LocalDateTime eventDate;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime registrationDeadline;

    @Min(1)
    @Column(nullable = false)
    private int capacity;

    @Column(length = 500)
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EventStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Registration> registrations = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (status == null) {
            status = EventStatus.DRAFT;
        }
    }

    public boolean isRegistrationOpen() {
        return status == EventStatus.PUBLISHED && registrationDeadline.isAfter(LocalDateTime.now());
    }
}
