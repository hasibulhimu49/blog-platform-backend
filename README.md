# Blog Platform API

[![Java](https://img.shields.io/badge/Java-21-orange?logo=java)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5-brightgreen?logo=springboot)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue?logo=postgresql)](https://www.postgresql.org/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

A production-ready RESTful API for a full-featured blog platform built with **Spring Boot 3**, **Spring Security**, and **PostgreSQL**. Features JWT authentication, role-based access control, posts with tags, nested comments, pagination, and full OpenAPI documentation.

---

## Features

- **JWT Authentication** — Stateless auth with HS256-signed tokens
- **Role-Based Access Control** — `USER` and `ADMIN` roles with endpoint-level enforcement
- **Posts** — CRUD, soft delete, status management (`DRAFT` / `PUBLISHED`), full-text search, pagination
- **Comments** — Nested comments per post with ownership-based edit/delete
- **Tags** — Reusable tags for categorizing posts, auto-created on first use
- **User Management** — Admin can list, block, and unblock users
- **Global Error Handling** — Structured `ApiResponse<T>` for every response (success & error)
- **OpenAPI / Swagger UI** — Interactive docs with JWT Authorize button
- **Production-Ready** — Multi-stage Dockerfile, prod profile with env vars, Actuator health endpoint

---

## Tech Stack

| Layer       | Technology                              |
|-------------|-----------------------------------------|
| Language    | Java 21                                 |
| Framework   | Spring Boot 3.5                         |
| Security    | Spring Security + JWT (JJWT 0.11.5)    |
| Persistence | Spring Data JPA + Hibernate             |
| Database    | PostgreSQL                              |
| Mapping     | MapStruct + manual mappers              |
| Docs        | SpringDoc OpenAPI 2.8 (Swagger UI)      |
| Build       | Gradle 8                                |
| Deployment  | Docker (multi-stage)                    |

---

## API Endpoints

### Authentication — `/api/v1/auth`

| Method | Endpoint          | Auth     | Description              |
|--------|-------------------|----------|--------------------------|
| POST   | `/register`       | Public   | Register a new user      |
| POST   | `/login`          | Public   | Login, receive JWT token |
| GET    | `/me`             | USER     | Get own profile          |
| POST   | `/logout`         | USER     | Logout (discard token)   |

### Posts — `/api/v1/posts`

| Method | Endpoint          | Auth       | Description                    |
|--------|-------------------|------------|--------------------------------|
| GET    | `/`               | Public     | Get all published posts (paged)|
| GET    | `/{id}`           | Public     | Get a single post              |
| GET    | `/my`             | USER       | Get my posts (all statuses)    |
| GET    | `/user/{userId}`  | Public     | Get posts by a user            |
| GET    | `/search?keyword` | Public     | Search posts by keyword        |
| POST   | `/`               | USER       | Create a post                  |
| PUT    | `/{id}`           | USER/Owner | Update a post (partial)        |
| DELETE | `/{id}`           | USER/ADMIN | Soft-delete a post             |

### Comments — `/api/v1/posts/{postId}/comments`

| Method | Endpoint        | Auth        | Description              |
|--------|-----------------|-------------|--------------------------|
| GET    | `/`             | Public      | Get comments for a post  |
| POST   | `/`             | USER        | Add a comment            |
| PUT    | `/{commentId}`  | USER/Owner  | Update own comment       |
| DELETE | `/{commentId}`  | USER/ADMIN  | Delete a comment         |

### Tags — `/api/v1/tags`

| Method | Endpoint   | Auth    | Description     |
|--------|------------|---------|-----------------|
| GET    | `/`        | Public  | List all tags   |
| POST   | `/`        | ADMIN   | Create a tag    |
| DELETE | `/{id}`    | ADMIN   | Delete a tag    |

### Users — `/api/v1/users`

| Method | Endpoint         | Auth  | Description         |
|--------|------------------|-------|---------------------|
| GET    | `/`              | ADMIN | List all users      |
| GET    | `/{id}`          | ADMIN | Get user by ID      |
| PATCH  | `/{id}/block`    | ADMIN | Block a user        |
| PATCH  | `/{id}/unblock`  | ADMIN | Unblock a user      |

---

## Response Format

All endpoints return a consistent `ApiResponse<T>` envelope:

```json
{
  "success": true,
  "message": "Post created successfully",
  "data": "",
  "timestamp": "2026-04-19T22:00:00"
}
```

Errors follow the same shape:

```json
{
  "success": false,
  "message": "Post not found with id: 99",
  "timestamp": "2026-04-19T22:00:00"
}
```

---

## Getting Started

### Prerequisites

- Java 21
- PostgreSQL 14+
- Gradle (or use the included `./gradlew`)

### 1. Clone the repository

```bash
git clone https://github.com/hasibulhimu49/blog-platform-backend.git
cd blog-platform-backend
```

### 2. Create a PostgreSQL database

```sql
CREATE DATABASE "blog-db";
```

### 3. Configure (dev profile — `application-dev.yaml`)

The dev profile already has default local credentials. Change if needed:

```yaml
datasource:
  url: jdbc:postgresql://localhost:5432/blog-db
  username: ******
  password: ******
```

### 4. Run

```bash
./gradlew bootRun
```

The API will be available at: `http://localhost:8080`  
Swagger UI: `http://localhost:8080/swagger-ui/index.html`

---

## Environment Variables (Production)

| Variable         | Description                                       |
|------------------|---------------------------------------------------|
| `DB_URL`         | PostgreSQL JDBC URL                               |
| `DB_USERNAME`    | Database username                                 |
| `DB_PASSWORD`    | Database password                                 |
| `JWT_SECRET`     | Base64-encoded 256-bit HS256 secret key           |
| `JWT_EXPIRATION` | Token expiry in ms (default: `86400000` = 24h)    |
| `PORT`           | Server port (default: `8080`)                     |

> **Generate a JWT secret:**
> ```bash
> openssl rand -base64 32
> ```

---

## Docker

### Build & run locally

```bash
docker build -t blog-platform-api .

docker run -p 8080:8080 \
  -e DB_URL=jdbc:postgresql://host.docker.internal:5432/blog-db \
  -e DB_USERNAME=postgres \
  -e DB_PASSWORD=admin1234 \
  -e JWT_SECRET=<your-base64-secret> \
  -e SPRING_PROFILES_ACTIVE=prod \
  blog-platform-api
```

---

## Project Structure

```
src/main/java/com/example/blog_platform_api/
├── auth/           # Registration, login, /me, JWT issuance
├── comment/        # Comment CRUD (entity, DTO, service, controller)
├── common/         # ApiResponse, exceptions, enums, BaseEntity, constants
├── config/         # Security, CORS, OpenAPI, JPA auditing configs
├── post/           # Post CRUD, search, pagination
├── security/       # JwtService, JwtAuthenticationFilter, UserPrincipal
├── tag/            # Tag management
└── user/           # User management (admin operations)
```

---

## License

This project is licensed under the [MIT License](https://opensource.org/licenses/MIT).

---

## Developer

**Mohammad Hasibul Hasan**  
📧 hasibulx2026@gmail.com  
🐙 [GitHub](https://github.com/hasibulhimu49)