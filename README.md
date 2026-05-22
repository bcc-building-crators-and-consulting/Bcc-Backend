BCC Backend — Building Creators & Consulting

Production-grade RESTful API built with Java 21 and Spring Boot 3.5

---

## 🛠️ Tech Stack

| Technology | Version |
|---|---|
| Java | 21 |
| Spring Boot | 3.5.14 |
| MySQL | 8.0 |
| Hibernate | 6.6 |
| JWT | 0.12.6 |
| Docker | Latest |
| Maven | 3.9 |

---

## 🎯 Features

- JWT Authentication & Role-based Access Control
- Admin Dashboard — Projects, Blogs, Careers, Gallery, Team
- OTP Email Verification
- File Upload (Images, Resumes)
- Swagger API Documentation
- Docker Support
- Caffeine Caching

---

## 🚀 Quick Start

### Prerequisites
- Java 21
- MySQL 8.0
- Maven 3.9+
- Docker (optional)

### Run Locally

```bash
# Clone the repository
git clone https://github.com/bcc-building-crators-and-consulting/Bcc-Backend.git

# Go to project folder
cd Bcc-Backend

# Create .env file with your values
cp .env.example .env

# Run the app
mvn spring-boot:run
```

### Run with Docker

```bash
docker-compose up --build
```

---

## 📋 Environment Variables

Create a `.env` file in root:

```env
DB_HOST=localhost
DB_PORT=3306
DB_NAME=bcc_db
DB_USERNAME=root
DB_PASSWORD=your_password

MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=your_app_password

JWT_SECRET=your_jwt_secret
CORS_ORIGINS=http://localhost:3000
UPLOAD_DIR=/app/uploads/
```

---

## 📖 API Documentation

Swagger UI available at:
http://localhost:8080/swagger-ui/index.html

---

## 📁 Project Structure
src/
├── main/
│   ├── java/bcc/group/
│   │   ├── controller/     # REST Controllers
│   │   ├── services/       # Business Logic
│   │   ├── repository/     # Database Layer
│   │   ├── entity/         # JPA Entities
│   │   ├── dto/            # Data Transfer Objects
│   │   ├── security/       # JWT & Auth
│   │   ├── config/         # App Configuration
│   │   └── mapper/         # MapStruct Mappers
│   └── resources/
│       └── application.properties
└── test/

---

## 🔐 API Endpoints

### Auth
| Method | Endpoint | Description |
|---|---|---|
| POST | /api/auth/login | Login with OTP |
| POST | /api/auth/verify-otp | Verify OTP |
| POST | /api/auth/refresh | Refresh Token |

### Team Members
| Method | Endpoint | Description |
|---|---|---|
| GET | /api/team | Get all members |
| POST | /api/admin/team | Add member |
| PUT | /api/admin/team/{id} | Update member |
| DELETE | /api/admin/team/{id} | Delete member |

### Projects
| Method | Endpoint | Description |
|---|---|---|
| GET | /api/projects | Get all projects |
| POST | /api/admin/projects | Add project |

---

## 👥 Organization

**Building Creators & Consulting (BCC)**
- 📧 Email: bccrudrapur@gmail.com

---

## 📄 License

Private — All rights reserved © 2026 BCC Group
