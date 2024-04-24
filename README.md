# Library-Management-System
### Overview
This Library Management System is designed to manage library operations related to books, patrons, and borrowing activities. The system is built using Spring Boot and PostgreSQL, and it includes RESTful endpoints with JWT-based authentication for secure access to the API.

### Getting Started
#### Prerequisites
 - PostgreSQL 
 - An IDE such as IntelliJ IDEA or Eclipse

### Configuration
Before running the application, you need to configure the PostgreSQL database settings and JWT settings in the application.properties file.
#### Open src/main/resources/application.properties.
#### Set the database connection properties:
         spring.datasource.url=jdbc:postgresql://localhost:5432/your_database
         spring.datasource.username=your_username
         spring.datasource.password=your_password
#### Configure JWT settings:
        app.jwtSecret=your_secret_key_here
        app.jwtExpirationMs=3600000

        The application will start running at http://localhost:8080.

### API Endpoints
#### Book Management
GET /api/books - Retrieve a list of all books.
GET /api/books/{id} - Retrieve details of a specific book by ID.
POST /api/books - Add a new book to the library.
 Required JSON Payload:
  {
     "title": "Example Book Title",
     "author": "Author Name",
     "publicationYear": 2021,
     "isbn": "ISBN-122"
  }
PUT /api/books/{id} - Update an existing book's information.
Required JSON Payload for updated info:
   {
     "title": "Book Title",
     "author": "Author Name",
     "publicationYear": 2021,
     "isbn": "ISBN-122"
   }
DELETE /api/books/{id} - Remove a book from the library.

#### Patron Management
GET /api/patrons - Retrieve a list of all patrons.
GET /api/patrons/{id} - Retrieve details of a specific patron by ID.
PUT /api/patrons/{id} - Update an existing patron's information.
Required JSON Payload for updated info:
     {
        "name": "patron name",
        "contactInfo": "123456",
        "email": "email@example.com",
        "password": "password"
     }
DELETE /api/patrons/{id} - Remove a patron from the system.

#### Authentication Endpoints
POST /api/auth/login - Authenticate a user and return a JWT.
   Required JSON Payload:
     {
        "email": "email@example.com",
        "password": "password"
     }
POST /api/auth/register - Register a new patron.
Required JSON Payload:
     {
        "name": "patron name",
        "contactInfo": "123456",
        "email": "email@example.com",
        "password": "password"
     }

#### Borrowing Endpoints (authentication required)
POST /api/borrow/{bookId}/patron/{patronId} - Allow a patron to borrow a book.
PUT /api/return/{bookId}/patron/{patronId} - Allow a patron to return a book.
  Obtain JWT Token from Login:
     Extract the JWT token from the response.
     Add the token to the Authorization header in subsequent requests to secured endpoints:  
     Authorization: Bearer your_jwt_token_here
   
### Security
This application uses JWT (JSON Web Token) for securing the API. Endpoints related to borrowing records require a valid JWT obtained from the authentication endpoints.

### Testing
Unit tests are written using JUnit and Mockito. To run the tests, execute:

### Transaction Management
The application uses Spring's @Transactional annotation to manage transactions, ensuring data integrity and consistency during operations involving multiple database accesses.

### Error Handling
The application handles common errors gracefully and returns appropriate HTTP status codes along with descriptive error messages.

### Custom Exceptions
Custom exceptions are used to handle specific scenarios such as resource not found (e.g., book or patron not found), validation failures, and business rule violations. This approach helps in returning more informative and precise error messages.
