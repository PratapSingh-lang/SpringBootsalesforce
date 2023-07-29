# SpringBootsalesforce

# **Documentation for Backend Application**
This documentation provides details on how to set up and run the backend application, along with information about API endpoints, authentication and additional features.

- **Table of Contents** 
    - Getting Started
    - API Endpoints
    - Authentication
    - Additional Features

**Getting Started**

Follow these steps to set up and run the backend application:

1. Clone the repository from GitHub: git clone https://github.com/PratapSingh-lang/SpringBootsalesforce.git

2. Navigate to the cloned repository: `cd SpringBootsalesforce`
3. Install the required dependencies: `mvn install`

4. Configure the database settings in `application.properties` file.
5. Build the application: `mvn clean package`
6. Run the application: `java -jar target/CodingTech-0.0.1-SNAPSHOT.jar`
7. The backend application will now be running on `http://localhost:7777`

**API Endpoints**

-   Open Swagger UI to get Api endpoints on http://localhost:7777/swagger-ui.html

**Authentication**

- JWT Token based mechanism is enabled on all api endpoints with Authentication and Authorization.

**Additional Features**

- I have provided Dockerfile to dockerise the complete application so that it is machine independent.