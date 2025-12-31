# E-Commerce Backend API

## Capstone Project - Neovarsity Academy

A comprehensive RESTful API for an E-Commerce Management System built with Spring Boot, featuring user authentication, product management, shopping cart, and order processing capabilities.

## Features

- **User Authentication & Authorization**
  - User registration and login
  - JWT-based authentication
  - Role-based access control (Customer & Admin)

- **Product Management**
  - CRUD operations for products
  - Product search and filtering by category
  - Stock management

- **Shopping Cart**
  - Add/remove items from cart
  - View cart contents
  - Clear cart functionality

- **Order Management**
  - Create orders from cart
  - View order history
  - Order status tracking
  - Admin order management

- **Security**
  - JWT token-based authentication
  - Password encryption using BCrypt
  - Secure API endpoints

## Technology Stack

- **Framework**: Spring Boot 3.1.5
- **Language**: Java 17
- **Database**: MySQL / H2 (for development)
- **Security**: Spring Security with JWT
- **ORM**: Spring Data JPA / Hibernate
- **Build Tool**: Maven
- **Validation**: Jakarta Validation

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+ (for production)
- IDE (IntelliJ IDEA, Eclipse, or VS Code)

## Installation & Setup

### 1. Clone the Repository

```bash
git clone https://github.com/anas0099/capstone-backend.git
cd capstone-backend
```

### 2. Database Setup

#### For MySQL (Production):
1. Create a MySQL database:
```sql
CREATE DATABASE ecommerce_db;
```

2. Update `application.properties` with your database credentials:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

#### For H2 (Development):
The project includes H2 database configuration for development. Use the `dev` profile:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### 3. Build the Project

```bash
mvn clean install
```

### 4. Run the Application

```bash
mvn spring-boot:run
```

The API will be available at `http://localhost:8080`

## API Documentation

### Base URL
```
http://localhost:8080/api
```

### Authentication Endpoints

#### Register User
```http
POST /api/auth/register
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123",
  "firstName": "John",
  "lastName": "Doe",
  "phoneNumber": "1234567890"
}
```

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "email": "user@example.com",
  "role": "CUSTOMER",
  "message": "Login successful"
}
```

### Product Endpoints

#### Get All Products
```http
GET /api/products
```

#### Get Product by ID
```http
GET /api/products/{id}
```

#### Search Products
```http
GET /api/products/search?keyword=laptop
```

#### Get Products by Category
```http
GET /api/products/category/{category}
```

#### Create Product (Admin)
```http
POST /api/products
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "Laptop",
  "description": "High-performance laptop",
  "price": 999.99,
  "stockQuantity": 50,
  "category": "Electronics",
  "brand": "TechBrand",
  "imageUrl": "https://example.com/image.jpg"
}
```

#### Update Product (Admin)
```http
PUT /api/products/{id}
Authorization: Bearer {token}
```

#### Delete Product (Admin)
```http
DELETE /api/products/{id}
Authorization: Bearer {token}
```

### Cart Endpoints

#### Add Item to Cart
```http
POST /api/cart/add
Authorization: Bearer {token}
Content-Type: application/json

{
  "productId": 1,
  "quantity": 2
}
```

#### Get Cart
```http
GET /api/cart
Authorization: Bearer {token}
```

#### Remove Item from Cart
```http
DELETE /api/cart/item/{cartItemId}
Authorization: Bearer {token}
```

#### Clear Cart
```http
DELETE /api/cart/clear
Authorization: Bearer {token}
```

### Order Endpoints

#### Create Order
```http
POST /api/orders
Authorization: Bearer {token}
Content-Type: application/json

{
  "shippingAddress": "123 Main St, City, Country",
  "paymentMethod": "Credit Card"
}
```

#### Get User Orders
```http
GET /api/orders
Authorization: Bearer {token}
```

#### Get Order by ID
```http
GET /api/orders/{orderId}
Authorization: Bearer {token}
```

#### Update Order Status (Admin)
```http
PUT /api/orders/{orderId}/status?status=SHIPPED
Authorization: Bearer {token}
```

### Admin Endpoints

#### Get All Orders
```http
GET /api/admin/orders
Authorization: Bearer {admin_token}
```

#### Get Orders by Status
```http
GET /api/admin/orders/status/{status}
Authorization: Bearer {admin_token}
```

## Authentication

Most endpoints require JWT authentication. Include the token in the Authorization header:

```http
Authorization: Bearer {your_jwt_token}
```

## Project Structure

```
capstone-backend/
├── src/
│   ├── main/
│   │   ├── java/com/neovarsity/ecommerce/
│   │   │   ├── config/
│   │   │   ├── controller/
│   │   │   ├── dto/
│   │   │   ├── exception/
│   │   │   ├── model/
│   │   │   ├── repository/
│   │   │   ├── security/
│   │   │   ├── service/
│   │   │   └── EcommerceApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── application-dev.properties
│   └── test/
├── pom.xml
└── README.md
```

## Testing

Run tests using Maven:
```bash
mvn test
```

## Database Schema

The application uses the following main entities:
- **User**: Stores user information and authentication details
- **Product**: Stores product information
- **Cart**: Shopping cart for users
- **CartItem**: Items in the shopping cart
- **Order**: Order information
- **OrderItem**: Items in an order

## Security Features

- JWT token-based authentication
- Password encryption with BCrypt
- Role-based access control (RBAC)
- Secure API endpoints
- Input validation

## Deployment

### Build JAR file:
```bash
mvn clean package
```

### Run JAR file:
```bash
java -jar target/ecommerce-backend-1.0.0.jar
```

## License

This project is created as part of the Neovarsity Academy Capstone Project.

## Author

Created as part of Neovarsity Academy Capstone Project

## Acknowledgments

- Spring Boot Framework
- Neovarsity Academy for project guidance

