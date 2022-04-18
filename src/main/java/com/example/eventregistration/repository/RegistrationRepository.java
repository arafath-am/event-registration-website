package com.example.eventregistration.repository;

import com.example.eventregistration.model.AppUser;
import com.example.eventregistration.model.Event;
import com.example.eventregistration.model.Registration;
import com.example.eventregistration.model.RegistrationStatus;
import org.springframework.data.jpa.repository.EntityGraph;
    import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    long countByEventAndStatus(Event event, RegistrationStatus status);
    long countByStatus(RegistrationStatus status);
    Optional<Registration> findByEventAndUser(Event event, AppUser user);
    List<Registration> findByUserOrderByRegisteredAtDesc(AppUser user);
    List<Registration> findByEventOrderByRegisteredAtDesc(Event event);
    boolean existsByEventAndUserAndStatusIn(Event event, AppUser user, List<RegistrationStatus> statuses);
}
