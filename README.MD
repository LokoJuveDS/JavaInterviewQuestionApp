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

## How to Run

```bash
mvn spring-boot:run