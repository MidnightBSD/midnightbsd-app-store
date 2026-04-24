# AGENTS.md

Guidance for AI coding agents working in this repository.

## Project Overview

This repository contains the MidnightBSD App Store web application and REST API.
It is a Maven-built Java 17 Spring Boot application with:

- Backend code under `src/main/java/org/midnightbsd/appstore`
- Unit and integration tests under `src/test/java`
- Flyway database migrations under `src/main/resources/db/migration`
- Thymeleaf templates under `src/main/resources/templates`
- AngularJS/Bootstrap static frontend assets under `src/main/resources/static`

The application depends on PostgreSQL, Elasticsearch, and Redis. It also imports
package metadata from Magus, the MidnightBSD package cluster software.

## Build And Test Commands

Use Maven from the repository root.

- `mvn test` runs unit tests via Surefire with the `test` Spring profile.
- `mvn integration-test` runs integration tests matching `IT*.java` via Failsafe with the `it` Spring profile.
- `mvn -DskipTests clean package` builds the application artifact without tests.
- `mvn spring-boot:run` starts the application locally.

The CI pipeline also prepares a local PostgreSQL database named
`app_store_test` and loads `src/main/resources/db/migration/V1_0__appstore_create.sql`
before running tests.

Notes:

- The `pom.xml` sets Java source/target to 17.
- Surefire and Failsafe currently have `testFailureIgnore` enabled. Do not treat
  a successful Maven exit code alone as proof that tests passed; inspect test
  output and reports when validating changes.
- JaCoCo enforces package-level line coverage with a minimum covered ratio of
  `0.50`.

## Runtime Configuration

Configuration lives in `src/main/resources/application.yml` and
`src/main/resources/bootstrap.yml`.

Spring profiles:

- `default` uses configured remote development services.
- `test` uses local PostgreSQL database `app_store_test`.
- `it` uses local PostgreSQL database `app_store_test`.
- `prod` uses production service locations.

When changing configuration, avoid committing local secrets or machine-specific
values. Prefer profile-specific configuration and environment overrides.

## Code Organization

The main Java packages are:

- `config`: Spring configuration classes.
- `ctl` and `ctl.api`: MVC and REST controllers.
- `exception`: application exceptions.
- `model`: JPA/domain models and search DTOs.
- `repository`: Spring Data repositories.
- `services`: business logic and integration services.

Keep the existing layering:

- Controllers should be thin and delegate to services.
- Services should own business rules and transactional boundaries.
- Repositories should remain persistence-focused Spring Data interfaces.
- Models should avoid controller-specific behavior.

## Style Guidelines

- Follow the existing Java style: 4-space indentation, constructor injection,
  `final` parameters where already used, and package names under
  `org.midnightbsd.appstore`.
- Prefer JUnit 5 and Mockito for unit tests, matching existing tests.
- Keep API routes consistent with the current `/api/...` controller structure.
- For database schema changes, add a new Flyway migration instead of editing
  existing migrations that may have already run.
- For frontend changes, preserve the current AngularJS 1.x and Bootstrap
  structure unless the task explicitly asks for a broader frontend migration.

## Dependency And Version Guidance

- Do not upgrade Spring Boot, Java, AngularJS, Bootstrap, database drivers, or
  Maven plugins casually. Version changes should be intentional and tested.
- If adding a dependency, prefer one that fits the existing Spring Boot/Maven
  ecosystem and explain why it is needed.
- Keep generated build output in `target/` out of commits.

## Validation Expectations

For backend changes, run the most focused relevant test first, then broader
tests when practical:

- Single test class: `mvn -Dtest=ClassName test`
- Unit test suite: `mvn test`
- Integration suite: `mvn integration-test`

For controller or service changes, add or update tests in the corresponding
`src/test/java/org/midnightbsd/appstore/ctl` or
`src/test/java/org/midnightbsd/appstore/services` package.

If local PostgreSQL, Elasticsearch, or Redis are unavailable, state which tests
could not be run and why.

## Agent Workflow Notes

- Check `git status --short` before editing. This repository may already contain
  user changes; do not overwrite or revert them unless explicitly asked.
- Make minimal, targeted edits that fit the existing architecture.
- Prefer `rg` for searching code and file names.
- Do not edit generated files, IDE metadata, or `target/` artifacts.
- When modifying application behavior, update tests or explain why no practical
  test change was made.
