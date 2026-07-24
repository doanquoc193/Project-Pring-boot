# Identity Service

Spring Boot REST API for user management and authentication.

## Technologies

- Java
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Maven

## Features

- Create user
- Get all users
- Get user by ID
- Update user
- Delete user
- Role management

## Run Project

1. Create PostgreSQL database:
   identity_service

2. Configure database in:
   src/main/resources/application.yaml

3. Run application:

./mvnw spring-boot:run

## API

POST /identity/users
GET /identity/users
GET /identity/users/{userId}
PUT /identity/users/{userId}
DELETE /identity/users/{userId}
