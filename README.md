# Job Board API
##### This project is a Java backend (Java 11+) that uses the ArbeitNow Job Board API to fetch job listings and store them in an H2 database. The API is public and does not require authentication.

### Technologies Used
- Java 11 or higher
- Maven
- Docker
- Spring Boot, JPA, MVC
- H2
- JUnit

### Getting Started
##### Prerequisites
- Java 11 or higher
- Maven
- Docker (optional)

##### Running the Application
- Clone this repository
- Navigate to the project root directory
- Build the project using Maven: mvn clean package
- Run the JAR file: java -jar target/job-board-api-1.0-SNAPSHOT.jar
Alternatively, you can run the application in a Docker container:

Build the Docker image: docker build -t job-board-api .
Run the container: docker run -p 8080:8080 job-board-api
Endpoints
### The application provides the following REST API endpoints:

- Get All Job Listings (Returns all job listings in the database)
- Get Location statistics (Returns job amount per location)
- Get Top10 Jobs by views
- Upload 5 pages with jobs from external api  and save to our H2 db

##### Also application uses @Scheduled which automatically uploads jobs from api every day at 00:00(u can provide ur personal crone)
