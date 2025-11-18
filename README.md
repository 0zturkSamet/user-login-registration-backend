# User Registration & Authentication Backend

A modern, secure REST API for user registration and authentication built with the latest Spring Boot 3.3.5 and Java 21. Features include email verification, JWT-based authentication, and comprehensive API documentation.

## Features

- **User Registration** with email verification
- **JWT Authentication** with access and refresh tokens
- **Email Verification** with token-based confirmation
- **Password Encryption** using BCrypt
- **Modern Spring Security 6** configuration
- **RESTful API** with proper validation
- **API Documentation** with SpringDoc OpenAPI/Swagger
- **Docker Support** with Docker Compose
- **PostgreSQL Database** integration
- **Comprehensive Error Handling**

## Technology Stack

| Technology | Version |
|------------|---------|
| Java | 21 (LTS) |
| Spring Boot | 3.3.5 |
| Spring Security | 6.x |
| Spring Data JPA | 3.x |
| PostgreSQL | 16 |
| JWT (jjwt) | 0.12.6 |
| Maven | 3.9+ |
| Docker | Latest |
| Lombok | Latest |

## Prerequisites

Before running this application, make sure you have:

- **Java 21** or higher installed
- **Maven 3.9+** installed
- **PostgreSQL 16** (or use Docker Compose)
- **Docker & Docker Compose** (optional, for containerized setup)

## Quick Start

### Option 1: Using Docker Compose (Recommended)

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd user-login-registration-backend
   ```

2. **Start all services**
   ```bash
   docker-compose up -d
   ```

   This will start:
   - PostgreSQL database on port `5432`
   - MailDev (email testing) on port `1080` (UI) and `1025` (SMTP)
   - Spring Boot application on port `8484`

3. **Access the application**
   - API Base URL: `http://localhost:8484/registration/v1`
   - Swagger UI: `http://localhost:8484/registration/v1/swagger-ui.html`
   - MailDev UI: `http://localhost:1080`

### Option 2: Manual Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd user-login-registration-backend
   ```

2. **Configure PostgreSQL**
   ```bash
   createdb registration
   ```

3. **Configure environment variables**
   ```bash
   cp .env.example .env
   # Edit .env with your configuration
   ```

4. **Build the application**
   ```bash
   mvn clean install
   ```

5. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

6. **Set up MailDev (for email testing)**
   ```bash
   docker run -d -p 1080:1080 -p 1025:1025 maildev/maildev
   ```

## Configuration

### Environment Variables

Create a `.env` file (use `.env.example` as template):

```bash
# Database
DATABASE_URL=jdbc:postgresql://localhost:5432/registration
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=your_password

# JWT (Generate secure keys for production!)
JWT_SECRET_KEY=your-256-bit-secret-key
JWT_EXPIRATION=86400000
JWT_REFRESH_EXPIRATION=604800000

# Mail
MAIL_HOST=localhost
MAIL_PORT=1025

# Application
CONFIRMATION_URL=http://localhost:8484/registration/v1/api/v1/registration/confirm
```

### Generate JWT Secret Key

For production, generate a secure 256-bit secret key:

```bash
openssl rand -base64 64
```

## API Endpoints

### Authentication Endpoints

#### Register User
```http
POST /api/v1/registration
Content-Type: application/json

{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "password": "SecurePassword123"
}
```

**Response:**
```json
{
  "message": "Registration successful! Please check your email to verify your account.",
  "token": "confirmation-token"
}
```

#### Verify Email
```http
GET /api/v1/registration/confirm?token={confirmation-token}
```

**Response:**
```json
{
  "message": "Email verified successfully! You can now login."
}
```

#### Login
```http
POST /api/v1/auth/login
Content-Type: application/json

{
  "email": "john.doe@example.com",
  "password": "SecurePassword123"
}
```

**Response:**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "email": "john.doe@example.com",
  "firstName": "John",
  "lastName": "Doe"
}
```

#### Refresh Token
```http
POST /api/v1/auth/refresh
Authorization: Bearer {refresh-token}
```

**Response:**
```json
{
  "accessToken": "new-access-token",
  "refreshToken": "new-refresh-token",
  "tokenType": "Bearer",
  "email": "john.doe@example.com",
  "firstName": "John",
  "lastName": "Doe"
}
```

## API Documentation

Interactive API documentation is available via Swagger UI:

**Swagger UI:** `http://localhost:8484/registration/v1/swagger-ui.html`

**OpenAPI JSON:** `http://localhost:8484/registration/v1/v3/api-docs`

## Project Structure

```
src/main/java/com/example/demo/
├── appuser/              # User entity and repository
│   ├── AppUser.java
│   ├── AppUserRole.java
│   ├── AppUserRepository.java
│   └── AppUserService.java
├── auth/                 # Authentication logic
│   ├── AuthenticationController.java
│   └── AuthenticationService.java
├── config/               # Configuration classes
│   └── OpenApiConfig.java
├── dto/                  # Data Transfer Objects
│   ├── AuthenticationRequest.java
│   ├── AuthenticationResponse.java
│   ├── ErrorResponse.java
│   └── RegistrationDto.java
├── email/                # Email service
│   ├── EmailSender.java
│   └── EmailService.java
├── exception/            # Exception handling
│   └── GlobalExceptionHandler.java
├── registration/         # Registration logic
│   ├── RegistrationController.java
│   ├── RegistrationService.java
│   ├── EmailValidator.java
│   └── token/
│       ├── ConfirmationToken.java
│       ├── ConfirmationTokenRepository.java
│       └── ConfirmationTokenService.java
├── security/             # Security configuration
│   ├── config/
│   │   └── WebSecurityConfig.java
│   ├── JwtService.java
│   ├── JwtAuthenticationFilter.java
│   └── PasswordEncoder.java
└── DemoApplication.java
```

## Security Features

### Password Security
- Passwords are encrypted using BCrypt with strength factor 12
- Minimum password length: 8 characters
- Passwords are never stored in plain text

### JWT Authentication
- Stateless authentication using JWT tokens
- Access tokens valid for 24 hours
- Refresh tokens valid for 7 days
- Tokens are signed using HS256 algorithm

### API Security
- CSRF protection disabled (stateless JWT authentication)
- All endpoints require authentication except:
  - `/api/v1/registration/**`
  - `/api/v1/auth/**`
  - `/swagger-ui/**`
  - `/v3/api-docs/**`

## Testing

### Running Tests
```bash
mvn test
```

### Manual Testing with cURL

**Register a user:**
```bash
curl -X POST http://localhost:8484/registration/v1/api/v1/registration \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "password": "SecurePassword123"
  }'
```

**Login:**
```bash
curl -X POST http://localhost:8484/registration/v1/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john.doe@example.com",
    "password": "SecurePassword123"
  }'
```

**Access protected endpoint:**
```bash
curl -X GET http://localhost:8484/registration/v1/api/v1/protected \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

## Email Testing

This application uses MailDev for email testing in development:

1. **Access MailDev UI:** `http://localhost:1080`
2. All emails sent by the application will appear here
3. Click on emails to view confirmation links

## Database Schema

### AppUser Table
| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT | Primary key |
| first_name | VARCHAR | User's first name |
| last_name | VARCHAR | User's last name |
| email | VARCHAR | User's email (unique) |
| password | VARCHAR | Encrypted password |
| app_user_role | VARCHAR | User role (USER, ADMIN) |
| locked | BOOLEAN | Account locked status |
| enabled | BOOLEAN | Account enabled status |

### ConfirmationToken Table
| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT | Primary key |
| token | VARCHAR | Confirmation token |
| created_at | TIMESTAMP | Token creation time |
| expires_at | TIMESTAMP | Token expiration time |
| confirmed_at | TIMESTAMP | Token confirmation time |
| app_user_id | BIGINT | Foreign key to AppUser |

## Error Handling

The API returns standardized error responses:

```json
{
  "timestamp": "2025-01-15T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid input parameters",
  "path": "/api/v1/registration",
  "details": [
    "email: Email should be valid",
    "password: Password must be at least 8 characters"
  ]
}
```

## Common HTTP Status Codes

| Status Code | Description |
|-------------|-------------|
| 200 | Success |
| 201 | Created |
| 400 | Bad Request (validation error) |
| 401 | Unauthorized (invalid credentials) |
| 404 | Not Found |
| 500 | Internal Server Error |

## Docker Commands

```bash
# Start all services
docker-compose up -d

# View logs
docker-compose logs -f

# Stop all services
docker-compose down

# Rebuild and start
docker-compose up -d --build

# Remove volumes (clean start)
docker-compose down -v
```

## Troubleshooting

### Port Already in Use
If port 8484 is already in use, change it in `application.yml`:
```yaml
server:
  port: 8080
```

### Database Connection Error
Ensure PostgreSQL is running and credentials are correct:
```bash
docker-compose logs postgres
```

### Email Not Sending
Check MailDev is running:
```bash
docker ps | grep maildev
```

## Production Deployment

### Security Checklist

- [ ] Generate a strong JWT secret key
- [ ] Use environment variables for sensitive data
- [ ] Configure proper SMTP server (not MailDev)
- [ ] Set `spring.jpa.hibernate.ddl-auto` to `validate` or `none`
- [ ] Enable HTTPS/TLS
- [ ] Configure CORS properly
- [ ] Set up rate limiting
- [ ] Enable database connection pooling
- [ ] Configure proper logging levels
- [ ] Set up monitoring and health checks

### Environment-Specific Configuration

Create `application-prod.yml` for production:

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false

logging:
  level:
    com.example.demo: INFO
    org.springframework.security: WARN
```

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For issues and questions:
- Create an issue in the repository
- Email: support@example.com

## Acknowledgments

- Spring Framework Team
- Spring Security Team
- JWT.io for token documentation
- PostgreSQL Community

---

**Built with Spring Boot 3.3.5 and Java 21**
