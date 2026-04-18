# Spring-WebApplication

![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.4-brightgreen?style=flat-square&logo=springboot)
![H2 Database](https://img.shields.io/badge/Database-H2%20(In--Memory)-blue?style=flat-square)
![Maven](https://img.shields.io/badge/Build-Maven-C71A36?style=flat-square&logo=apachemaven)
![Lombok](https://img.shields.io/badge/Lombok-1.18.38-red?style=flat-square)

A Java 21 backend web application built with **Spring Boot 3.5.4**, implementing a fully functional REST API for managing **Employees** and **Departments**. The project focuses on clean layered architecture, robust input validation (including custom annotations), global exception handling, and DTO-based data transfer using ModelMapper — all backed by an in-memory H2 database.

---

##  Project Objective

The goal of this project is to build and demonstrate a production-style Spring Boot REST API for learning purposes, covering:

- CRUD operations for two domain entities: **Employee** and **Department**
- Clean separation of concerns using Controller → Service → Repository layers
- DTO pattern to decouple API contracts from database entities, mapped via **ModelMapper**
- Standard and **custom validation annotations** enforced at the DTO level
- Structured, consistent error responses through a **global exception handler** using `@RestControllerAdvice`
- Partial updates via HTTP `PATCH` using Java Reflection (`ReflectionUtils`)
- Lightweight persistence using an **H2 in-memory database** with Spring Data JPA

---

##  Project Structure

```
Spring-WebApplication/
├── .mvn/wrapper/
│   └── maven-wrapper.properties
├── src/
│   ├── main/
│   │   ├── java/com/SpringBootWebTutorial/Module2/WebTutorial/
│   │   │   ├── WebTutorialApplication.java          # @SpringBootApplication entry point
│   │   │   ├── advices/
│   │   │   │   ├── ApiError.java                    # Structured error response model (@Data @Builder)
│   │   │   │   └── CustomGlobalException.java       # @RestControllerAdvice – global exception handler
│   │   │   ├── annotations/
│   │   │   │   ├── EmployeeRoleValidation.java       # Custom annotation: validates role = ADMIN | USER
│   │   │   │   ├── EmployeeRoleValidator.java        # ConstraintValidator logic for role validation
│   │   │   │   ├── Prime.java                       # Custom annotation: validates age is a prime number
│   │   │   │   └── IsPrime.java                     # ConstraintValidator logic for prime number check
│   │   │   ├── config/
│   │   │   │   └── MapperConfig.java                # @Bean definition for ModelMapper
│   │   │   ├── controllers/
│   │   │   │   ├── EmployeeController.java          # REST endpoints under /employee
│   │   │   │   └── DepartmentController.java        # REST endpoints under /departments
│   │   │   ├── dto/
│   │   │   │   ├── EmployeeDTO.java                 # Employee data transfer object with validations
│   │   │   │   └── DepartmentDTO.java               # Department data transfer object with validations
│   │   │   ├── entities/
│   │   │   │   ├── EmployeeEntity.java              # JPA entity mapped to "employee" table
│   │   │   │   └── DepartmentEntity.java            # JPA entity for departments
│   │   │   ├── exceptions/
│   │   │   │   └── ResourceNotFoundException.java  # Custom RuntimeException for 404 scenarios
│   │   │   ├── repositories/
│   │   │   │   ├── EmployeeRepo.java                # JpaRepository<EmployeeEntity, Long>
│   │   │   │   └── DepartmentRepo.java              # JpaRepository<DepartmentEntity, Long>
│   │   │   └── services/
│   │   │       ├── EmployeeService.java             # Business logic for Employee operations
│   │   │       └── DepartmentService.java           # Business logic for Department operations
│   │   └── resources/
│   │       └── application.properties              # App name + H2 datasource config
│   └── test/
│       └── java/com/SpringBootWebTutorial/Module2/WebTutorial/
│           └── WebTutorialApplicationTests.java     # Spring Boot context load test
├── pom.xml
├── mvnw / mvnw.cmd
└── .gitignore
```

---

##  Key Features

###  Full CRUD REST API

Two complete resource endpoints are exposed:

**Employee** — base path `/employee`

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/employee` | Fetch all employees |
| `GET` | `/employee/{employeeID}` | Fetch employee by ID |
| `POST` | `/employee` | Create a new employee |
| `PUT` | `/employee/{employeeId}` | Full update of an employee |
| `PATCH` | `/employee/{employeeId}` | Partial field update of an employee |
| `DELETE` | `/employee/{employeeId}` | Delete an employee |
| `GET` | `/employee/greet` | Returns a greeting string |
| `GET` | `/employee/getSecretMessage` | Returns a secret message |

**Department** — base path `/departments`

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/departments` | Fetch all departments |
| `GET` | `/departments/{Id}` | Fetch department by ID |
| `POST` | `/departments` | Create a new department |
| `PUT` | `/departments/{Id}` | Full update of a department |
| `GET` | `/departments/hello` | Returns a hello string |

---

###  Clean Layered Architecture

Strict separation of concerns across three layers:

- **Controller** — handles HTTP routing, delegates to the Service layer, and returns `ResponseEntity<>` with proper HTTP status codes
- **Service** — contains all business logic; uses ModelMapper to convert between DTO and Entity before persisting or returning data
- **Repository** — extends `JpaRepository<Entity, Long>`, providing all CRUD operations through Spring Data with zero SQL

---

###  DTO Pattern with ModelMapper

`EmployeeDTO` and `DepartmentDTO` serve as the API's public contracts, fully decoupled from the JPA entity classes. A `ModelMapper` bean configured in `MapperConfig.java` is injected into both `EmployeeService` and `DepartmentService`, handling all DTO ↔ Entity conversions with a single `.map()` call.

---

###  Validation with Custom Annotations

Input is validated at the DTO level using Jakarta Bean Validation. Beyond standard constraints, two custom annotations were implemented from scratch:

**`@EmployeeRoleValidation`**
Ensures the `role` field accepts only `"ADMIN"` or `"USER"`. The backing `EmployeeRoleValidator` implements `ConstraintValidator<EmployeeRoleValidation, String>` and performs a `List.of("ADMIN", "USER").contains(value)` check.

**`@Prime`**
Ensures the employee `age` field is a prime number. The backing `IsPrime` implements `ConstraintValidator<Prime, Integer>` using an optimised O(√n) primality algorithm — skipping even numbers after 2 and iterating only up to the square root.

Standard annotations used across DTOs: `@NotBlank`, `@Size(min, max)`, `@Email`, `@Min`, `@Max`, `@AssertTrue`, `@JsonProperty`.

---

###  Global Exception Handling

`CustomGlobalException` is annotated with `@RestControllerAdvice` and handles exceptions across all controllers, returning a consistent `ApiError` JSON structure:

- `ResourceNotFoundException` → **404 NOT FOUND** — thrown in Service when an entity ID does not exist
- `MethodArgumentNotValidException` → **400 BAD REQUEST** — triggered on `@Valid` failures; all field-level messages are collected into the `subErrors` list
- `Exception` → **500 INTERNAL SERVER ERROR** — fallback handler for unexpected errors

`ApiError` is built with Lombok's `@Data` and `@Builder`, carrying `status` (HttpStatus), `message` (String), and `subErrors` (List\<String>).

---

###  Partial Update via PATCH + Java Reflection

`PATCH /employee/{employeeId}` accepts a `Map<String, Object>` of only the fields the client wants to change. The service uses `ReflectionUtils.findField(EmployeeEntity.class, fieldName)` and `ReflectionUtils.setField(field, entity, value)` to apply changes dynamically — meaning only the provided fields are updated, with all others left untouched.

---

###  H2 In-Memory Database

No external database setup is required. Spring Boot auto-configures an in-memory H2 instance on startup. The `application.properties` file also contains commented-out configuration for connecting to a persistent H2 file store, making it straightforward to switch persistence modes during development.

---

###  Lombok for Boilerplate Elimination

All entity and DTO classes use `@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor`. `ApiError` uses `@Data` and `@Builder`. This eliminates hundreds of lines of boilerplate without sacrificing clarity.

---

##  Challenges Faced & Optimisations Made

### 1. DTO ↔ Entity Conversion Boilerplate

**Challenge:** Manually mapping fields between `EmployeeDTO` ↔ `EmployeeEntity` and `DepartmentDTO` ↔ `DepartmentEntity` in every service method was verbose and fragile — any new field added to an entity required updates across multiple mapping blocks.

**Optimisation:** Integrated **ModelMapper 3.0.0** and registered it as a Spring `@Bean` in `MapperConfig.java`. Every service method now performs conversion with a single `modelMapper.map(source, Target.class)` call, keeping the service layer clean and automatically handling new fields.

---

### 2. Standard Validation Annotations Were Not Enough

**Challenge:** Jakarta's built-in annotations could not express the application's domain rules — restricting `role` to only `ADMIN`/`USER`, and requiring `age` to be a prime number have no standard constraint equivalent.

**Optimisation:** Implemented two custom annotations. `@EmployeeRoleValidation` with `EmployeeRoleValidator` performs a whitelist check against `List.of("ADMIN", "USER")`. `@Prime` with `IsPrime` uses an O(√n) primality test — efficiently ruling out even numbers first, then checking odd divisors only up to the square root. Both integrate seamlessly into Spring's validation pipeline via `@Constraint(validatedBy = ...)`.

---

### 3. Inconsistent and Scattered Error Handling

**Challenge:** Exception handlers were initially scoped per controller using `@ExceptionHandler` inside `EmployeeController`, making error handling non-reusable. Each controller would need its own duplicate handlers, and response shapes were inconsistent.

**Optimisation:** Lifted all handlers into `CustomGlobalException` using `@RestControllerAdvice` — making it globally applicable to every controller. The `ApiError` model with `@Builder` ensures every error response, whether a 404, 400, or 500, shares the same structured JSON format with `status`, `message`, and an optional `subErrors` list.

---

### 4. Partial Updates Required Sending the Entire Object

**Challenge:** `PUT` requires a full request body, meaning updating a single field like `name` would require the client to resend all fields — risking accidental overwrites if any field was omitted.

**Optimisation:** Added a dedicated `PATCH /employee/{employeeId}` endpoint that accepts a `Map<String, Object>`. The service retrieves the existing entity, then applies only the provided fields using Spring's `ReflectionUtils` — leaving all other fields untouched. This gives clients precise, lightweight partial update capability.

---

### 5. Validation Failures Returned Vague Error Messages

**Challenge:** When a `POST` request failed multiple validation rules at once (e.g., blank name, invalid email, wrong role), the default Spring error response gave no breakdown of which fields failed and why.

**Optimisation:** The `HandleMethodArgumentNotValid` handler in `CustomGlobalException` streams over `exception.getBindingResult().getAllErrors()`, maps each error to its `defaultMessage`, and collects them into the `subErrors` list of the `ApiError` response — giving clients a complete, actionable breakdown of every constraint violation in a single response.

---

##  Tech Stack

| Technology | Version | Purpose |
|---|---|---|
| Java | 21 | Core language |
| Spring Boot | 3.5.4 | Framework & auto-configuration |
| Spring Web (MVC) | — | REST API layer, embedded Tomcat |
| Spring Data JPA | 3.5.3 | ORM & repository abstraction |
| H2 Database | — | In-memory relational database |
| Hibernate | — | JPA implementation |
| Lombok | 1.18.38 | Boilerplate reduction |
| ModelMapper | 3.0.0 | DTO ↔ Entity mapping |
| Spring Validation | — | Jakarta Bean Validation |
| Maven | Wrapper included | Build & dependency management |

---

##  Getting Started

### Prerequisites

- Java 21+
- No external database required — H2 runs in-memory automatically

### Clone & Run

```bash
git clone https://github.com/HEYDEV001/Spring-WebApplication.git
cd Spring-WebApplication

# Linux / macOS
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

The application starts at `http://localhost:8080`

### Run Tests

```bash
./mvnw test
```
---

##  Author

**Dev Pathak** — [HEYDEV001](https://github.com/HEYDEV001)
