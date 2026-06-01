# Java Interview Helper

Java Interview Helper is a Spring Boot application for storing and managing Java interview questions and answers.

The project provides a REST API for creating, updating, retrieving, and deleting interview questions. Questions can be organized by category and language, making it easier to structure and review interview preparation materials.

---

## Features

- Create, read, update, and delete interview questions
- Organize questions by category
- Store questions in PostgreSQL
- Automatic database migrations with Liquibase
- Seed initial categories and sample questions
- Global exception handling
- Request validation
- Unit tests for service and controller layers
- OpenAPI / Swagger documentation

---

## Project Structure

```text
src/
├── main/
│   ├── java/com/jih/
│   │   ├── JavaInterviewHelperApplication.java  # Entry point
│   │   ├── config/                              # Application configuration
│   │   ├── controller/                          # REST controllers
│   │   ├── dto/                                 # Request / response DTOs
│   │   ├── exception/                           # Global exception handling
│   │   ├── model/
│   │   │   └── entity/                          # JPA entities
│   │   ├── repository/                          # Spring Data JPA repositories
│   │   └── service/                             # Business logic
│   └── resources/
│       ├── application.properties               # Application configuration
│       └── db/changelog/                        # Liquibase migrations
└── test/
    └── java/com/jih/                            # Unit tests
```

---

## Tech Stack

| Layer            | Technology                                  |
|------------------|---------------------------------------------|
| Language         | Java 25                                     |
| Framework        | Spring Boot 4                               |
| Web              | Spring Web                                  |
| Persistence      | Spring Data JPA + Hibernate + PostgreSQL 16 |
| Migrations       | Liquibase                                   |
| API Docs         | SpringDoc OpenAPI (Swagger UI)              |
| Testing          | JUnit 5 + Mockito + MockMvc                 |
| Build            | Maven                                       |
| Containerisation | Docker + Docker Compose                     |

---


## Database

The application uses PostgreSQL as the primary database.

Database schema is managed through Liquibase migrations.

---

## Environment Variables

| Variable    | Default Value |
|-------------|---------------|
| DB_HOST     | localhost     |
| DB_PORT     | 5432          |
| DB_NAME     | jih_db        |
| DB_USER     | jih_user      |
| DB_PASSWORD | jih_password  |

---

## Running Locally

### Start PostgreSQL

```bash
docker-compose up -d
```

### Build the project

```bash
./mvnw clean compile
```

### Run the application

```bash
./mvnw spring-boot:run
```

### Run tests

```bash
./mvnw test
```

### Stop PostgreSQL and keep data

```bash
docker-compose down
```

### Stop PostgreSQL and remove all data

```bash
# ⚠️ Destroys postgres_data volume
docker-compose down -v
```

---

## API Documentation

Once the application is running, the interactive Swagger UI is available at:

```text
http://localhost:8080/swagger-ui.html
```

Raw OpenAPI spec (JSON):

```text
http://localhost:8080/api-docs
```

---