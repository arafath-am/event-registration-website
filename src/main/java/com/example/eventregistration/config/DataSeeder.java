package com.example.eventregistration.config;

import com.example.eventregistration.model.*;
import com.example.eventregistration.repository.EventRepository;
import com.example.eventregistration.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.seed-data:true}")
    private boolean seedData;

    @Override
    public void run(String... args) {
        if (!seedData) {
            return;
        }

        seedUsers();
        seedEvents();
    }

    private void seedUsers() {
        if (!userRepository.existsByEmail("admin@eventhub.com")) {
            userRepository.save(AppUser.builder()
                    .fullName("Admin User")
                    .email("admin@eventhub.com")
                    .password(passwordEncoder.encode("admin123"))
                    .role(Role.ROLE_ADMIN)
                    .build());
        }

        if (!userRepository.existsByEmail("user@eventhub.com")) {
            userRepository.save(AppUser.builder()
                    .fullName("Demo Student")
                    .email("user@eventhub.com")
                    .password(passwordEncoder.encode("user123"))
                    .role(Role.ROLE_USER)
                    .build());
        }
    }

    private void seedEvents() {
        if (eventRepository.count() > 0) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();

        List<Event> events = List.of(
                Event.builder()
                        .title("AI & Cloud Computing Symposium")
                        .description("A technical event covering applied AI, cloud deployment strategies, and responsible software architecture for modern systems.")
                        .location("Engineering Auditorium")
                        .category("Technology")
                        .eventDate(now.plusDays(14).withHour(10).withMinute(0))
                        .registrationDeadline(now.plusDays(12).withHour(23).withMinute(59))
                        .capacity(120)
                        .imageUrl("https://images.unsplash.com/photo-1519389950473-47ba0277781c")
                        .status(EventStatus.PUBLISHED)
                        .build(),
                Event.builder()
                        .title("Graduate Research Poster Night")
                        .description("Students present research posters in distributed systems, machine learning, databases, cybersecurity, and software engineering.")
                        .location("Student Innovation Center")
                        .category("Academic")
                        .eventDate(now.plusDays(21).withHour(17).withMinute(30))
                        .registrationDeadline(now.plusDays(18).withHour(23).withMinute(59))
                        .capacity(80)
                        .imageUrl("https://images.unsplash.com/photo-1551836022-d5d88e9218df")
                        .status(EventStatus.PUBLISHED)
                        .build(),
                Event.builder()
                        .title("Career Fair Prep Workshop")
                        .description("Hands-on resume review, behavioral interview practice, LinkedIn profile cleanup, and technical interview preparation.")
                        .location("Career Services Lab")
                        .category("Career")
                        .eventDate(now.plusDays(7).withHour(15).withMinute(0))
                        .registrationDeadline(now.plusDays(6).withHour(20).withMinute(0))
                        .capacity(35)
                        .imageUrl("https://images.unsplash.com/photo-1556761175-b413da4baf72")
                        .status(EventStatus.PUBLISHED)
                        .build(),
                Event.builder()
                        .title("Database Design Hack Night")
                        .description("A practical evening session on relational schema design, indexing, query optimization, and transaction handling.")
                        .location("Computer Science Lab 204")
                        .category("Workshop")
                        .eventDate(now.plusDays(10).withHour(18).withMinute(0))
                        .registrationDeadline(now.plusDays(9).withHour(22).withMinute(0))
                        .capacity(40)
                        .imageUrl("https://images.unsplash.com/photo-1555949963-aa79dcee981c")
                        .status(EventStatus.PUBLISHED)
                        .build()
        );

        eventRepository.saveAll(events);
    }
}
