
```
# Weather Application

The Weather Application is a Spring Boot application that provides weather information for various cities using 
a third-party weather service. It is built with Java 17 and follows the Hexagonal Architecture pattern for
 modularity and maintainability. The application integrates with OpenMeteo Weather API and IP Geolocation 
 API to retrieve weather data and geolocation information. The project is Dockerized and includes multistage builds
 for efficient containerization.

## Features

- Retrieves weather information for geolocation info retrieved by IP addres
- Utilizes OpenMeteo Weather API as the data source
- Integrates with IP Geolocation API for geolocation information
- Uses Spring Open Feign for API communication
- Implements Mapstruct for object mapping
- Applies Liquibase for database schema management
- Provides OpenAPI documentation for API endpoints
- Includes integration tests for comprehensive testing
- Dockerized with multistage builds for efficient containerization

## Technologies Used

- Java 17
- Spring Boot
- Spring Open Feign
- Liquibase
- Open API
- Mapstruct
- Hexagonal Architecture
- Integration Testing
- Docker

## Getting Started

Follow these instructions to build and run the project using Docker Compose:

### Prerequisites

- Docker
- Docker Compose

### Installation

1. Navigate to the project directory:

```
cd weather
```

3. Build the Docker images and set up the containers using Docker Compose:

```
docker-compose build
docker-compose up
```

### Usage

Explore the OpenAPI documentation:

   - Access the Swagger UI at `http://localhost:9094/api/v1/swagger-ui/index.html#/`.

### How to make request
1. GET http://localhost:8080/weather
2. Request should contain 2 headers: 
   * X-Forwarded-For (your external IP address)
   * X-API-KEY (auth api key, default = `M2ZjNDZmOWItMzJmMC00YzhlLWE3ZTctNDY3YzQ2YzAzZjli`)
Example:
```
curl --location 'http://localhost:9094/api/v1/weather' \
--header 'X-Forwarded-For: 81.198.20.21' \
--header 'X-API-KEY: M2ZjNDZmOWItMzJmMC00YzhlLWE3ZTctNDY3YzQ2YzAzZjli'
```
