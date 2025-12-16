<!-- Project README - professional, concise, and actionable -->
# Project Management System

A lightweight project management web application (Spring Boot + Thymeleaf backend, optional Vite + React frontend) built to help teams track projects, tasks, teams and attachments.

This repository contains a server-rendered Java Spring Boot application with a small React/Vite frontend scaffold under `/frontend` (used for progressive enhancement). Templates are Thymeleaf-based and static assets live under `src/main/resources/static`.

---

## Highlights

- Spring Boot 4.x (Java 25)
- Thymeleaf server-side templates
- JPA/Hibernate with PostgreSQL
- Structured services, repositories and controllers
- Centralized styling in `src/main/resources/static/css` with `style.css` and `style.enhancements.css`
- Attachments upload/download support and basic auth flows

---

## Quickstart (Development)

Prerequisites (macOS / zsh):

- Java 25 SDK installed and `JAVA_HOME` configured
- PostgreSQL accessible (see `src/main/resources/application.properties`)
- Maven wrapper is included (`./mvnw`)
- Node.js + npm (optional, for frontend asset tooling)

1. Install and configure PostgreSQL

   - Create a database named `project_management` (or adjust `application.properties` accordingly)

2. Build backend (skip tests for faster iteration):

```bash
./mvnw -DskipTests package
```

3. Run the application:

```bash
./mvnw -Dspring-boot.run.profiles=dev spring-boot:run
```

By default the app serves on `http://localhost:8080/`.

4. (Optional) Frontend development

The repo contains a `frontend/` folder scaffolded with Vite + React. To run it independently:

```bash
cd frontend
npm install
npm run dev
```

When building the frontend for production, copy the `dist/` output into `src/main/resources/static/frontend` (this repo previously copied the built SPA into static assets).

---

## Useful Commands

- Run tests: `./mvnw test`
- Run app: `./mvnw spring-boot:run`
- Build packaged jar: `./mvnw -DskipTests package`
- Lint / format JS/CSS: use tools inside `frontend/` (if used)

---

## Configuration

Edit `src/main/resources/application.properties` for database credentials, ports, and other environment-specific settings. Prefer to override via environment variables in production.

Recommended environment variables:

- `SPRING_DATASOURCE_URL` — JDBC URL
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`

---

## Development Notes & Maintenance

- Styles: keep all global overrides in `style.enhancements.css`. When changes are finalized, regenerate `style.min.css` by concatenating `style.css` + `style.enhancements.css` and minifying. Example (simple concat fallback):

```bash
cat src/main/resources/static/css/style.css src/main/resources/static/css/style.enhancements.css > src/main/resources/static/css/style.min.css
```

- Logging: backend uses SLF4J; prefer `logger.error/warn/info/debug` inside controllers and services instead of `printStackTrace()`.
- Database counts and heavy queries: prefer repository-level count queries (e.g. `countByManagerId(...)`) to avoid loading large collections into memory.

---

## Troubleshooting

- "Port 8080 already in use": find and stop other Java processes using the port: `lsof -i :8080` then `kill <pid>`
- Database errors: check `application.properties` and ensure the DB is reachable and migrations (if any) are applied.
- Authentication problems: check `SecurityConfig` and ensure correct `UserDetailsService` or registered `AuthenticationProvider`.

---

## Contribution Guide

- Branch from `main` and open pull requests with concise descriptions.
- Run the test-suite before submitting changes.
- Keep changes scoped: UI-only changes should not modify backend logic unless required.

---

## License

This repository does not include a license file by default. Add a `LICENSE` file if you intend to publish under an open source license.

---

If you'd like, I can also:

- regenerate and commit `style.min.css`,
- add a `CONTRIBUTING.md` with development conventions, or
- produce a short `DEPLOY.md` with production deployment steps.

---

Last updated: 2025-12-16
