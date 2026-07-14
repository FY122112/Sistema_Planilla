# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project overview

Spring Boot 3.5 / Java 17 backend for **Sistema_Planilla**, a payroll (planilla) management system for a textile
company (TextiLima SAC). It manages employees, positions, banks, pension systems, attendance, payroll periods,
payroll concepts, and payslips ("boletas"), with JWT-based authentication.

`README.md` at the repo root is not a normal readme — it is the full product backlog (Historias de Usuario
HU-001–HU-060), grouped into 4 modules: Planilla General, Registro de Trabajadores, Control de Tiempos y
Remuneración, and Boletas/Autogestión. Each entry has an `Estado` (Completado/Pendiente). Consult it to understand
what a feature request is actually asking for and whether related pieces already exist.

## Commands

Build/run via the Maven wrapper (`mvnw.cmd` on Windows, `./mvnw` on Unix):

```
mvnw.cmd clean compile          # compile
mvnw.cmd spring-boot:run        # run the app locally
mvnw.cmd test                   # run all tests
mvnw.cmd test -Dtest=ClassName  # run a single test class
mvnw.cmd clean package          # build the jar
```

The app requires a local MySQL instance (`sistema_planilla` schema on `localhost:3306`); connection settings are in
`src/main/resources/application.properties`. `spring.jpa.hibernate.ddl-auto=update`, so schema changes follow from
entity changes — there are no separate migration scripts.

Test coverage is currently minimal (a single Spring context-load smoke test).

## Architecture

The codebase follows **hexagonal architecture (ports & adapters)** under
`src/main/java/com/todocodeacademy/sistema_planilla/` (note: the package segment is spelled `aplication`, not
`application` — match the existing typo when adding files, don't "fix" it in isolation).

```
domain/model/                          Pure domain classes (no framework annotations except Lombok @Getter)
aplication/ports/input/*ServicePort    Use-case interfaces (one per aggregate, e.g. EmpleadoServicePort)
aplication/ports/output/*RepositoryPort  Persistence interfaces the domain/services depend on
aplication/service/*Service            Implements the input port, depends only on output ports + domain
infraestructure/entity/*Entity         JPA @Entity classes (persistence model, separate from domain/model)
infraestructure/repository/Jpa*Repository   Spring Data JPA interfaces
infraestructure/repository/adapter/*RepositoryAdapter  Implements the output port; wraps the Jpa repo + entity mapper
infraestructure/mapper/*EntMapper      Entity <-> domain mapping (hand-written, NOT MapStruct)
infraestructure/input/controllers/*Controller  @RestController, depends on the *ServicePort + input mapper
infraestructure/input/dto/Request|Response     Request/response DTOs (Java records)
infraestructure/input/mapper/*Mapper   DTO <-> domain mapping (hand-written; may inject output ports to resolve
                                        foreign keys like Puesto/Banco/SistemaPension from an id in the request)
infraestructure/security/              JWT auth: SecurityConfig, JwtTokenValidator (filter), JwtUtils, UserDetailsService
```

Key points:

- **Two distinct mapper layers exist and are not interchangeable**: `infraestructure/mapper` converts between JPA
  entities and domain objects (used by repository adapters); `infraestructure/input/mapper` converts between DTOs
  and domain objects (used by controllers). Don't conflate them.
- Although `mapstruct` is a declared dependency in `pom.xml`, no `@Mapper` interfaces exist — every mapper in the
  project today is a hand-written `@Component`/`@RequiredArgsConstructor` class. Follow that convention for new
  mappers rather than introducing MapStruct-generated ones, unless asked to change this.
- Domain objects (`domain/model`) are rich models: they validate invariants in constructors/mutators (throwing
  `IllegalArgumentException`/`IllegalStateException`), expose intention-revealing methods (e.g.
  `Empleado.cesarEmpleado`, `actualizarDatosPersonales`, `asignarBanco`) instead of plain setters, and provide a
  static `reconstruir(...)` factory used by entity mappers to rehydrate an object from persisted data. Controllers
  and services never touch JPA entities directly.
- Services in `aplication/service` orchestrate business rules across output ports (e.g. `EmpleadoService.save`
  checks `findByNumeroDocumento` via the repository port before delegating the actual insert) — that's where
  cross-aggregate validation like "no duplicate DNI" belongs, not in the controller or mapper.
- When adding a new aggregate/module, replicate the full vertical slice used by `Empleado` (or a smaller one like
  `Banco`): domain model -> input port -> output port -> service -> JPA entity -> JPA repository -> repository
  adapter -> entity mapper -> DTOs (Request/Response) -> input mapper -> controller.

## Security

Stateless JWT auth configured in `infraestructure/security/config/SecurityConfig.java`:

- `JwtTokenValidator` (a custom `OncePerRequestFilter`) runs before `BasicAuthenticationFilter`, reads the
  `Authorization: Bearer <token>` header, validates it via `JwtUtils` (Auth0 `java-jwt`, HMAC256, signed with
  `security.jwt.private.key` from `application.properties`), and populates the `SecurityContext` with a
  `UsernamePasswordAuthenticationToken` built from the token's `authorities` claim.
- `/api/banco/**`, `/api/usuarios/**`, `/api/roles/**`, and `/auth/login/**` are `permitAll()`; everything else
  requires a valid token (`anyRequest().authenticated()`).
- `@EnableMethodSecurity` is on, so method-level `@PreAuthorize`-style checks are expected to be used for
  finer-grained authorization beyond the URL patterns above.
- Passwords are hashed with `BCryptPasswordEncoder`.
