# Microservice E-Commerce Platform

A scalable, microservices-based e-commerce platform built with Spring Boot and Spring Cloud. This project demonstrates a distributed architecture with service discovery, API gateway, and multiple independent services.

## Architecture Overview

This project consists of the following microservices:

- **API Gateway** - Central entry point for all client requests with JWT authentication and routing
- **Eureka Server** - Service registry and discovery server
- **Config Server** - Centralized configuration management for all microservices
- **User Service** - Handles user management and authentication
- **Product Service** - Manages product catalog and inventory
- **Cart Service** - Shopping cart management for users
- **Order Service** - Processes and manages customer orders

## Technology Stack

- **Framework**: Spring Boot 4.0.4
- **Java Version**: Java 21
- **Cloud Framework**: Spring Cloud 2025.1.1
- **Service Discovery**: Netflix Eureka
- **API Gateway**: Spring Cloud Gateway
- **Authentication**: JWT (JSON Web Tokens)
- **Circuit Breaker**: Resillience4J
- **Configuration Management**: Spring Cloud Config
- **Build Tool**: Maven

## Prerequisites

- Java Development Kit (JDK) 21 or higher
- Maven 3.6+

## Getting Started

### 1. Clone the Repository
```bash
git clone https://github.com/MaheshGudooru/Microservice_Ecommerce.git
cd Microservice_Ecommerce
```

### 2. Build All Services
From the project root directory, build all microservices:

```bash
cd api-gateway && mvn clean install
cd ../eureka-server && mvn clean install
cd ../config-server && mvn clean install
cd ../user-service && mvn clean install
cd ../product-service && mvn clean install
cd ../cart-service && mvn clean install
cd ../order-service && mvn clean install
```

### 3. Running the Microservices

Start services in the recommended order:

#### 1. Start Config Server (Configuration Management)
```bash
cd config-server
mvn spring-boot:run
```

#### 2. Start Eureka Server (Service Registry)
```bash
cd eureka-server
mvn spring-boot:run
# Accessible at: http://localhost:8761
```

#### 3. Start Individual Microservices
```bash
# User Service
cd user-service
mvn spring-boot:run

# Product Service
cd product-service
mvn spring-boot:run

# Cart Service
cd cart-service
mvn spring-boot:run

# Order Service
cd order-service
mvn spring-boot:run
```

#### 4. Start API Gateway
```bash
cd api-gateway
mvn spring-boot:run
# API Gateway will be accessible at: http://localhost:8080
```

## Project Structure

```
Microservice_Ecommerce/
├── api-gateway/              # API Gateway Service
├── eureka-server/            # Service Discovery Server
├── config-server/            # Configuration Server
├── user-service/             # User Management Service
├── product-service/          # Product Catalog Service
├── cart-service/             # Shopping Cart Service
├── order-service/            # Order Management Service
└── README.md                 # This file
```

## Service Endpoints

| Service | Default Port | Purpose |
|---------|-------------|---------|
| API Gateway | 8080 | Main entry point for all requests |
| Eureka Server | 8761 | Service registry and discovery |
| Config Server | 8888 | Configuration management |
| User Service | 8081 | User operations |
| Product Service | 8082 | Product operations |
| Cart Service | 8083 | Shopping cart operations |
| Order Service | 8084 | Order operations |

> **Note**: Actual ports may vary. Check `https://github.com/MaheshGudooru/config-server` files for each service configuration.

## Features

- ✅ **Distributed Architecture** - Independent, scalable microservices
- ✅ **Service Discovery** - Automatic service registration and discovery with Eureka
- ✅ **API Gateway** - Centralized routing and request handling
- ✅ **JWT Authentication** - Secure API endpoints with token-based authentication
- ✅ **Centralized Configuration** - Manage configuration across all services from one place

## Configuration

Each microservice can be configured through:

1. **Config Server**: Centralized configuration management (configurations for microservices are hosted on GitHub)
2. **Local Files**: `application.yml` or `application.yaml` in `src/main/resources/`

## Endpoints
```
GET http://localhost:8080/api/products
GET http://localhost:8080/api/products/{id}
GET http://localhost:8080/api/products?category

GET http://localhost:8080/api/user
POST http://localhost:8080/api/user/login?email&password
POST http://localhost:8080/api/user/register?name&email&password

GET http://localhost:8080/api/cart
POST http://localhost:8080/api/cart/add?productId&quantity
POST http://localhost:8080/api/cart/remove?cartItemId
POST http://localhost:8080/api/cart/empty
POST http://localhost:8080/api/cart/update?cartItemId&quantity

GET http://localhost:8080/api/order
POST http://localhost:8080/api/order?address&paymentMethod
POST http://localhost:8080/api/order/orderstatus?orderId&status
```
