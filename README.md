# Appointment Booking System

A microservices-based appointment booking system for salons, built with Spring Boot and Spring Cloud. This system enables customers to book appointments at salons, manage services, and process payments.

## 🏗️ Architecture

This project follows a microservices architecture pattern with the following components:

### Service Discovery
- **Eureka Server** (Port: 8070) - Service registry and discovery server

### API Gateway
- **Gateway Server** (Port: 5000) - Single entry point for all client requests, handles routing, security, and load balancing

### Core Services
- **User Service** (Port: 5001) - Manages user accounts, authentication, and user profiles
- **Salon Service** (Port: 5002) - Handles salon information and management
- **Category Service** (Port: 5003) - Manages service categories
- **Offering Service** (Port: 5004) - Manages service offerings and packages
- **Booking Service** (Port: 5005) - Handles appointment bookings and scheduling
- **Payment Service** (Port: 5006) - Processes payments using Stripe integration

### Data Storage
Each microservice has its own dedicated MySQL database:
- `userdb` (Port: 3301)
- `salondb` (Port: 3307)
- `categorydb` (Port: 3303)
- `servicedb` (Port: 3305)
- `bookingdb` (Port: 3302)
- `paymentdb` (Port: 3304)

## 🛠️ Technology Stack

### Backend
- **Java 21** - Programming language
- **Spring Boot 3.5.6/3.5.7** - Application framework
- **Spring Cloud 2025.0.0** - Microservices framework
- **Spring Cloud Gateway** - API Gateway with WebFlux
- **Netflix Eureka** - Service discovery
- **OpenFeign** - Declarative REST client for inter-service communication
- **Spring Data JPA** - Data persistence
- **Spring Security** - Security framework
- **OAuth2 Resource Server** - OAuth2 authentication with Keycloak integration
- **MySQL** - Relational database
- **Stripe Java SDK** - Payment processing
- **Lombok** - Boilerplate code reduction
- **Jib Maven Plugin** - Container image building

### Infrastructure
- **Docker & Docker Compose** - Containerization and orchestration
- **Maven** - Build tool and dependency management

## 📋 Prerequisites

Before running the application, ensure you have the following installed:

- **Java 21** or higher
- **Maven 3.6+**
- **Docker** and **Docker Compose**
- **MySQL** (if running databases locally)
- **Keycloak** (for authentication - running on port 8080)

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone <repository-url>
cd appointment-booking

### 2. Build the Project

Build all services using Maven:

```bash
# Build all services
mvn clean install

# Or build individual service
cd booking-service
mvn clean install
```

### 3. Database Setup

The project uses Docker Compose for database setup. You can either:

#### Option A: Use Docker Compose (Recommended)
```bash
cd docker-compose/default
docker-compose up -d
```

This will start all MySQL databases in containers.

#### Option B: Run Databases Locally
Ensure MySQL is running and create the required databases:
- `userdb`
- `salondb`
- `categorydb`
- `servicedb`
- `bookingdb`
- `paymentdb`

### 4. Configure Keycloak

1. Start Keycloak server on port 8080
2. Create a realm (default: `master`)
3. Configure OAuth2 clients as needed
4. Update the gateway configuration with your Keycloak JWK endpoint

### 5. Run Services

#### Option A: Run with Docker Compose

```bash
cd docker-compose/default
docker-compose up
```

This will start all services in Docker containers.

#### Option B: Run Services Locally

Start services in the following order:

1. **Eureka Server** (must start first)
   ```bash
   cd eureka-server
   mvn spring-boot:run
   ```

2. **Gateway Server**
   ```bash
   cd gateway-server
   mvn spring-boot:run
   ```

3. **Core Services** (can be started in any order)
   ```bash
   # User Service
   cd user-service
   mvn spring-boot:run

   # Salon Service
   cd salon-service
   mvn spring-boot:run

   # Category Service
   cd category-service
   mvn spring-boot:run

   # Offering Service
   cd offering-service
   mvn spring-boot:run

   # Booking Service
   cd booking-service
   mvn spring-boot:run

   # Payment Service
   cd payment-service
   mvn spring-boot:run
   ```

### 6. Verify Services

- **Eureka Dashboard**: http://localhost:8070
- **API Gateway**: http://localhost:5000

All services should register with Eureka and be visible in the dashboard.

## 📡 API Endpoints

The API Gateway routes requests to appropriate services:

- `/api/users/**` → User Service
- `/api/salons/**`, `/api/admin/salons/**`, `/salons/**` → Salon Service
- `/api/categories/**` → Category Service
- `/api/bookings/**` → Booking Service
- `/api/payments/**` → Payment Service
- `/auth/**` → User Service (Authentication)

## 🔐 Security

The Gateway Server implements OAuth2 Resource Server authentication using Keycloak:
- JWT token validation
- Secure inter-service communication
- Protected API endpoints

## 🐳 Docker Images

The project uses Jib Maven Plugin to build Docker images. Images are published to:
- `tejastawde28/salon-eureka-server:v1`
- `tejastawde28/salon-gateway-server:v1`
- `tejastawde28/salon-user:v1`
- `tejastawde28/salon-salon:v1`
- `tejastawde28/salon-category:v1`
- `tejastawde28/salon-service-offering:v1`
- `tejastawde28/salon-booking:v1`
- `tejastawde28/salon-payment:v1`

## 📁 Project Structure

```
appointment-booking/
├── eureka-server/          # Service discovery server
├── gateway-server/         # API Gateway
├── user-service/           # User management service
├── salon-service/          # Salon management service
├── category-service/       # Category management service
├── offering-service/       # Service offering management
├── booking-service/        # Booking management service
├── payment-service/        # Payment processing service
└── docker-compose/         # Docker Compose configurations
    └── default/
        └── docker-compose.yml
```

## 🔄 Inter-Service Communication

Services communicate using:
- **OpenFeign** - For synchronous REST API calls
- **Service Discovery** - Eureka for locating services
- **Load Balancing** - Gateway uses load balancing via Eureka

## 🚧 Upcoming Features

The following components are planned to be added:

- Additional microservices (to be announced)
- **RabbitMQ** - For asynchronous messaging and event-driven communication
- **Frontend** - User interface application

## 🧪 Testing

Run tests for each service:

```bash
cd <service-name>
mvn test
```

## 📝 Configuration

Each service has its own `application.yml` configuration file where you can customize:
- Database connections
- Eureka server URLs
- Service ports
- Security settings

## 🤝 Contributing

This is a work-in-progress project. Contributions are welcome!
