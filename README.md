# EventHub - Event Registration Website

EventHub is a full-stack Java Spring Boot web application for creating events, browsing upcoming events, registering attendees, managing capacity, and reviewing registrations through an admin dashboard.


## Features

### Public users
- View upcoming events
- Search events by title, description, location, or category
- View event details
- Create a user account
- Login/logout

### Authenticated users
- Register for events
- Auto-waitlist when capacity is full
- Cancel own registration
- View personal registration history

### Admin users
- Admin dashboard with event and registration metrics
- Create, edit, publish, close, or delete events
- View all registrations for each event
- Manage event capacity and registration deadline

### Technical features
- Spring Boot MVC + Thymeleaf frontend
- Spring Security authentication and role-based authorization
- Spring Data JPA repositories
- H2 database for local development
- MySQL-ready production profile
- Server-side validation
- REST API endpoints
- Seed data loader
- Unit/integration tests
- Dockerfile and docker-compose
- Actuator health endpoint

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Backend | Spring Boot 3.5.14 |
| Web MVC | Spring MVC + Thymeleaf |
| Security | Spring Security |
| Persistence | Spring Data JPA / Hibernate |
| Database | H2 for dev, MySQL for prod |
| Build | Maven |
| Testing | JUnit 5, Spring Boot Test, Spring Security Test |
| Deployment | Docker, Docker Compose |

## Default Login Accounts

The application seeds demo users at startup.

| Role | Email | Password |
|---|---|---|
| Admin | `admin@eventhub.com` | `admin123` |
| User | `user@eventhub.com` | `user123` |

## Run Locally

Requirements:
- Java 17+
- Maven 3.6.3+

```bash
git clone https://github.com/YOUR_USERNAME/event-registration-website.git
cd event-registration-website
mvn spring-boot:run
```

Open:

```text
http://localhost:8080
```

H2 Console:

```text
http://localhost:8080/h2-console
```

H2 settings:

```text
JDBC URL: jdbc:h2:mem:eventhubdb
Username: sa
Password: password
```

## Run Tests

```bash
mvn test
```

## Run with Docker

```bash
docker compose up --build
```

App:

```text
http://localhost:8080
```

## Project Structure

```text
event-registration-website/
  src/main/java/com/example/eventregistration/
    config/          Security and seed data configuration
    controller/      MVC and REST controllers
    dto/             Form and API DTOs
    exception/       Custom domain exceptions
    model/           JPA entities and enums
    repository/      Spring Data JPA repositories
    service/         Business logic
  src/main/resources/
    static/          CSS and JavaScript
    templates/       Thymeleaf pages
  src/test/          Service and controller tests
  docs/              Architecture, API, deployment and database docs
  Dockerfile
  docker-compose.yml
  README.md
```

## Main Pages

| URL | Access | Description |
|---|---|---|
| `/` | Public | Landing page |
| `/events` | Public | Browse/search events |
| `/events/{id}` | Public | Event detail page |
| `/register` | Public | Create user account |
| `/login` | Public | Login page |
| `/my-registrations` | User/Admin | Personal registrations |
| `/admin/dashboard` | Admin | Admin dashboard |
| `/admin/events/new` | Admin | Create event |
| `/admin/events/{id}/edit` | Admin | Edit event |

## REST API

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/events` | List published events |
| GET | `/api/events/{id}` | Get event by ID |
| GET | `/api/health` | Simple API health check |

## Design Highlights

- Uses MVC separation instead of putting all logic into controllers like a sleep-deprived intern.
- Capacity logic lives in `RegistrationService`, not in the view layer.
- Admin authorization is enforced through Spring Security route rules.
- Registration uniqueness is enforced by both service logic and a database unique constraint.
- The project includes practical docs so GitHub visitors understand architecture, not just screenshots and hope.


## Future Improvements

- Email confirmations
- QR code check-in
- Payment integration
- Calendar export
- Event recommendation engine
- Admin analytics dashboard
- Cloud deployment to AWS/GCP/Azure
