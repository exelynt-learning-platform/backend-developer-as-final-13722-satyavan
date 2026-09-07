# RESTful Resource Booking System

A secure resource booking REST API built with Spring Boot 3, Spring Security 6, JWT, Spring Data JPA, and MySQL.

---

## Prerequisites

- **Java 17+**
- **MySQL 8+**
- **Maven**

---

## Configuration

Create a `.env` file in the root directory (or configure environment variables):

DB_URL=jdbc:mysql://localhost:3306/booking_db?createDatabaseIfNotExist=true
DB_USERNAME=root
DB_PASSWORD=your_password
JWT_SECRET=your_secret
JWT_EXPIRATION_MS=your_expiration

---

## How to Run

### 1. Start MySQL

Ensure your MySQL instance is running and accessible with the credentials specified in `.env`.

### 2. Export Environment Variables (Linux/macOS)

export $(cat .env | xargs)

_(On Windows, set them in your system environment or IDE Run Configuration)._

### 3. Build & Run

**Linux / macOS:**

./mvnw clean spring-boot:run

**Windows:**

mvnw.cmd clean spring-boot:run

The application will start at: `http://localhost:8080`

### 4. Interactive API Docs

- **Swagger UI:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **OpenAPI JSON:** [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

### Pre-seeded Test Accounts

| Role    | Email             | Password   |
| ------- | ----------------- | ---------- |
| `ADMIN` | `admin@gmail.com` | `admin123` |
| `USER`  | `user@gmail.com`  | `user123`  |

---

## API Endpoints

All authenticated requests require the header:

Authorization: Bearer <your-jwt-token>

### 1. Authentication

| Method | Endpoint         | Access        | Description                                        |
| ------ | ---------------- | ------------- | -------------------------------------------------- |
| `POST` | `/auth/login`    | Public        | Authenticates credentials and returns a JWT token. |
| `POST` | `/auth/register` | Public        | Registers a new user (`ROLE_USER`).                |
| `POST` | `/auth/logout`   | Authenticated | Blacklists and revokes the current bearer token.   |

### 2. Resources

| Method   | Endpoint          | Access        | Description                          |
| -------- | ----------------- | ------------- | ------------------------------------ |
| `GET`    | `/resources`      | Authenticated | Retrieves all bookable resources.    |
| `GET`    | `/resources/{id}` | Authenticated | Retrieves a specific resource by ID. |
| `POST`   | `/resources`      | `ROLE_ADMIN`  | Creates a new resource.              |
| `PUT`    | `/resources/{id}` | `ROLE_ADMIN`  | Updates an existing resource.        |
| `DELETE` | `/resources/{id}` | `ROLE_ADMIN`  | Deletes a resource by ID.            |

### 3. Reservations

| Method   | Endpoint                    | Access        | Description                                             |
| -------- | --------------------------- | ------------- | ------------------------------------------------------- |
| `POST`   | `/reservations`             | Authenticated | Creates a reservation for the authenticated user.       |
| `GET`    | `/reservations`             | Authenticated | Returns paginated reservations (Admin: all, User: own). |
| `PATCH`  | `/reservations/{id}/status` | Authenticated | Updates status (`PENDING`, `CONFIRMED`, `CANCELLED`).   |
| `DELETE` | `/reservations/{id}`        | `ROLE_ADMIN`  | Deletes a reservation by ID.                            |

#### Reservation Query Parameters (`GET /reservations`)

| Parameter  | Type    | Description                                            | Example     |
| ---------- | ------- | ------------------------------------------------------ | ----------- |
| `status`   | string  | Filter by status (`PENDING`, `CONFIRMED`, `CANCELLED`) | `CONFIRMED` |
| `minPrice` | number  | Minimum reservation price                              | `50.00`     |
| `maxPrice` | number  | Maximum reservation price                              | `200.00`    |
| `page`     | integer | Page index (0-based, default `0`)                      | `0`         |
| `size`     | integer | Items per page (default `10`)                          | `10`        |
| `sort`     | string  | Field and direction (default `startTime,desc`)         | `price,asc` |
