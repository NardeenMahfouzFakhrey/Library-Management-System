# Library-Management-System
### Overview
This Library Management System is designed to manage library operations related to books, patrons, and borrowing activities. The system is built using Spring Boot and PostgreSQL, and it includes RESTful endpoints with JWT-based authentication for secure access to the API.

### Getting Started
#### Prerequisites
 PostgreSQL 
 An IDE such as IntelliJ IDEA or Eclipse

### Configuration
Before running the application, you need to configure the PostgreSQL database settings and JWT settings in the application.properties file.
####Open src/main/resources/application.properties.
#### Set the database connection properties:
         spring.datasource.url=jdbc:postgresql://localhost:5432/your_database
         spring.datasource.username=your_username
         spring.datasource.password=your_password
#### Configure JWT settings:
        app.jwtSecret=your_secret_key_here
        app.jwtExpirationMs=3600000
