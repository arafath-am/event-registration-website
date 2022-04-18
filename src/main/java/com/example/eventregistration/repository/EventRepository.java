package com.example.eventregistration.repository;

import com.example.eventregistration.model.Event;
import com.example.eventregistration.model.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByStatusOrderByEventDateAsc(EventStatus status);

    long countByStatus(EventStatus status);

    @Query("""
        SELECT e FROM Event e
        WHERE e.status = :status
          AND e.eventDate >= :now
          AND (
              :keyword IS NULL OR :keyword = '' OR
              LOWER(e.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
              LOWER(e.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
              LOWER(e.location) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
              LOWER(e.category) LIKE LOWER(CONCAT('%', :keyword, '%'))
          )
        ORDER BY e.eventDate ASC
    """)
    List<Event> searchPublishedUpcomingEvents(
            @Param("keyword") String keyword,
            @Param("status") EventStatus status,
            @Param("now") LocalDateTime now
    );
}
