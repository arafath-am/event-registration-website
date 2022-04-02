package com.example.eventregistration.service;

import com.example.eventregistration.dto.EventForm;
import com.example.eventregistration.exception.BusinessRuleException;
import com.example.eventregistration.exception.ResourceNotFoundException;
import com.example.eventregistration.model.Event;
import com.example.eventregistration.model.EventStatus;
import com.example.eventregistration.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    @Transactional(readOnly = true)
    public List<Event> findPublishedUpcomingEvents(String keyword) {
        return eventRepository.searchPublishedUpcomingEvents(keyword, EventStatus.PUBLISHED, LocalDateTime.now());
    }

    @Transactional(readOnly = true)
    public List<Event> findAllEvents() {
        return eventRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Event findById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));
    }

    @Transactional
    public Event createEvent(EventForm form) {
        validateDeadline(form.getRegistrationDeadline(), form.getEventDate());

        Event event = Event.builder()
                .title(form.getTitle())
                .description(form.getDescription())
                .location(form.getLocation())
                .category(form.getCategory())
                .eventDate(form.getEventDate())
                .registrationDeadline(form.getRegistrationDeadline())
                .capacity(form.getCapacity())
                .imageUrl(form.getImageUrl())
                .status(form.getStatus())
                .build();

        return eventRepository.save(event);
    }

    @Transactional
    public Event updateEvent(Long id, EventForm form) {
        validateDeadline(form.getRegistrationDeadline(), form.getEventDate());

        Event event = findById(id);
        event.setTitle(form.getTitle());
        event.setDescription(form.getDescription());
        event.setLocation(form.getLocation());
        event.setCategory(form.getCategory());
        event.setEventDate(form.getEventDate());
        event.setRegistrationDeadline(form.getRegistrationDeadline());
        event.setCapacity(form.getCapacity());
        event.setImageUrl(form.getImageUrl());
        event.setStatus(form.getStatus());

        return eventRepository.save(event);
    }

    @Transactional
    public void deleteEvent(Long id) {
        Event event = findById(id);
        eventRepository.delete(event);
    }

    @Transactional(readOnly = true)
    public long countPublishedEvents() {
        return eventRepository.countByStatus(EventStatus.PUBLISHED);
    }

    private void validateDeadline(LocalDateTime registrationDeadline, LocalDateTime eventDate) {
        if (registrationDeadline != null && eventDate != null && registrationDeadline.isAfter(eventDate)) {
            throw new BusinessRuleException("Registration deadline cannot be after event date.");
        }
    }

    public EventForm toForm(Event event) {
        EventForm form = new EventForm();
        form.setTitle(event.getTitle());
        form.setDescription(event.getDescription());
        form.setLocation(event.getLocation());
        form.setCategory(event.getCategory());
        form.setEventDate(event.getEventDate());
        form.setRegistrationDeadline(event.getRegistrationDeadline());
        form.setCapacity(event.getCapacity());
        form.setImageUrl(event.getImageUrl());
        form.setStatus(event.getStatus());
        return form;
    }
}
