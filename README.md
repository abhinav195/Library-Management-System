Stack: Java 21, Spring Boot 3, Spring Security 6, Spring Data JPA/Hibernate, MySQL, HikariCP, OpenFeign, Spring Cloud Gateway (WebFlux), JJWT, Caffeine cache, Actuator, Postman

Designed & built a 4-service microservice backend: Users, Books, Issues (borrowing), and API Gateway; isolated schemas per service, clean domain boundaries, RESTful contracts.

Security & auth: Implemented JWT-based authentication/authorization with role scopes (USER/ADMIN), stateless sessions, and token blacklist (Caffeine) for logout/force-invalidate. Centralized auth at gateway with Authorization header relay.

API Gateway: Spring Cloud Gateway for routing, CORS, and token passthrough; exposed Actuator endpoints (health, gateway) for monitoring.

Business logic (Issues): Enforced domain rules—max 5 concurrent borrows/user, one copy per title per user, 30-day due date with penalty points (+1 per late return; block at 5 points). All updates transactional.

Inter-service calls: Used OpenFeign clients (Issues → Books, Issues → Users) with error handling and 403/401 propagation through gateway.

Data layer: Spring Data JPA with MySQL and HikariCP; entity design for Users, Books, Issues; repository patterns and DTOs for clean I/O.

Admin features: Manage users (promote/demote), catalogue CRUD, global visibility of loans; user features include signup/login, profile, list/borrow/return books, view my loans.

Ops & tooling: Centralized config via application.yml per service, structured logging, Postman collections/environments for end-to-end testing through gateway.

Highlights / challenges solved

Fixed JWT WeakKeyException by adopting a 256-bit HS256 secret; implemented robust token parsing/validation.

Resolved 403s on Feign calls by relaying the bearer token from gateway and aligning security filters across services.

Addressed CORS preflight failures at gateway; added explicit allow lists for origins/headers/methods.

Aligned Spring Cloud ↔ Spring Boot versions; migrated to new gateway property keys (WebFlux) and enabled gateway actuator endpoints.
