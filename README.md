# Order Management

REST API for managing orders, order lines, and products, built as a portfolio project to practice backend development
with Spring Boot.

## Stack

- Java 21
- Spring Boot 4.1.0 (Web, Data JPA, Security, Validation)
- PostgreSQL
- Lombok
- Swagger / OpenAPI (springdoc-openapi)

## Features

CRUD operations for:

- **Products**: create, retrieve, update, and delete products (name, description, price, stock)
- **Orders**: create orders with their associated order lines, retrieve, update, and delete

## Running it locally

### Prerequisites

- Java 21
- PostgreSQL running locally, with a database created:

```sql
CREATE DATABASE gestion_pedidos;
```

You don't need Maven installed: the project includes the Maven Wrapper (`mvnw` / `mvnw.cmd`), which downloads and uses
the correct version automatically.

### Configuration

The application reads database credentials from environment variables:

| Variable      | Description         | Default                        |
|---------------|---------------------|--------------------------------|
| `DB_USERNAME` | PostgreSQL username | `postgres`                     |
| `DB_PASSWORD` | PostgreSQL password | *(required, no default value)* |

Set them before running the application, using any of these options:

**From a terminal (Linux/macOS)**

```bash
export DB_PASSWORD=your_password
./mvnw spring-boot:run
```

**From a terminal (Windows, PowerShell)**

```powershell
$env:DB_PASSWORD="your_password"
./mvnw spring-boot:run
```

**From an IDE** (IntelliJ, Eclipse, VS Code, etc.): set the environment variable in your editor's run/debug
configuration, using the same name (`DB_PASSWORD`) and value.

### Run

```bash
./mvnw spring-boot:run
```

The application starts by default on `http://localhost:8080`.

## API Documentation

With the application running, the interactive documentation (Swagger UI) is available at:

```
http://localhost:8080/swagger-ui/index.html
```

## Project Status

Actively in development. Completed:

- DTO pattern applied to both Product and Order layers
- Unit tests for the Product and Order service layers (JUnit 5 + Mockito)
- JWT authentication with user registration and login
- Role-based authorization (ADMIN/USER)

Next steps:

- Prepare deployment

## Notes

This is a personal learning and portfolio project, not a production product.