# Testing Strategy

## Test Types Included

### Service Tests

`RegistrationServiceTest` verifies:

- Successful registration
- Waitlist behavior when event capacity is full
- Duplicate registration prevention

### Controller Tests

`EventControllerTest` verifies:

- Public event list page loads
- Event details page loads
- Security behavior for protected routes

## Run Tests

```bash
mvn test
```

## Future Test Improvements

- Add repository tests with `@DataJpaTest`
- Add admin controller authorization tests
- Add Selenium or Playwright UI tests
- Add Testcontainers for MySQL integration testing
- Add CI test workflow using GitHub Actions
