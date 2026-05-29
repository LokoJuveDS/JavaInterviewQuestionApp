# Java Interview Helper

Java Interview Helper is a simple Spring Boot console application for storing and reviewing Java interview questions and answers.

The project is currently focused on CLI-based interaction, but the structure is prepared for future extension with REST API, database storage, and web UI.

## Features

- Add interview questions and answers
- List saved questions
- Use predefined initial questions
- Simple layered structure:
    - controller
    - service
    - repository
    - model
    - config

## Tech Stack

- Java 25
- Spring Boot 4
- Maven
- Lombok

## Running Locally

**Start a PostgreSQL instance**

```bash
docker-compose up -d
```

To stop and **wipe all data**:

```bash
# ⚠️ Destroys postgres_data volume
docker-compose down -v
```

To stop while **keeping data**:

```bash
docker-compose down
```

1. **Build and run the application**:

Build the project:

```bash
./mvnw clean compile
```

Run the application:

```bash
./mvnw spring-boot:run
```

2. **Run tests**:

```bash
./mvnw test
```
