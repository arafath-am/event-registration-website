# API Documentation

Base URL:

```text
http://localhost:8080/api
```

## Health Check

```http
GET /api/health
```

Response:

```json
{
  "status": "UP",
  "service": "EventHub API"
}
```

## List Events

```http
GET /api/events
```

Optional query:

```http
GET /api/events?keyword=cloud
```

Response:

```json
[
  {
    "id": 1,
    "title": "AI & Cloud Computing Symposium",
    "description": "A technical event...",
    "location": "Engineering Auditorium",
    "category": "Technology",
    "eventDate": "2026-07-01T10:00:00",
    "registrationDeadline": "2026-06-29T23:59:00",
    "capacity": 120,
    "registeredCount": 30,
    "availableSeats": 90,
    "status": "PUBLISHED"
  }
]
```

## Get Event by ID

```http
GET /api/events/{id}
```

Example:

```http
GET /api/events/1
```

## Notes

The public API currently exposes read-only event information. Registration is handled through authenticated web forms because the project is designed primarily as an MVC website. A future version can add token-based API authentication and JSON registration endpoints.
