# TheaterBookingExamSpring2025 Project Overview

## Introduction
TheaterBookingExamSpring2025 is a RESTful API backend for managing theater performances and actors. It supports full CRUD operations for performances, assigning lead actors, populating test data, secure authentication and authorization using JWT, and integration with external APIs.

## Project Structure

### Main Packages
- `config`: Hibernate and application configuration
- `controllers`: Controllers handling HTTP logic
- `daos`: Data Access Objects using JPA and Hibernate
- `dtos`: Data Transfer Objects
- `entities`: JPA entity classes (`Actor`, `Performance`)
- `enums`: Genre enum (`DRAMA`, `COMEDY`, `MUSICAL`)
- `exceptions`: Custom exceptions and error handling
- `routes`: API route definitions
- `security`: JWT security implementation
- `utils`: Utility classes and populators

## Domain Model

### Performance
- Properties: `id`, `title`, `startTime`, `endTime`, `ticketPrice`, `latitude`, `longitude`, `genre`
- Relationship: Many-to-one with `Actor`

### Actor
- Properties: `id`, `firstName`, `lastName`, `email`, `phone`, `yearsOfExperience`
- Relationship: One-to-many with `Performance`

### User
- Properties: `username`, `password`
- Relationship: Many-to-many with roles (`USER`, `ADMIN`)

## API Endpoints

### Performances
- `GET /performances` – Get all performances
- `GET /performances/{id}` – Get a performance by ID (includes actor info)
- `POST /performances` – Create a performance
- `PUT /performances/{id}` – Update a performance
- `DELETE /performances/{id}` – Delete a performance
- `PUT /performances/{performanceId}/actors/{actorId}` – Assign an actor to a performance
- `POST /performances/populate` – Populate database with sample performances and actors

**PUT is preferred for assigning actors due to its stability and predictability. PATCH could be used for partial updates, but PUT offers clearer behavior**

### Additional (Not Implemented Yet)
- Filtering performances by genre
- Actor overview by revenue/time
- Integration with external API to fetch props data by genre

### Security
- `POST /auth/register` – Register user
- `POST /auth/login` – Login and receive JWT token
- `POST /auth/user/addrole` – Add role to a user
- `GET /protected/user_demo` – Protected route for USER
- `GET /protected/admin_demo` – Protected route for ADMIN

## Testing
Endpoints tested thoroughly using Rest-Assured and IntelliJ HTTP client:
- CRUD operations for performances
- Actor assignments
- Populator endpoints
- JWT-based login and protected endpoints

Test classes available in `/test` directory.

## Error Handling
Custom `ApiException` returned as JSON for robust error handling. Examples:
- Performance ID not found
- Deletion attempt on non-existent performance

### Checked vs. Unchecked Exceptions
- **Checked exceptions** require explicit handling or declaration
- **Unchecked exceptions** (RuntimeException) allow more flexible error management
- Project utilizes unchecked exceptions (`ApiException`) for consistent API response handling.

## Technologies Used
- Java 17
- Javalin
- Hibernate / JPA
- PostgreSQL
- Maven
- JWT (JBCrypt, token security)
- Lombok
- Jackson
- SLF4J & Logback

## Getting Started

1. Clone repository
2. Create `config.properties` in `/resources`:

```properties
DB_NAME=theater_booking_exam
DB_USERNAME=postgres
DB_PASSWORD=yourpassword
SECRET_KEY=minimum32characterslong
ISSUER=exam2025
TOKEN_EXPIRE_TIME=3600000

3. Build and run application

API available at: http://localhost:7007