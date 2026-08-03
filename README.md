# E-Blouder Gym Management & POS Backend

A robust, enterprise-grade backend service built for climbing and bouldering gyms. This application handles core operational workflows including member management, serialized equipment rental fleets, tracking maintenance lifecycles, and a retail Point of Sale (POS) inventory system.

Designed with clean architecture principles, separation of concerns (Interface/Implementation pattern), and rigorous test coverage, this project serves as a comprehensive demonstration of modern Java Spring Boot backend engineering.

---

## Key Features

* **Security & Authentication:** Role-Based Access Control (RBAC) supporting USER, and ADMIN roles using Spring Security and JWT.
* **️Rental Fleet Management:** Manages serialized equipment (shoes, harnesses) with state validation (checking conditions like GOOD, NEEDS_REPAIR, RETIRED) and active rental checks to prevent double-booking.
* **️Maintenance Lifecycle:** Tracks broken gear sent for repair, automatically updates equipment states, and logs repair costs, concluding with an automated rollback to GOOD condition upon completion.
* **Retail POS & Orders:** Inventory control for retail goods (chalk, tape, brushes, energy drinks) with transaction processing, stock validation, and atomic stock deductions.
* **Database Version Control:** Fully managed database migrations and realistic initial seed data using Flyway and PostgreSQL.
* **Comprehensive Testing:** Isolated unit testing and controller layer mock tests covering business logic, custom exceptions, and edge cases.

---

## Tech Stack

* **Language:** Java 17+ / 21
* **Framework:** Spring Boot
* **Database & Migration:** PostgreSQL, Flyway
* **Persistence:** Spring Data JPA, Hibernate (with soft-delete configurations)
* **Mapping:** MapStruct (for clean DTO-to-Entity conversions)
* **Security:** Spring Security, JSON Web Tokens (JWT)
* **Documentation:** OpenAPI / Swagger UI
* **Testing:** JUnit 5, Mockito, Spring MockMvc

---

## System Documentation & Architecture Diagrams

The project's architectural evolution and structural layouts are documented inside the `docs/` folder:

* [UML_ClassDiagram_v2.pdf](docs/UML_ClassDiagram_v2.pdf) - **Latest Class Diagram (v2):** Comprehensive object-oriented model illustrating domain entities, controller-service-repository relationships, inheritance hierarchies, and database mapping structures.
* [UML_DesignDiagram_v1.pdf](docs/UML_DesignDiagram_v1.pdf) - **Design (Implementation) Diagram (v1):** Architectural overview showing system interactions, component boundaries.

---

## Architecture & Project Structure

The project follows a clean, layered backend architecture:
```text
src/main/java/com/example/demo/ 
├── config/               # Application configuration classes 
├── controllers/          # REST endpoints (Auth, Equipment, Maintenance, Members, Orders, Products, Rentals, Users)
├── dtos/                 # Request/Response Data Transfer Objects 
├── enums/                # System Enumerations (Conditions, Types, Statuses, Roles)
├── exceptions/           # Custom domain exception handlers 
├── mappers/              # MapStruct mapping components 
├── models/               # JPA Entities extending a unified BaseEntity 
├── repositories/         # Spring Data JPA interfaces 
├── security/             # JWT filters and configuration 
└── services/             # Business logic layer (Interfaces & Impl)
```
---

## Core API Endpoints

### Authentication (/api/auth)
* POST /api/auth/register - Register a new user profile
* POST /api/auth/login - Authenticate and receive a JWT token

### Rentals (/api/rentals)
* POST /api/rentals/checkout - Check out equipment for a member (POS desk flow)
* POST /api/rentals/{id}/return - Close an active rental transaction
* GET /api/rentals - View all rentals (supports ?status=ACTIVE filter)
* GET /api/rentals/member/{memberId} - View rental history for a specific member

### Retail Orders (/api/orders)
* POST /api/orders - Place a retail order and decrement stock atomically
* GET /api/orders - View all gym orders (Admin/Staff)
* GET /api/orders/{id} - Get order receipt details
* PATCH /api/orders/{id}/status - Update order status (PENDING, PAID, CANCELLED)
* GET /api/orders/user/{userId} - View order history for a specific user

### Inventory Products (/api/products)
* GET /api/products - Paginated product catalog
* POST /api/products - Add new retail item
* GET /api/products/{id} - Get product details by ID
* PUT /api/products/{id} - Update full product details
* DELETE /api/products/{id} - Delete product
* PATCH /api/products/{id}/stock - Fast stock adjustments (shipments or write-offs)

### Users (/api/users)
* GET /api/users - Get all users
* POST /api/users - Create a user
* GET /api/users/{id} - Get user by ID
* PATCH /api/users/{id}/display-name - Update user display name

### Members (/api/members)
* GET /api/members - Get all gym members
* POST /api/members - Register a new member
* GET /api/members/{id} - Get member details by ID
* PATCH /api/members/{id}/membership - Update membership type

### Equipment (/api/equipment)
* GET /api/equipment - Get all equipment
* POST /api/equipment - Add new equipment to fleet
* GET /api/equipment/{id} - Get equipment details by ID
* PATCH /api/equipment/{id}/condition - Update equipment condition
* PATCH /api/equipment/{id}/retire - Retire equipment from service

### Maintenance (/api/maintenance)
* GET /api/maintenance - Get all maintenance logs (supports filtering by equipmentId)
* POST /api/maintenance - Send gear to maintenance shop
* GET /api/maintenance/{id} - Get specific maintenance log details
* PATCH /api/maintenance/{id}/complete - Mark repair complete, update cost, and automatically restore equipment status to GOOD
* DELETE /api/maintenance/{id} - Delete a maintenance log

---

## Getting Started & Local Setup

### Prerequisites
* Java Development Kit (JDK 17 or higher)
* PostgreSQL database instance
* Maven

### Installation
1. Clone the repository:
```text
   git clone https://github.com/BilyiPJATK/e-commerce-api
   cd e-commerce-api
```

2. Configure your database:
```text
   Update src/main/resources/application.properties with your local PostgreSQL credentials:
   spring.datasource.url=jdbc:postgresql://localhost:5432/your_database_name
   spring.datasource.username=your_username
   spring.datasource.password=your_password
```

3. Run the application:
```text
   mvn spring-boot:run
```

4. Explore Swagger API Documentation:
```text
   Once running, open your browser and navigate to:
   http://localhost:8080/swagger-ui/index.html
```

---

## Running Tests

To verify all unit and controller layer tests:
mvn test

---

## Author

**Volodymyr Bilyi**