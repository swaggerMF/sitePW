# TripBoard

A university semester project for planning trips with friends: a shared itinerary, collaborative activity ideas and voting, and Splitwise-style expense tracking. Angular communicates with a Spring Boot REST API backed by PostgreSQL. No Docker or external travel services are required.

## Features

- Register and sign in with BCrypt password hashing and an eight-hour JWT.
- Create, view, edit and delete trips; add registered travelers by email or username.
- Plan activities chronologically, grouped by day.
- Suggest ideas, change your vote, and let the organizer accept or reject them. Accepted ideas can become itinerary activities.
- Create, view, edit and delete expenses with a selected payer and participants.
- Split expenses equally to the cent, calculate balances, suggest simplified transfers and record settlements.
- View upcoming plans, trip spending and your balance on the dashboard.
- Responsive desktop/mobile screens, validation, loading/empty states and destructive-action confirmations.

## Prerequisites and versions

| Component             | Version used                               |
| --------------------- | ------------------------------------------ |
| Angular / Angular CLI | 21.2.24                                    |
| TypeScript / RxJS     | 5.9.3 / 7.8.2                              |
| Java                  | 21; verified with Temurin 21.0.12.1+1      |
| Spring Boot           | 3.5.6                                      |
| Maven                 | 3.9.11, downloaded by the included wrapper |
| PostgreSQL            | 17; verified with 17.6                     |
| Node.js / npm         | verified with 20.19.5 / 9.2.0              |
| Playwright            | 1.56.1, development tests only             |

Install a **full Java 21 JDK**, PostgreSQL, and a compatible Node.js version. Angular 21 supports Node 20.19+, 22.12+, or 24.x; use a supported Node LTS release for your machine. See [Angular compatibility](https://angular.dev/reference/versions). Run `java -version`, `javac -version`, `node --version` and `psql --version` to check your tools. Set `JAVA_HOME` to the JDK directory if necessary.

The first Maven build and `npm ci` need internet access. Maven does not need to be installed separately. On Windows, use `backend\mvnw.cmd` instead of `backend/mvnw` and set environment variables in PowerShell or your IDE.

## 1. Create a local PostgreSQL database

Start PostgreSQL, then connect as its administrator. On many Linux installations:

```sh
sudo -u postgres psql
```

Inside `psql`:

```sql
CREATE ROLE tripboard LOGIN;
\password tripboard
CREATE DATABASE tripboard OWNER tripboard;
\q
```

Choose a local password at the prompt. The application account owns only its application database. On other operating systems, run the same SQL through `psql` or pgAdmin using your PostgreSQL administrator account.

## 2. Configure the backend

From the repository root:

```sh
cp .env.example .env
```

Edit `.env` with your local settings:

```dotenv
DB_URL=jdbc:postgresql://localhost:5432/tripboard
DB_USER=tripboard
DB_PASSWORD='your-local-database-password'
JWT_SECRET='replace-this-with-a-random-value-of-at-least-32-bytes'
DEMO_DATA=false
DEMO_PASSWORD='choose-a-demo-password-of-at-least-10-characters'
```

Generate a signing secret locally with `openssl rand -hex 32` and paste it into `JWT_SECRET`. Keep `.env` private; it is ignored by Git. The example contains placeholders, not working credentials. Quote values containing spaces or shell metacharacters.

| Variable        | Required / meaning                                           |
| --------------- | ------------------------------------------------------------ |
| `DB_URL`        | Defaults to `jdbc:postgresql://localhost:5432/tripboard`     |
| `DB_USER`       | Defaults to `tripboard`                                      |
| `DB_PASSWORD`   | Required; the password chosen above                          |
| `JWT_SECRET`    | Required; at least 32 UTF-8 bytes                            |
| `DEMO_DATA`     | Defaults to `false`; set to `true` to seed an empty database |
| `DEMO_PASSWORD` | Required when seeding; 10–72 UTF-8 bytes                     |

Spring Boot does **not** automatically read `.env`. The helper below exports it, or you can set the variables directly in your terminal/IDE. Only source an `.env` file you control.

## 3. Run the backend

From the repository root:

```sh
./scripts/run-backend.sh
```

This reads `.env` and runs `backend/mvnw spring-boot:run`. The API starts at `http://localhost:8080/api`. Flyway applies versioned migrations; Hibernate validates the resulting schema. Application restarts preserve PostgreSQL data.

Without the helper, export the variables above, then run:

```sh
cd backend
./mvnw spring-boot:run
```

In IntelliJ or another Java IDE, import `backend/pom.xml`, choose JDK 21 and set the environment variables on the `TripBoardApplication` run configuration.

## 4. Run the frontend

In a second terminal:

```sh
cd frontend
npm ci
npm start
```

Open **http://127.0.0.1:4200**. The development proxy forwards `/api` requests to `127.0.0.1:8080`, so no CORS configuration is needed. Register an account, create a trip and add another registered user. Use a separate browser profile to demonstrate multiple users.

## Demo data and credentials

Before the **first backend startup on an empty database**, set `DEMO_DATA=true` and choose `DEMO_PASSWORD` in `.env`. The seed creates:

| Email                   | Username | Password                   |
| ----------------------- | -------- | -------------------------- |
| `vlad@tripboard.demo`   | Vlad     | Your `DEMO_PASSWORD` value |
| `andrei@tripboard.demo` | Andrei   | Your `DEMO_PASSWORD` value |
| `mihai@tripboard.demo`  | Mihai    | Your `DEMO_PASSWORD` value |

The Iceland Road Trip includes five activities, €1,115 of expenses, three suggestions and votes. A second Dolomites trip demonstrates membership in another organizer’s trip. The seed skips databases that already contain users and never overwrites existing accounts. Set `DEMO_DATA=false` after seeding. There is no committed shared password.

## Build and test

Backend unit/integration tests and executable JAR, without requiring PostgreSQL:

```sh
cd backend
./mvnw clean verify
```

The default tests use H2 in PostgreSQL compatibility mode. They cover exact splitting and remainder cents, balances, settlements, debt simplification, vote uniqueness/change, conversion, authentication, validation and backend permissions. Production always uses PostgreSQL; H2 is test-scoped only.

Build the Angular production bundle:

```sh
cd frontend
npm ci
npm run build
```

Outputs:

- `backend/target/tripboard-1.0.0.jar`
- `frontend/dist/tripboard/browser/`

With the same environment exported, run the packaged backend with `java -jar backend/target/tripboard-1.0.0.jar`. For deployment, serve the frontend through a web server that falls back to `index.html` for Angular routes and proxies `/api` to Spring Boot; use HTTPS. `npm start` is the local development server.

### Test against real PostgreSQL

Create a **fresh, separate test database** owned by your test user. The tests insert fixed test accounts; they must not run against your demonstration or real application database. Export the following before running `./mvnw test` in `backend`:

```sh
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/tripboard_test
export SPRING_DATASOURCE_USERNAME=tripboard
export SPRING_DATASOURCE_PASSWORD='your-local-database-password'
export SPRING_DATASOURCE_DRIVER_CLASS_NAME=org.postgresql.Driver
export SPRING_JPA_HIBERNATE_DDL_AUTO=validate
export SPRING_FLYWAY_ENABLED=true
./mvnw test
```

Use a fresh test database for each run. These overrides enable the real migrations and database constraints instead of H2 schema generation.

### Browser workflow test

With both servers running against a disposable database:

```sh
cd frontend
npx playwright install chromium
npm run test:e2e
```

If Chromium is already installed, use `CHROMIUM_PATH=/usr/bin/chromium npm run test:e2e`. `BASE_URL` overrides the default frontend URL. The test creates uniquely named accounts, exercises the main user flows and mobile layouts, then deletes its trip. Test accounts remain in the disposable database. Screenshots are written to `/tmp/tripboard-browser-tests` by default; override `ARTIFACT_DIR` on other platforms.

## Business rules and deliberate scope

- The organizer manages the trip and membership. A content creator or organizer can edit/delete that content; ideas must still be open. Only the organizer decides ideas and converts accepted ones.
- One immutable currency per trip: EUR, RON or USD. Activities must fall within the trip dates; date edits cannot exclude existing activities. Expense dates may precede the trip to allow advance bookings.
- Equal splitting uses `BigDecimal` and whole cents. Remainder cents go to participants in ascending user-ID order. Shares may be zero when splitting a tiny expense among many people.
- **Positive balance means money to receive.** The correct formula is `paid − shares + settlements sent − settlements received`. This corrects the sign reversal in the original brief: when a debtor pays, both their debt and the creditor’s receivable decrease.
- Simplification greedily matches debtors and creditors; it does not guarantee the fewest possible transfers. Settlements record actual payments; no money is transferred by the app. A participant in the payment or the organizer can record one; its creator or organizer can delete a mistaken entry. Overpayments are allowed and can reverse a balance.
- Members with expense or settlement records cannot be removed, even at zero balance, so historical calculations stay complete. Their financial records must be removed/reassigned first. Past authored content and votes remain attributed to the original user.
- Closing an idea is final. Converting it is allowed once, even if the resulting activity is later deleted.
- The dashboard compares upcoming activity times with UTC. Activities use date/time values without a destination time zone; timezone-aware travel scheduling is outside this MVP.
- JWTs expire after eight hours. Tokens live in session storage; signing out clears the current tab. Refresh tokens, password reset, email verification and token revocation are outside this semester project.
- No booking, external travel APIs, payment processing, chat, maps, exchange rates or email invitations.

## Project structure

```text
backend/
  pom.xml, mvnw, mvnw.cmd, .mvn/   Maven project and wrapper
  src/main/java/com/tripboard/
    controller/                   REST endpoints; DTOs only
    service/                      Business rules, transactions and authorization
    repository/                   Spring Data JPA repositories
    entity/                       Hibernate relational model
    dto/, mapper/                 Validated requests and explicit response mapping
    security/, exception/         JWT, password hashing and JSON errors
  src/main/resources/db/migration/ Versioned PostgreSQL schema
  src/test/                       Business and API integration tests
frontend/
  src/app/core/, shared/          Auth, API access and reusable UI
  src/app/auth/, trips/           Account and trip pages
  src/app/itinerary/, expenses/   Planning and shared finances
  src/app/suggestions/, members/ Collaboration pages
scripts/                         Backend runner and browser regression test
docs/                            Requirements, technical specification, task history
```

See [requirements](docs/requirements.md), [technical specification](docs/technical-specification.md) and [task tracking](docs/tasks.md). `git log --oneline --reverse` shows the genuine incremental implementation history. No history rewriting or artificial empty commits are used.

## Troubleshooting

- **“release version 21 not supported”**: install the full JDK, not just a JRE. Set `JAVA_HOME` and check `javac -version`.
- **Database connection refused**: start PostgreSQL and check the host/port in `DB_URL`.
- **Password authentication failed**: use the password for the application’s PostgreSQL role, not an OS account password.
- **Missing `DB_PASSWORD` / `JWT_SECRET`**: use `scripts/run-backend.sh` or explicitly export the variables in the backend process’s environment.
- **Frontend cannot connect**: keep Spring Boot running on port 8080 or adjust `frontend/proxy.conf.json`.
- **No demo accounts**: seeding only runs when there are no users. Use a new demo database; do not clear a database containing work you want to keep.
- **Expired session**: sign in again. API authorization is enforced even when a browser has an old token.
