# Money Manager API

A Spring Boot REST API for personal finance tracking — manage income, expenses, and categories, view a spending dashboard, filter transactions, and export reports to Excel. Secured with JWT-based authentication and email account activation.

## Features

- **Authentication** — Register with email activation, JWT-based login, stateless session management
- **Categories** — Create, list, and update custom income/expense categories per user
- **Income & Expense tracking** — Add, list, and delete transactions
- **Dashboard** — Aggregated view of recent activity and totals
- **Filtering** — Query transactions by type, category, date range, and keyword
- **Excel export** — Download income and expense records as `.xlsx` reports
- **Email reports** — Receive income/expense Excel reports directly via email
- **Scheduled notifications** — Daily reminder/summary jobs run automatically at 10 PM and 11 PM IST
- **Global exception handling** — Consistent JSON error responses across the API

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.5.3 |
| Security | Spring Security, JWT (jjwt) |
| Persistence | Spring Data JPA, Hibernate |
| Database | MySQL (PostgreSQL driver also available) |
| Email | Spring Mail (SMTP) |
| Reports | Apache POI (Excel `.xlsx` generation) |
| Build | Maven |
| Boilerplate reduction | Lombok |

## Project Structure

```
src/main/java/in/master/moneymanager/
├── config/          # Security configuration (JWT filter chain, CORS, password encoding)
├── controller/       # REST endpoints
├── dto/              # Request/response data transfer objects
├── entity/           # JPA entities (Category, Expense, Income, Profile)
├── exception/        # Custom exceptions + global exception handler
├── repository/       # Spring Data JPA repositories
├── security/         # JWT request filter
├── service/          # Business logic
└── util/             # JWT utility (token generation/validation)
```

## Prerequisites

- Java 21 (JDK)
- Maven (or use the included wrapper, if present)
- A MySQL database (a free instance from [Aiven](https://aiven.io) works well)
- An SMTP-capable email account for sending activation and report emails (e.g. Gmail with an app password)

## Environment Variables

The application is fully configured via environment variables — no values are hardcoded. Create a `.env` file in the project root for local development (supported via `spring-dotenv`), or set these directly on your hosting platform.

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
| `PORT` | Server port (optional — most hosts set this automatically) | `8080` |

> **Note on JDBC SSL parameter:** use `sslMode` (camelCase), not `ssl-mode` — that's the parameter name MySQL Connector/J actually recognizes.

## Running Locally

1. Clone the repository and navigate into the project folder.
2. Create a `.env` file in the project root with the variables listed above.
3. Build and run:
   ```bash
   mvn clean package -DskipTests
   java -jar target/moneymanager-0.0.1-SNAPSHOT.jar
   ```
   Or run directly with Maven:
   ```bash
   mvn spring-boot:run
   ```
4. The API will be available at `http://localhost:8080/api/v1.0`.

### Note for IPv6-only networks

If you're on a network that uses NAT64/DNS64 (common on some ISPs and campus Wi-Fi), Java may fail to resolve cloud database hostnames with an `UnknownHostException`, even though `ping` succeeds. If you hit this, add the following JVM argument:
```
-Djava.net.preferIPv6Addresses=true
```

## API Overview

All endpoints are prefixed with `/api/v1.0`. Endpoints other than `/status`, `/health`, `/register`, `/activate`, and `/login` require a valid JWT in the `Authorization` header.

| Method | Endpoint | Description | Auth required |
|---|---|---|---|
| GET | `/status`, `/health` | Health check | No |
| POST | `/register` | Register a new user | No |
| GET | `/activate` | Activate account via emailed link | No |
| POST | `/login` | Authenticate and receive a JWT | No |
| GET | `/profile` | Get current user's profile | Yes |
| POST | `/categories` | Create a category | Yes |
| GET | `/categories` | List categories | Yes |
| GET | `/categories/{type}` | List categories by type | Yes |
| PUT | `/categories/{categoryId}` | Update a category | Yes |
| POST | `/incomes` | Add an income entry | Yes |
| GET | `/incomes` | List income entries | Yes |
| DELETE | `/incomes/{id}` | Delete an income entry | Yes |
| POST | `/expenses` | Add an expense entry | Yes |
| GET | `/expenses` | List expense entries | Yes |
| DELETE | `/expenses/{id}` | Delete an expense entry | Yes |
| GET | `/dashboard` | Get dashboard summary | Yes |
| POST | `/filter` | Filter transactions | Yes |
| GET | `/excel/download/income` | Download income report as Excel | Yes |
| GET | `/excel/download/expense` | Download expense report as Excel | Yes |
| GET | `/email/income-excel` | Email income report as Excel | Yes |
| GET | `/email/expense-excel` | Email expense report as Excel | Yes |

## Deployment

This project includes a multi-stage `Dockerfile` that builds the application from source — no local Docker installation or pre-built jar is required. Push to a Git repository and deploy on any platform that supports Docker-based builds (e.g. Render, Koyeb).

Set `SPRING_PROFILES_ACTIVE=prod` in your deployment environment to reduce verbose SQL/Hibernate logging that's useful for local development but noisy in production.

Minimum required environment variables for deployment: all variables listed above except `PORT`, which most platforms set automatically.

## Security Notes

- Passwords are hashed with BCrypt.
- Authentication is stateless — JWTs are validated on every request via a custom filter.
- `spring.jpa.hibernate.ddl-auto=update` is convenient for development but not recommended for production once real data exists; consider migrating to Flyway or Liquibase for controlled schema changes.
- Never commit `.env` or real credentials — the `.gitignore` already excludes `.env`; double-check this before your first push, especially if you've copied this project from elsewhere.
```es.