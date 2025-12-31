# Quick Start Guide

## Getting Started in 5 Minutes

### Step 1: Prerequisites Check
Ensure you have:
- Java 17+ installed (`java -version`)
- Maven 3.6+ installed (`mvn -version`)
- MySQL 8.0+ installed (optional, can use H2 for development)

### Step 2: Clone and Setup
```bash
# Clone the repository
git clone https://github.com/anas0099/capstone-backend.git
cd capstone-backend

# Run setup script (optional)
chmod +x setup.sh
./setup.sh
```

### Step 3: Configure Database

#### Option A: H2 Database (Quick Start - No MySQL needed)
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

#### Option B: MySQL Database
1. Create database:
```sql
CREATE DATABASE ecommerce_db;
```

2. Update `src/main/resources/application.properties` with your MySQL credentials

3. Run:
```bash
mvn spring-boot:run
```

### Step 4: Test the API

The API will start at `http://localhost:8080`

#### Test Registration:
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123",
    "firstName": "Test",
    "lastName": "User",
    "phoneNumber": "1234567890"
  }'
```

#### Test Login:
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123"
  }'
```

Save the token from the response for authenticated requests.

#### Test Get Products:
```bash
curl http://localhost:8080/api/products
```

## Next Steps

1. Read the README.md for detailed documentation
2. Check API_DOCUMENTATION.md for all available endpoints
3. Review PROJECT_REPORT.md for project details
4. See DEPLOYMENT.md for production deployment guide

## Common Issues

### Port 8080 already in use
Change port in `application.properties`:
```properties
server.port=8081
```

### Database connection error
- Check MySQL is running
- Verify credentials in `application.properties`
- Or use H2 database with dev profile

### Build errors
```bash
mvn clean install
```

## Documentation Files

- `README.md` - Main project documentation
- `API_DOCUMENTATION.md` - Complete API reference
- `PROJECT_REPORT.md` - Detailed project report
- `DEPLOYMENT.md` - Deployment instructions
- `QUICK_START.md` - This file

## Project Structure

```
capstone-backend/
├── src/main/java/com/neovarsity/ecommerce/
│   ├── config/
│   ├── controller/
│   ├── dto/
│   ├── exception/
│   ├── model/
│   ├── repository/
│   ├── security/
│   └── service/
├── src/main/resources/
│   └── application.properties
└── pom.xml
```

## Verification Checklist

- [ ] Java 17+ installed
- [ ] Maven installed
- [ ] Application starts without errors
- [ ] Can register a user
- [ ] Can login and get JWT token
- [ ] Can access products endpoint
- [ ] Database tables created automatically

## Need Help?

1. Check the error logs in console
2. Review the documentation files
3. Verify database connection
4. Check Java and Maven versions
