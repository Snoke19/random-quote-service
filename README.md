# Random Quote Service

[![Java 17](https://img.shields.io/badge/java-17-%23ED8B00.svg?style=for-the-badge&logo=java&logoColor=white)](https://www.java.com)
[![Spring Boot 4.1.0](https://img.shields.io/badge/Spring%20Boot-4.1.0-%236DB33F.svg?style=for-the-badge&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-%23336791.svg?style=for-the-badge&logo=postgresql&logoColor=white)](https://www.postgresql.org)
[![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)](https://www.docker.com)

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Build Status](https://github.com/your-org/random-quote-service/actions/workflows/build.yml/badge.svg)](https://github.com/your-org/random-quote-service/actions)
[![Code Quality](https://sonarcloud.io/api/project_badges/measure?project=random-quote-service&metric=alert_status)](https://sonarcloud.io)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=random-quote-service&metric=coverage)](https://sonarcloud.io)

---

## 📖 Table of Contents

- [📌 About the Project](#-about-the-project)
- [✨ Features](#-features)
- [🏗️ Architecture](#-architecture)
- [🚀 Getting Started](#-getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
  - [Running with Docker](#running-with-docker)
  - [Running Locally](#running-locally)
- [📡 API Documentation](#-api-documentation)
  - [Endpoints](#endpoints)
  - [Request/Response Examples](#requestresponse-examples)
  - [Error Responses](#error-responses)
- [🛠️ Configuration](#-configuration)
- [📦 Dependencies](#-dependencies)
- [🧪 Testing](#-testing)
- [📊 Performance Considerations](#-performance-considerations)
- [🔒 Security](#-security)
- [🤝 Contributing](#-contributing)
- [📜 License](#-license)
- [📞 Contact](#-contact)

---

## 📌 About the Project

**Random Quote Service** is a RESTful API service that provides inspirational, motivational, and categorized quotes. It allows users to fetch random quotes based on categories, search quotes by text content, and browse available categories.

This project is built using **Spring Boot 4.1.0** with **PostgreSQL** as the database, following modern Java development best practices and clean architecture principles.

### 🎯 Purpose

- Provide a simple, fast API for fetching random quotes
- Support category-based filtering
- Enable full-text search across quotes
- Serve as a foundation for quote-based applications

### 🏆 Key Characteristics

- **High Performance**: Optimized SQL queries with GIN indexes for text search
- **Scalable**: Dockerized with proper resource management
- **Maintainable**: Clean layered architecture with proper separation of concerns
- **Testable**: Comprehensive test coverage with TestContainers
- **Production-Ready**: Health checks, monitoring, and proper error handling

---

## ✨ Features

### Quote Management

✅ **Get Random Quote by Categories** - Fetch a random quote filtered by one or more categories  
✅ **Search Quotes by Text** - Full-text search with pagination  
✅ **Category Filtering** - Filter quotes by multiple categories  
✅ **Text Search** - ILIKE (case-insensitive) search on quote text  

### Category Management

✅ **List All Categories** - Get all available categories with pagination  
✅ **Search Categories by Name** - Filter categories by name pattern  
✅ **Paginated Results** - All list endpoints support offset-based pagination  

### Technical Features

✅ **RESTful API Design** - Following REST best practices  
✅ **Input Validation** - Comprehensive request validation  
✅ **Internationalization** - Support for multiple languages  
✅ **Proper Error Handling** - Structured error responses with HTTP status codes  
✅ **Docker Support** - Containerized deployment with Docker and Docker Compose  
✅ **Health Monitoring** - Actuator endpoints for health checks  
✅ **Database Migration** - Schema initialization scripts included  

---

## 🏗️ Architecture

```mermaid
flowchart TB

    Client["Client Applications<br/>(Web)"]

    subgraph Service["Random Quote Service"]

        subgraph Controllers["REST Controllers Layer"]
            QuoteController["QuoteController"]
            CategoryController["CategoryController"]
        end

        subgraph Services["Service Layer"]
            QuoteService["QuoteService"]
            CategoryService["CategoryService"]
        end

        subgraph Repositories["Repository Layer"]
            QuoteRepository["QuoteRepository"]
            CategoryRepository["CategoryRepository"]
        end

        QuoteController --> QuoteService
        CategoryController --> CategoryService

        QuoteService --> QuoteRepository
        CategoryService --> CategoryRepository
    end

    subgraph Database["PostgreSQL Database"]
        Authors["authors"]
        Quotes["quotes"]
        Categories["categories"]
        CategoriesQuotes["categories_quotes"]

        Authors <--> Quotes
        Quotes <--> Categories
        Categories <--> CategoriesQuotes
    end

    Client --> Service

    QuoteRepository --> Database
    CategoryRepository --> Database
```

### Design Patterns Used

- **Layered Architecture** - Clear separation between presentation, business logic, and data access
- **Repository Pattern** - Abstract data access layer
- **Dependency Injection** - Spring IoC container for loose coupling
- **DTO Pattern** - Data Transfer Objects for API responses
- **Factory Pattern** - RowMapper and ResultSetExtractor for object creation

---

## 🚀 Getting Started

### Prerequisites

Before you begin, ensure you have the following installed:

| Tool | Version | Download |
|------|---------|----------|
| Java | 17+ | [Oracle JDK](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) or [OpenJDK](https://openjdk.org) |
| Maven | 3.8.7+ | [Apache Maven](https://maven.apache.org/download.cgi) |
| Docker | 20.10+ | [Docker Desktop](https://www.docker.com/products/docker-desktop/) |
| Docker Compose | 2.0+ | Included with Docker Desktop |
| PostgreSQL | 15+ | [PostgreSQL](https://www.postgresql.org/download/) |

### Installation

#### 1. Clone the Repository

```bash
# Using HTTPS
git clone https://github.com/your-org/random-quote-service.git
cd random-quote-service

# Using SSH
git clone git@github.com:your-org/random-quote-service.git
cd random-quote-service
```

#### 2. Build the Project

```bash
# Build with Maven
./mvnw clean package

# Or using Maven wrapper on Windows
mvnw.cmd clean package
```

#### 3. Verify Build

```bash
# Check that the JAR file was created
ls -lh target/*.jar

# Or on Windows
dir target\*.jar
```

---

## 🐳 Running with Docker (Recommended)

The easiest way to run the application with all dependencies is using Docker Compose.

### Step 1: Set Up Environment

The project includes pre-configured environment files:

- `env/credentials.env` - Database credentials
- `env/random-quote-service.env` - Application configuration

You can modify these files if needed, or create a `.env` file in the project root.

### Step 2: Start Services

```bash
# Start all services (PostgreSQL + Application)
docker-compose up -d

# View running containers
docker-compose ps

# View logs
docker-compose logs -f
```

### Step 3: Access the API

The application will be available at:
```
http://localhost:8080
```

### Step 4: Health Check

Verify the application is running:
```bash
# Check application health
curl http://localhost:8080/actuator/health

# Expected response: {"status":"UP"}
```

### Step 5: Stop Services

```bash
# Stop all containers
docker-compose down

# Stop and remove containers, networks, and volumes
docker-compose down -v
```

---

## 🏃 Running Locally (Without Docker)

### Step 1: Set Up PostgreSQL

#### Option A: Using Docker (Recommended)

```bash
# Start PostgreSQL container
docker run --name postgres-db -e POSTGRES_USER=root_user -e POSTGRES_PASSWORD=root -e POSTGRES_DB=quotes -p 5432:5432 -d postgres:15-alpine

# Initialize database schema
psql -h localhost -U root_user -d quotes -f src/main/resources/schema.sql
```

#### Option B: Local PostgreSQL Installation

```bash
# Create database
createdb quotes

# Create user
createuser root_user -P

# Initialize schema
psql -U root_user -d quotes -f src/main/resources/schema.sql
```

### Step 2: Configure Application

Create a `.env` file or set environment variables:

```bash
# Required environment variables
export POSTGRES_URL=jdbc:postgresql://localhost:5432/quotes
export POSTGRES_USER=root_user
export POSTGRES_PASSWORD=root
export SOURCE_MESSAGES=messages

# Or on Windows (CMD)
set POSTGRES_URL=jdbc:postgresql://localhost:5432/quotes
set POSTGRES_USER=root_user
set POSTGRES_PASSWORD=root
set SOURCE_MESSAGES=messages
```

### Step 3: Run the Application

```bash
# Run with Maven
./mvnw spring-boot:run

# Or with Java (after building)
java -jar target/random-quote-service-0.0.1-SNAPSHOT.jar

# Or on Windows
mvnw.cmd spring-boot:run
java -jar target\random-quote-service-0.0.1-SNAPSHOT.jar
```

### Step 4: Verify

```bash
# Check health
curl http://localhost:8080/actuator/health

# Test an endpoint
curl "http://localhost:8080/random/quote?categories=love"
```

---

## 📡 API Documentation

### Base URL

```
http://localhost:8080
```

### Content-Type

All endpoints return `application/json` and expect `application/json` for requests.

---

## 🎯 Endpoints

### Quote Endpoints

| Method | Endpoint | Description | Parameters |
|--------|----------|-------------|------------|
| GET | `/random/quote` | Get a random quote by categories | `categories` (required) |
| GET | `/quotes` | Search quotes by text with pagination | `text`, `offset` (default: 0) |

### Category Endpoints

| Method | Endpoint | Description | Parameters |
|--------|----------|-------------|------------|
| GET | `/categories` | Get all categories with pagination | `offset` (default: 0) |
| GET | `/categories/{name}` | Get categories matching name pattern | `name`, `offset` (default: 0) |

---

## 📝 Request/Response Examples

### 1. Get Random Quote by Categories

**Request:**
```bash
GET /random/quote?categories=love&categories=motivational
```

**Request (cURL):**
```bash
curl -X GET "http://localhost:8080/random/quote?categories=love&categories=motivational"
```

**Response (200 OK):**
```json
{
  "id": 8,
  "quoteText": "You miss 100% of the shots you don't take.",
  "author": {
    "id": 8,
    "name": "Author Eight"
  }
}
```

**Error Response (400 Bad Request) - Missing categories:**
```json
{
  "type": "NOT_EMPTY_VALIDATION",
  "code": 400,
  "message": "Required request parameter 'categories' for method parameter type List is not present",
  "details": {
    "categories": "Categories cannot be empty! Please add categories!"
  },
  "path": "uri=/random/quote",
  "timestamp": "2026-06-15T10:00:00.123456"
}
```

**Error Response (404 Not Found) - No quotes in category:**
```json
{
  "type": "NOT_FOUND_ENTITY",
  "code": 404,
  "message": "Quote not found!",
  "details": {},
  "path": "uri=/random/quote",
  "timestamp": "2026-06-15T10:00:00.123456"
}
```

---

### 2. Search Quotes by Text

**Request:**
```bash
GET /quotes?text=great&offset=0
```

**Request (cURL):**
```bash
curl -X GET "http://localhost:8080/quotes?text=great&offset=0"
```

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "quoteText": "The only way to do great work is to love what you do.",
    "author": {
      "id": 1,
      "name": "Author One"
    },
    "categories": [
      {
        "id": 1,
        "name": "courage"
      },
      {
        "id": 2,
        "name": "friendship"
      }
    ]
  }
]
```

**Note:** Returns up to 10 quotes per request. Use `offset` parameter for pagination.

---

### 3. Get All Categories

**Request:**
```bash
GET /categories?offset=0
```

**Request (cURL):**
```bash
curl -X GET "http://localhost:8080/categories?offset=0"
```

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "name": "courage"
  },
  {
    "id": 2,
    "name": "friendship"
  },
  {
    "id": 3,
    "name": "happiness"
  }
]
```

---

### 4. Get Categories by Name

**Request:**
```bash
GET /categories/friend
```

**Request (cURL):**
```bash
curl -X GET "http://localhost:8080/categories/friend"
```

**Response (200 OK):**
```json
[
  {
    "id": 2,
    "name": "friendship"
  }
]
```

---

## ❌ Error Responses

All error responses follow a consistent structure:

| Field | Type | Description |
|-------|------|-------------|
| `type` | string | Error type/category |
| `code` | integer | HTTP status code |
| `message` | string | Human-readable error message |
| `details` | object | Additional error details (field-specific errors) |
| `path` | string | Request path |
| `timestamp` | string | ISO-8601 timestamp |

### Common Error Types

| Type | HTTP Status | Description |
|------|-------------|-------------|
| `NOT_FOUND_ENTITY` | 404 | Entity not found in database |
| `NOT_EMPTY_VALIDATION` | 400 | Required field is empty |
| `Validation` | 400 | General validation error |
| `Server Error` | 500 | Internal server error |

---

## ⚙️ Configuration

### Environment Variables

| Variable | Required | Default | Description |
|----------|----------|---------|-------------|
| `POSTGRES_URL` | ✅ Yes | - | JDBC URL for PostgreSQL |
| `POSTGRES_USER` | ✅ Yes | - | Database username |
| `POSTGRES_PASSWORD` | ✅ Yes | - | Database password |
| `SOURCE_MESSAGES` | ✅ Yes | `messages` | Message source basename |

### Application Properties

The main configuration file is `src/main/resources/application.properties`:

```properties
# Application name
spring.application.name=random-quote-service

# Database configuration
spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.username=${POSTGRES_USER}
spring.datasource.password=${POSTGRES_PASSWORD}
spring.datasource.url=${POSTGRES_URL}

# Messages
spring.messages.basename=${SOURCE_MESSAGES}

# Actuator
management.endpoints.web.exposure.include=health
```

## 🧪 Testing

### Running Tests

```bash
# Run all tests
./mvnw test

# Run specific test class
./mvnw test -Dtest=QuoteControllerTests

# Run with test coverage
./mvnw clean verify
```

## 📜 License

This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for details.

```
MIT License

Copyright (c) 2026 Quotopia

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

*Built with ❤️ using Spring Boot and Java*

*Last updated: 2026-06-15*
