# Spring WebSocket Project

This project demonstrates how to integrate WebSocket with a Spring Boot application. It includes WebSocket endpoints for handling connections and sending messages, as well as a RESTful API for managing users.

## Features

- WebSocket communication using Spring WebSocket
- RESTful API to manage users
- Automatically broadcasts messages to all WebSocket clients when a new user is added
- User data is persisted using JPA and MySQL

## Technology Stack

- Java 22
- Spring Boot 3.1.5
- Spring WebSocket
- Spring Data JPA
- MySQL
- Lombok
- Jackson for JSON parsing

## Dependencies

The project uses the following dependencies:

- `spring-boot-starter-web`: For building web applications and RESTful APIs.
- `spring-boot-starter-websocket`: For WebSocket support in Spring Boot.
- `spring-boot-starter-data-jpa`: For integrating with a relational database (MySQL in this case).
- `mysql-connector-j`: MySQL JDBC driver.
- `spring-boot-starter-actuator`: For monitoring and management of Spring Boot applications.
- `spring-boot-starter-thymeleaf`: For rendering web pages (not used in this project but included for future expansion).
- `lombok`: For reducing boilerplate code (Getters, Setters, Constructors, etc.).

## Setup Instructions

1. Clone the repository to your local machine:

    ```bash
    git clone <repository_url>
    cd <project_directory>
    ```

2. Set up the MySQL database. Create a new database (e.g., `websocket_db`) and update the `application.properties` with your MySQL credentials.

3. Run the application:

    ```bash
    ./mvnw spring-boot:run
    ```

4. The application will start on port `8080`.

## WebSocket Endpoints

- `ws://localhost:8080/bin`: WebSocket endpoint for handling `BinWebSocketHandler`.
- `ws://localhost:8080/app`: WebSocket endpoint for handling `AppWebSocketHandler`.

### Sending a Message via WebSocket

- You can send a `boolean` message to the `/app` endpoint. If the message is `"true"` or `"false"`, the server will acknowledge it with `"Received: true"` or `"Received: false"`.
  
### Broadcasting a Message

- Whenever a new user is added via the `/user/add` endpoint, the `BinWebSocketHandler` broadcasts the user's details to all connected WebSocket clients.

## API Endpoints

### `POST /user/add`

Add a new user to the system. The request body should contain a `User` object.

**Request Example:**

```json
{
  "name": "John Doe",
  "email": "john.doe@example.com",
  "password": "password123"
}
