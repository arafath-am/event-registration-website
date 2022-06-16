# Deployment Guide

## Local Development

```bash
mvn spring-boot:run
```

## Build JAR

```bash
mvn clean package
java -jar target/event-registration-website-1.0.0.jar
```

## Docker

```bash
docker compose up --build
```

## Production Profile

The `docker` profile uses MySQL.

Environment variables:

```text
SPRING_PROFILES_ACTIVE=docker
MYSQL_HOST=mysql
MYSQL_PORT=3306
MYSQL_DATABASE=eventhub
MYSQL_USER=eventhub_user
MYSQL_PASSWORD=eventhub_password
```

## Cloud Deployment Options

Good beginner-friendly deployment targets:

- Render
- Railway
- AWS Elastic Beanstalk
- Azure App Service
- Google Cloud Run

## Production Improvements

Before using this in production:

- Disable demo seed accounts
- Use managed MySQL/PostgreSQL
- Use HTTPS
- Configure environment-based secrets
- Add email verification
- Add structured logging
- Add CI/CD pipeline
