# TheaterBookingExamSpring2025 Project Overview

## Introduction
TheaterBookingExamSpring2025 is a RESTful API application for managing theater performances and actors.  
It includes CRUD operations for performances, assignment of lead actors, database population, and secure authentication and authorization using JWT.

## Project Structure

### Main Packages
- `config`: Hibernate and application configuration
- `controllers`: Controllers handling HTTP logic
- `daos`: Data Access Objects (JPA and Hibernate)
- `dtos`: Data Transfer Objects
- `entities`: JPA entity classes (Actor, Performance)
- `enums`: Genre enum (DRAMA, COMEDY, MUSICAL)
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

I use `PUT` to assign an actor to a performance because it's idempotent and suitable for updating an existing resource. `PATCH` could also be used, but `PUT` is more appropriate for complete assignment.

### Populator
- `GET /populator/users` – Populate default users
- `GET /populator/theater` – Populate performances and actors

### Security
- `POST /auth/register` – Register user
- `POST /auth/login` – Login and receive JWT token
- `POST /auth/user/addrole` – Add role to a user
- `GET /protected/user_demo` – Protected route for USER
- `GET /protected/admin_demo` – Protected route for ADMIN

## Testing

All required endpoints have been tested using IntelliJ `.http` files:
- GET, POST, PUT, DELETE for `/performances`
- PUT to assign actor to performance
- Actor data verified via `GET /performances/{id}`
- Populator endpoints tested
- Register/login with JWT verified
- Responses saved under `/resources/http/dev.http`

## Error Handling

- A custom `ApiException` is returned as JSON for failed operations
- Examples include:
    - Fetching a performance by non-existent ID
    - Deleting a performance that does not exist

### Checked vs. Unchecked
In Java, **checked exceptions** must be declared or caught, while **unchecked exceptions** (RuntimeExceptions) do not.  
I used unchecked exceptions (`ApiException`) for centralized REST error handling, improving control over API responses.

## Technologies Used
- Java 17
- Javalin (REST API)
- Hibernate / JPA
- PostgreSQL
- Maven
- JWT (JBCrypt, token security)
- Lombok
- Jackson
- SLF4J & Logback

## Getting Started

1. Clone the repo.
2. Create a `config.properties` file inside `/resources`:

DB_NAME=theater_booking_exam DB_USERNAME=postgres 
DB_PASSWORD=yourpassword 
SECRET_KEY=minimum32characterslong 
ISSUER=exam2025 TOKEN_EXPIRE_TIME=3600000


3. Build the app:

4. Run it:


API will be available at: `http://localhost:7007`

## Deployment
The application can be containerized using the provided `Dockerfile`.


