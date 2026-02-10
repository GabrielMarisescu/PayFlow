# PayFlow – Claude Instructions

## Project Overview

PayFlow is a distributed payment processing platform simulating real-world banking. It's a greenfield project — built from scratch following the architecture defined in `README.md`.

## Tech Stack

- **Language:** Java / Spring Boot
- **Sync Communication:** gRPC
- **Async Communication:** Apache Kafka
- **Database:** PostgreSQL
- **Cache:** Redis
- **Containerization:** Docker / Docker Compose
- **Testing:** JUnit / Testcontainers

## Architecture

Microservice-based, event-driven platform. Services:

1. **API Gateway** – Entry point. REST to gRPC translation, auth validation, rate limiting, request logging.
2. **Authentication Service** – User management, JWT tokens, RBAC.
3. **Payment Service** – Core orchestrator. Payment creation, validation, state machine, event publishing.
4. **Processing Worker Service** – Async payment execution, retry/timeout handling, external provider simulation.
5. **Kafka** – Central event bus for service decoupling.

## Payment State Machine

```
CREATED → VALIDATED → PROCESSING → SENT → CONFIRMED
                        ↓
                    FAILED → RETRYING
```

## Development Phases

- **Phase 1 (current):** Gateway, Auth, Payment orchestration, Worker, Event infrastructure
- **Phase 2:** Reporting, Reconciliation, Advanced monitoring

## Conventions

- Follow standard Spring Boot / Maven project structure
- Each microservice lives in its own top-level directory
- Use Docker Compose for local multi-service orchestration
- Domain events for all state transitions
- Security at every layer (JWT, RBAC, audit logging)
- Write tests using JUnit and Testcontainers
