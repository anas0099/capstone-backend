#!/bin/bash

echo "========================================="
echo "E-Commerce Backend Setup Script"
echo "========================================="
echo ""

echo "Checking Java version..."
java -version
if [ $? -ne 0 ]; then
    echo "ERROR: Java is not installed. Please install Java 17 or higher."
    exit 1
fi

echo ""
echo "Checking Maven version..."
mvn -version
if [ $? -ne 0 ]; then
    echo "ERROR: Maven is not installed. Please install Maven 3.6 or higher."
    exit 1
fi

echo ""
echo "Cleaning and building project..."
mvn clean install -DskipTests

if [ $? -eq 0 ]; then
    echo ""
    echo "========================================="
    echo "Build successful!"
    echo "========================================="
    echo ""
    echo "To run the application:"
    echo "  mvn spring-boot:run"
    echo ""
    echo "Or with H2 database (dev profile):"
    echo "  mvn spring-boot:run -Dspring-boot.run.profiles=dev"
    echo ""
else
    echo ""
    echo "========================================="
    echo "Build failed. Please check the errors above."
    echo "========================================="
    exit 1
fi

