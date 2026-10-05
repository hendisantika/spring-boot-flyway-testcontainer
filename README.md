# spring-boot-flyway-testcontainer

A Spring Boot application that manages a PostgreSQL schema with **Flyway** migrations, runs the database locally
with **Docker Compose**, and verifies everything in tests using **Testcontainers**.

## Tech Stack

| Component        | Version                     |
|------------------|-----------------------------|
| Java             | 25                          |
| Spring Boot      | 4.1.1                       |
| Maven            | 3.10.0 (via Maven Wrapper)  |
| PostgreSQL       | 17.3 (`postgres:17.3-alpine3.21`) |
| Flyway           | managed by Spring Boot      |
| Testcontainers   | managed by Spring Boot      |
| pgAdmin          | 8.12.0                      |

## Prerequisites

- **JDK 25** (e.g. Temurin or Oracle JDK 25). Check with `java -version`.
- **Docker** running locally. Docker Compose (local run) and Testcontainers (tests) both need it.

If you have several JDKs installed on macOS, point Maven at JDK 25 like this:

```shell
export JAVA_HOME=$(/usr/libexec/java_home -v 25)
```

## Project Structure

```
src/main/java/id/my/hendisantika/flywaytestcontainer
├── SpringBootFlywayTestcontainerApplication.java
└── entity
    ├── Bookmark.java
    └── Category.java
src/main/resources
├── application.properties
└── db/migration
    ├── V1_20250217_1030__Create_Bookmarks_Table.sql
    ├── V2_20250217_1040__Add_Status_Category_to_Bookmarks.sql
    └── V3_20250217_1045__Add_Published_at_col_to_Bookmarks.sql
src/test/java/id/my/hendisantika/flywaytestcontainer
├── SpringBootFlywayTestcontainerApplicationTests.java
└── TestcontainersConfiguration.java
```

## Running the Application

```shell
./mvnw spring-boot:run
```

`spring-boot-docker-compose` starts the services in [`compose.yaml`](compose.yaml) automatically:

- **PostgreSQL** (`postgres17`): database `flyway`, user `yu71`, password `53cret`
- **pgAdmin**: http://localhost:5050, login `admin@localhost.com` / `admin`

On startup, Flyway applies the migrations in `src/main/resources/db/migration`, and Hibernate then validates the
schema (`spring.jpa.hibernate.ddl-auto=validate`). The application listens on http://localhost:8080.

## Running the Tests

```shell
./mvnw clean package
```

The tests use `TestcontainersConfiguration`, which starts a throwaway PostgreSQL container and wires it in through
`@ServiceConnection`, so no local database is needed. Each test run applies all Flyway migrations to a fresh database.

## Flyway Migrations

Migration files follow Flyway's naming convention `V<version>__<description>.sql`. Underscores in the version part
act as dots, so `V1_20250217_1030` becomes version `1.20250217.1030`.

| Version         | Description                          |
|-----------------|--------------------------------------|
| 1.20250217.1030 | Create `bookmarks` table             |
| 2.20250217.1040 | Create `categories` table; add `status` and `category_id` to bookmarks |
| 3.20250217.1045 | Add `published_at` column            |

To change the schema, add a new versioned script. Never edit a migration that has already been applied.

> **Note (Spring Boot 4):** Flyway auto-configuration now lives in its own module, so this project depends on
> `spring-boot-starter-flyway` rather than just `flyway-core`. Without the starter, migrations never run.

## CI

- GitHub Actions: [`.github/workflows/maven.yml`](.github/workflows/maven.yml) builds with Temurin JDK 25.
- GitLab CI: [`.gitlab-ci.yml`](.gitlab-ci.yml) builds with `maven:3-eclipse-temurin-25-alpine`.
