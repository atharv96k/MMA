<p align="center">
  <img src="docs/logo.png" alt="Money Manager API" width="120" />
</p>

<h1 align="center">Money Manager API</h1>

<p align="center">
  A Spring Boot REST API for personal finance tracking — income, expenses, categories, a spending dashboard, filters, and Excel reports, secured with JWT and email activation.
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-21-orange?logo=openjdk&logoColor=white" alt="Java 21" />
  <img src="https://img.shields.io/badge/Spring%20Boot-3.5.3-6DB33F?logo=springboot&logoColor=white" alt="Spring Boot 3.5.3" />
  <img src="https://img.shields.io/badge/MySQL-compatible-4479A1?logo=mysql&logoColor=white" alt="MySQL" />
  <img src="https://img.shields.io/badge/build-Maven-C71A36?logo=apachemaven&logoColor=white" alt="Maven" />
  <img src="https://img.shields.io/badge/license-MIT-blue" alt="License" />
</p>

---

**Get started** (2 minutes):

​```bash
git clone https://github.com/<your-username>/<repo-name>.git
cd <repo-name>
cp .env.example .env        # fill in DB, mail, and JWT values — see below
mvn clean package -DskipTests
java -jar target/moneymanager-0.0.1-SNAPSHOT.jar
​```

The API is live at `http://localhost:8080/api/v1.0`.

---

## What it does

| Capability | Details |
|---|---|
| **Authentication** | Register with email activation, JWT login, fully stateless sessions |
| **Categories** | Create, list, and update custom income/expense categories per user |
| **Income & Expenses** | Add, list, and delete transactions |
| **Dashboard** | Aggregated recent activity and totals |
| **Filtering** | Query transactions by type, category, date range, and keyword |
| **Excel export** | Download income/expense records as `.xlsx` |
| **Email reports** | Send income/expense Excel reports straight to your inbox |
| **Scheduled notifications** | Daily jobs run automatically at 10 PM and 11 PM IST |
| **Global exception handling** | Consistent JSON error responses across every endpoint |

---

## Tech stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.5.3 |
| Security | Spring Security, JWT (`jjwt`) |
| Persistence | Spring Data JPA, Hibernate |
| Database | MySQL (PostgreSQL driver also on the classpath) |
| Email | Spring Mail (SMTP) |
| Reports | Apache POI (`.xlsx` generation) |
| Build | Maven |
| Boilerplate reduction | Lombok |

---

## Project structure

​```
src/main/java/in/master/moneymanager/
├── config/       Security configuration (JWT filter chain, CORS, password encoding)
├── controller/   REST endpoints
├── dto/          Request/response data transfer objects
├── entity/       JPA entities — Category, Expense, Income, Profile
├── exception/    Custom exceptions + global exception handler
├── repository/   Spring Data JPA repositories
├── security/     JWT request filter
├── service/      Business logic
└── util/         JWT utility (token generation/validation)
​```

---

## Prerequisites

| Requirement | Notes |
|---|---|
| Java 21 (JDK) | `java -version` to check |
| Maven | Or use the included wrapper, if present |
| MySQL database | A free instance from [Aiven](https://aiven.io) works well |
| SMTP account | For activation + report emails (e.g. Gmail with an app password) |

---

## Environment variables

Everything is configured via environment variables — nothing is hardcoded. Create a `.env` in the project root for local dev (loaded automatically via `spring-dotenv`), or set these on your hosting platform directly.

| Variable | Description | Example |
|---|---|---|
| `DB_URL` | JDBC connection string | `jdbc:mysql://host:3306/defaultdb?sslMode=REQUIRED` |
| `DB_USERNAME` | Database username | `avnadmin` |
| `DB_PASSWORD` | Database password | — |
| `MAIL_HOST` | SMTP host | `smtp.gmail.com` |
| `MAIL_PORT` | SMTP port | `587` |
| `MAIL_USERNAME` | SMTP account username | — |
| `MAIL_PASSWORD` | SMTP account password / app password | — |
| `MAIL_FROM` | "From" address for outgoing emails | `noreply@yourapp.com` |
| `JWT_SECRET` | Secret key used to sign JWTs | a long random string |
| `FRONTEND_URL` | Deployed frontend origin, used for CORS | `https://yourapp.vercel.app` |
| `ACTIVATION_URL` | Base URL the activation email links to | `https://yourapp.com/activate` |
| `PORT` | Server port | most hosts set this automatically |
| `SPRING_PROFILES_ACTIVE` | Set to `prod` on deployment to quiet debug logging | `prod` |

> **JDBC SSL parameter:** use `sslMode` (camelCase) — that's what MySQL Connector/J actually reads. `ssl-mode` (Aiven's own display convention) is silently ignored by the Java driver.

---

## Running locally

​```bash
mvn clean package -DskipTests
java -jar target/moneymanager-0.0.1-SNAPSHOT.jar
​```

Or skip the jar and run directly:

​```bash
mvn spring-boot:run
​```

<details>
<summary><strong>On an IPv6-only / NAT64 network?</strong></summary>

<br>

Some ISPs and campus Wi-Fi networks are IPv6-only and translate IPv4 hosts via NAT64. `ping` and Windows tools succeed transparently, but Java's resolver can throw `UnknownHostException` on the same hostname. Fix it with:

​```
-Djava.net.preferIPv6Addresses=true
​```

Add it as a JVM argument (IDE run config → VM arguments, or prefix your `java -jar` command).

</details>

---

## API reference

All endpoints are prefixed with `/api/v1.0`. Everything except the five public routes below requires a valid JWT in the `Authorization` header.

| Method | Endpoint | Description | Auth |
|---|---|---|:---:|
| GET | `/status`, `/health` | Health check | — |
| POST | `/register` | Register a new user | — |
| GET | `/activate` | Activate account via emailed link | — |
| POST | `/login` | Authenticate, receive a JWT | — |
| GET | `/profile` | Current user's profile | ✅ |
| POST | `/categories` | Create a category | ✅ |
| GET | `/categories` | List categories | ✅ |
| GET | `/categories/{type}` | List categories by type | ✅ |
| PUT | `/categories/{categoryId}` | Update a category | ✅ |
| POST | `/incomes` | Add an income entry | ✅ |
| GET | `/incomes` | List income entries | ✅ |
| DELETE | `/incomes/{id}` | Delete an income entry | ✅ |
| POST | `/expenses` | Add an expense entry | ✅ |
| GET | `/expenses` | List expense entries | ✅ |
| DELETE | `/expenses/{id}` | Delete an expense entry | ✅ |
| GET | `/dashboard` | Dashboard summary | ✅ |
| POST | `/filter` | Filter transactions | ✅ |
| GET | `/excel/download/income` | Download income report (.xlsx) | ✅ |
| GET | `/excel/download/expense` | Download expense report (.xlsx) | ✅ |
| GET | `/email/income-excel` | Email income report | ✅ |
| GET | `/email/expense-excel` | Email expense report | ✅ |

---

## Deployment

This repo ships a multi-stage `Dockerfile` that builds from source — no local Docker install and no pre-built jar required. Push to Git and deploy on any Docker-capable platform (Render, Koyeb, etc.).

​```bash
# What the platform runs on your behalf:
docker build -t moneymanager .
docker run -p 8080:8080 --env-file .env moneymanager
​```

Set `SPRING_PROFILES_ACTIVE=prod` in your deployment environment to quiet the verbose SQL/Hibernate logging that's useful locally but noisy in production. All environment variables above except `PORT` are required for a working deployment.

---

## Security notes

- Passwords are hashed with BCrypt.
- Authentication is stateless — JWTs are validated per-request via a custom filter.
- `spring.jpa.hibernate.ddl-auto=update` is convenient in development; once real data exists, consider Flyway or Liquibase for controlled schema migrations instead.
- Never commit `.env` or real credentials — confirm `.gitignore` excludes it before your first push.

---

<p align="center">Built with Spring Boot · MIT Licensed</p>
