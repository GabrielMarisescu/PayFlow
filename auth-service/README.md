# Auth Service

Authentication and authorization microservice for the PayFlow platform. Manages user identities, issues JWT tokens, and enforces role-based access control across all platform services.

---

## Responsibilities

- User registration and credential management
- User authentication (login / logout)
- JWT access and refresh token generation
- Token validation and renewal
- Role and permission management (RBAC)
- Password hashing and security policies
- Audit logging of auth events

---

## Features

### User Management
- [ ] User registration with email and password
- [ ] Email uniqueness validation
- [ ] Password strength enforcement (min length, complexity)
- [ ] Password hashing with BCrypt
- [ ] User profile retrieval (by ID, by email)
- [ ] User account deactivation / activation
- [ ] Update user details

### Authentication
- [ ] Login endpoint (email + password) returning JWT tokens
- [ ] Access token (short-lived) + Refresh token (long-lived) pair
- [ ] Refresh token rotation (issue new pair, invalidate old)
- [ ] Logout (invalidate refresh token)
- [ ] Logout from all devices (invalidate all refresh tokens for user)

### Authorization & RBAC
- [ ] Predefined roles: `ADMIN`, `USER`
- [ ] Role assignment on registration (default `USER`)
- [ ] Admin endpoint to assign / revoke roles
- [ ] Permission checks based on roles
- [ ] Role hierarchy support

### Token Management
- [ ] JWT signing with RSA or HMAC secret
- [ ] Configurable token expiration times
- [ ] Token blacklisting on logout (Redis)
- [ ] Token validation endpoint for other services (gRPC)

### Security
- [ ] Rate limiting on login attempts
- [ ] Account lockout after repeated failures
- [ ] Secure password reset flow
- [ ] CORS configuration
- [ ] Input validation on all endpoints

### Audit & Observability
- [ ] Log all login attempts (success / failure)
- [ ] Log role changes
- [ ] Log token generation and revocation
- [ ] Actuator health and metrics endpoints

### Inter-Service Communication
- [ ] gRPC endpoint for token validation (used by Gateway and other services)
- [ ] gRPC endpoint for user lookup by ID

---

## API Endpoints (REST)

| Method | Endpoint                  | Description                  | Auth Required |
|--------|---------------------------|------------------------------|---------------|
| POST   | `/api/auth/register`      | Register new user            | No            |
| POST   | `/api/auth/login`         | Authenticate and get tokens  | No            |
| POST   | `/api/auth/refresh`       | Refresh access token         | Refresh Token |
| POST   | `/api/auth/logout`        | Invalidate refresh token     | Yes           |
| GET    | `/api/users/me`           | Get current user profile     | Yes           |
| GET    | `/api/users/{id}`         | Get user by ID               | ADMIN         |
| PUT    | `/api/users/{id}/roles`   | Assign roles to user         | ADMIN         |
| DELETE | `/api/users/{id}/roles`   | Revoke roles from user       | ADMIN         |
| PUT    | `/api/users/me`           | Update own profile           | Yes           |
| POST   | `/api/auth/password-reset`| Request password reset       | No            |

---

## Tech Stack

- Java 21
- Spring Boot 3.5.10
- Spring Security
- Spring Data JPA
- PostgreSQL
- Redis (token blacklist)
- gRPC (inter-service auth validation)
- JWT (jjwt library)
- Lombok
- JUnit + Testcontainers

---

## Configuration

Key properties (via `application.yml`):

| Property                          | Description                    |
|-----------------------------------|--------------------------------|
| `jwt.secret`                      | JWT signing secret             |
| `jwt.access-token-expiration`     | Access token TTL (minutes)     |
| `jwt.refresh-token-expiration`    | Refresh token TTL (days)       |
| `auth.max-login-attempts`         | Max failed logins before lock  |
| `auth.lockout-duration`           | Account lockout duration       |
| `spring.datasource.url`           | PostgreSQL connection URL      |
| `spring.data.redis.host`          | Redis host                     |
| `grpc.server.port`                | gRPC server port               |

---

## Data Model

### User
| Field          | Type      | Description              |
|----------------|-----------|--------------------------|
| id             | UUID      | Primary key              |
| email          | String    | Unique, login identifier |
| password       | String    | BCrypt hashed            |
| firstName      | String    | User first name          |
| lastName       | String    | User last name           |
| enabled        | Boolean   | Account active flag      |
| locked         | Boolean   | Account locked flag      |
| failedAttempts | Integer   | Failed login counter     |
| createdAt      | Timestamp | Creation timestamp       |
| updatedAt      | Timestamp | Last update timestamp    |

### Role
| Field | Type   | Description       |
|-------|--------|-------------------|
| id    | Long   | Primary key       |
| name  | String | Role name (enum)  |

### RefreshToken
| Field     | Type      | Description             |
|-----------|-----------|-------------------------|
| id        | UUID      | Primary key             |
| token     | String    | Opaque refresh token    |
| userId    | UUID      | Owner reference         |
| expiresAt | Timestamp | Expiration time         |
| revoked   | Boolean   | Revocation flag         |
| createdAt | Timestamp | Creation timestamp      |
