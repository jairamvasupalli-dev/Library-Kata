# Library Application

A simple Spring Boot REST application for managing a library catalog. It supports
adding books, borrowing and returning books, and listing the books borrowed by a user.

## Project Overview

The application exposes a small set of REST APIs to:

1. Add a new book to the catalog.
2. Borrow a book.
3. Return a book.
4. List the books borrowed by a given user.

The application uses an H2 in-memory database, so all data is reset on every restart.

## Technologies Used

- Java 21
- Spring Boot 3.x
- Maven
- Spring Web
- Spring Data JPA
- H2 In-Memory Database
- Lombok

## Project Structure

```
src/main/java/com/example/library
├── controller
│   └── BookController.java
├── service
│   └── BookService.java
├── repository
│   └── BookRepository.java
├── entity
│   └── Book.java
├── dto
│   ├── AddBookRequest.java
│   ├── BorrowBookRequest.java
│   └── BookResponse.java
└── LibraryApplication.java
```

## How to Run the Application

Make sure you have **JDK 21** and **Maven** installed.

### Build the project

```bash
mvn clean install
```

### Run the application

```bash
mvn spring-boot:run
```

The application starts on **http://localhost:8080**.

## H2 Console

Once the application is running, the H2 console is available at:

```
http://localhost:8080/h2-console
```

Connection settings:

- **JDBC URL:** `jdbc:h2:mem:librarydb`
- **User Name:** `sa`
- **Password:** *(leave blank)*

## API Endpoints

| Method | Endpoint                  | Description                          |
|--------|---------------------------|--------------------------------------|
| POST   | `/books`                  | Add a new book                       |
| POST   | `/books/{id}/borrow`      | Borrow a book                        |
| POST   | `/books/{id}/return`      | Return a book                        |
| GET    | `/users/{userName}/books` | List books borrowed by a user        |

---

### 1. Add Book

**Request**

```
POST /books
Content-Type: application/json
```

```json
{
  "title": "Clean Code",
  "author": "Robert C. Martin"
}
```

**Response** — `201 Created`

```json
{
  "id": 1,
  "title": "Clean Code",
  "author": "Robert C. Martin",
  "borrowed": false,
  "borrowedBy": null
}
```

---

### 2. Borrow Book

**Request**

```
POST /books/1/borrow
Content-Type: application/json
```

```json
{
  "userName": "John"
}
```

**Response** — `200 OK`

```json
{
  "id": 1,
  "title": "Clean Code",
  "author": "Robert C. Martin",
  "borrowed": true,
  "borrowedBy": "John"
}
```

**Error Response** — `409 Conflict` (book already borrowed)

```json
{
  "status": 409,
  "error": "Conflict",
  "path": "/books/1/borrow"
}
```

**Error Response** — `404 Not Found` (book does not exist)

```json
{
  "status": 404,
  "error": "Not Found",
  "path": "/books/99/borrow"
}
```

---

### 3. Return Book

**Request**

```
POST /books/1/return
```

**Response** — `200 OK`

```json
{
  "id": 1,
  "title": "Clean Code",
  "author": "Robert C. Martin",
  "borrowed": false,
  "borrowedBy": null
}
```

**Error Response** — `404 Not Found` (book does not exist)

```json
{
  "status": 404,
  "error": "Not Found",
  "path": "/books/99/return"
}
```

---

### 4. List Borrowed Books

**Request**

```
GET /users/John/books
```

**Response** — `200 OK`

```json
[
  {
    "id": 1,
    "title": "Clean Code",
    "author": "Robert C. Martin",
    "borrowed": true,
    "borrowedBy": "John"
  }
]
```

## Maven Commands

| Command                 | Description                          |
|-------------------------|--------------------------------------|
| `mvn clean install`     | Clean, build, run tests, and package |
| `mvn spring-boot:run`   | Run the application                  |
| `mvn test`              | Run the tests                        |
