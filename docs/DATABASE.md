# Database Design

## Tables

### app_users

| Column | Type | Description |
|---|---|---|
| id | BIGINT | Primary key |
| full_name | VARCHAR | User full name |
| email | VARCHAR | Unique login email |
| password | VARCHAR | BCrypt-hashed password |
| role | VARCHAR | ROLE_USER or ROLE_ADMIN |
| created_at | TIMESTAMP | Account creation time |

### events

| Column | Type | Description |
|---|---|---|
| id | BIGINT | Primary key |
| title | VARCHAR | Event title |
| description | TEXT | Event details |
| location | VARCHAR | Event location |
| category | VARCHAR | Event category |
| event_date | TIMESTAMP | Event start date/time |
| registration_deadline | TIMESTAMP | Last registration time |
| capacity | INT | Maximum confirmed registrations |
| image_url | VARCHAR | Optional event image |
| status | VARCHAR | DRAFT, PUBLISHED, CLOSED, CANCELLED |
| created_at | TIMESTAMP | Event creation time |

### registrations

| Column | Type | Description |
|---|---|---|
| id | BIGINT | Primary key |
| event_id | BIGINT | Foreign key to events |
| user_id | BIGINT | Foreign key to app_users |
| status | VARCHAR | REGISTERED, WAITLISTED, CANCELLED |
| registered_at | TIMESTAMP | Registration time |
| cancelled_at | TIMESTAMP | Cancellation time |

## Constraints

- `app_users.email` is unique.
- `(event_id, user_id)` is unique in `registrations` to prevent duplicate registration records.
- Service-layer logic allows re-registration after cancellation by updating the existing record.

## ERD

```text
app_users
  id PK
  full_name
  email UNIQUE
  password
  role
  created_at

events
  id PK
  title
  description
  location
  category
  event_date
  registration_deadline
  capacity
  image_url
  status
  created_at

registrations
  id PK
  event_id FK -> events.id
  user_id FK -> app_users.id
  status
  registered_at
  cancelled_at
```
