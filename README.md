# KnowledgeBase Backend - Spring Boot

A robust, secure, and scalable backend for the Knowledge Sharing Platform, built with Spring Boot and MySQL.

## 1. Approach

### Architecture Overview
The backend follows a standard **Spring Boot MVC architecture**, ensuring a clean separation of concerns:
- **Presentation Layer**: REST Controllers handling HTTP requests.
- **Business Logic Layer**: Services implementing the core functionality and AI integrations.
- **Data Access Layer**: JPA Repositories for MySQL database interaction.
- **Security Layer**: Custom JWT Authentication Filter and Spring Security configuration.

### Folder Structure
```text
src/main/java/com/example/knowledgeplatform/
├── config/        # Security and CORS configurations
├── controller/    # REST API Endpoints (Auth, Articles)
├── entity/        # Database models (User, Article)
├── repository/    # JPA Interfaces for DB access
├── service/       # Business logic and AI service implementations
├── util/          # Utility classes (JWT, String helpers)
└── KnowledgePlatformApplication.java
```

### Key Design Decisions
- **Stateless Authentication**: Uses JWT (JSON Web Tokens) to maintain user sessions without server-side state, enhancing scalability.
- **Environmental Stability**: Explicitly removed Lombok annotations in favor of standard Java Getters/Setters to ensure zero-dependency compilation across different developer environments.
- **Centralized Security**: Implemented a global CORS configuration and an explicit `SecurityConfig` to manage public vs. protected routes (e.g., AI features require auth, reading articles does not).

## 2. AI Usage (Mandatory Section)

### Tools Used
- **Antigravity / Gemini**: Primary AI coding assistant for architecture design and debugging.

### How AI Helped
- **Code Generation**: Scaffolded initial JPA entities and Spring Boot controller templates. Generated the `JwtAuthenticationFilter` logic for token extraction.
- **Refactoring**: Assisted in the strategic decision to migrate from Lombok to explicit POJOs to resolve environment-specific compilation errors.
- **SQL Queries**: Verified database integrity and article counts during migration phases using custom query suggestions.
- **API Design**: Designed the structure for AI-powered endpoints (Improve Content, Suggest Tags) to be consistent with standard REST practices.

### Manual Corrections
- **JWT Troubleshooting**: Manually resolved `UnsatisfiedDependencyException` by properly configuring `jwt.secret` in the properties file after identifying a missing value during AI-assisted debugging.
- **CORS Conflict Resolution**: Manually synchronized global CORS settings in `SecurityConfig` with local controller configurations to fix 403 Forbidden errors.

## 3. Setup Instructions

### Prerequisites
- **Java 17** (JDK)
- **Maven 3.8+**
- **MySQL 8.0+**

### Environment Variables / Properties
Update `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/knowledge_platform
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
jwt.secret=your_64_character_ultra_secure_secret_key
jwt.expiration=86400000
openai.api.key=YOUR_OPENAI_KEY
```

### Installation
1. Create the database: `CREATE DATABASE knowledge_platform;`
2. Run the SQL script located in the root directory: `mysql -u root -p < database.sql`
3. Build the project:
   ```bash
   mvn clean install
   ```
4. Run the application:
   ```bash
   mvn spring-boot:run
   ```
   *Server will start on [http://localhost:8080](http://localhost:8080)*
