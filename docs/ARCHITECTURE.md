# Architecture Document

## Overview

EventHub follows a layered Spring Boot MVC architecture.

```text
Browser
  ↓
Thymeleaf Views
  ↓
Controllers
  ↓
Services
  ↓
Repositories
  ↓
Database
```

## Layers

### Controller Layer

Handles HTTP requests, form submissions, redirects, and model attributes.

Main controllers:
- `HomeController`
- `AuthController`
- `EventController`
- `AdminController`
- `ApiController`

### Service Layer

Contains business rules and transaction boundaries.

Main services:
- `UserService`
- `EventService`
- `RegistrationService`
- `CustomUserDetailsService`

Business rules include:
- Duplicate email prevention
- Event deadline validation
- Registration open/closed validation
- Duplicate registration prevention
- Capacity-based waitlisting
- User-owned cancellation enforcement

### Repository Layer

Uses Spring Data JPA to abstract database operations.

Main repositories:
- `UserRepository`
- `EventRepository`
- `RegistrationRepository`

### Security Layer

Spring Security provides:
- Form login
- BCrypt password hashing
- Route-based authorization
- Admin-only dashboard protection
- Authenticated-only registration actions

## Entity Relationships

```text
AppUser 1 ─── * Registration * ─── 1 Event
```

A user can register for many events.
An event can have many registrations.
A registration belongs to exactly one user and one event.

## Why This Looks Like a Real CS Project

- Proper separation of concerns
- Persistent data model
- Authentication and authorization
- Transactional business logic
- Form validation
- Error handling
- API support
- Test coverage
- Deployment artifacts
