# 🚀 BlogSphere Backend

<div align="center">

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-21-ED8B00.svg)](https://openjdk.java.net/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0+-4479A1.svg)](https://www.mysql.com/)
[![Hibernate](https://img.shields.io/badge/Hibernate-JPA-59666C.svg)](https://hibernate.org/)
[![JWT](https://img.shields.io/badge/JWT-0.12.6-000000.svg)](https://jwt.io/)
[![Maven](https://img.shields.io/badge/Maven-Latest-C71A36.svg)](https://maven.apache.org/)

**Robust and scalable backend API for the BlogSphere blogging platform**

</div>

---

## 📋 Table of Contents

-   [🌟 Project Overview](#-project-overview)
-   [🛠️ Tech Stack](#️-tech-stack)
-   [⚙️ IDE Setup](#️-ide-setup)
-   [🔧 Troubleshooting](#-troubleshooting)
-   [👥 Contribute](#-contributors)
-   [📈 Changelog](#-changelog)

---

## 🌟 Project Overview

**BlogSphere Backend** is a comprehensive REST API built with Spring Boot that powers the BlogSphere blogging platform. It provides a secure, scalable, and feature-rich foundation for modern blogging applications with advanced social features and AI integration.

### ✨ Key Features

-   🔐 **JWT Authentication & Authorization** - Secure user authentication with role-based access control
-   📝 **Complete Blog Management** - Full CRUD operations for blogs with rich content support
-   🤖 **AI-Powered Content Enhancement** - Intelligent text improvement using OpenRouter AI API
-   💬 **Interactive Comment System** - Comments with likes and real-time interactions
-   👥 **Social Features** - User following, blog saving, and social interactions
-   📧 **Email Integration** - Automated email notifications and password reset functionality
-   📁 **File Upload System** - Secure file handling with size validation and storage management
-   🛡️ **Spring Security Integration** - Comprehensive security with CORS and request filtering
-   📊 **Admin Dashboard API** - Administrative endpoints for platform management

### 🏗️ Architecture

-   **RESTful API Design** - Clean, predictable endpoint structure
-   **Layered Architecture** - Controller → Service → Repository pattern
-   **Data Validation** - Jakarta Bean Validation with custom validators
-   **Exception Handling** - Global exception handling with meaningful error responses
-   **Database Optimization** - Efficient JPA queries with Hibernate optimization

---

## 🛠️ Tech Stack

### 🔧 Core Framework & Runtime

| Technology      | Version | Purpose                                          |
| --------------- | ------- | ------------------------------------------------ |
| **Java**        | 21      | Modern JVM language with latest features         |
| **Spring Boot** | 3.4.5   | Enterprise-grade framework for rapid development |
| **Maven**       | Latest  | Dependency management and build automation       |

### 🗄️ Data Layer

| Technology             | Version | Purpose                                   |
| ---------------------- | ------- | ----------------------------------------- |
| **Spring Data JPA**    | 3.4.5   | Data access layer with repository pattern |
| **Hibernate**          | Latest  | Object-relational mapping (ORM) framework |
| **MySQL Connector**    | Latest  | Database connectivity and driver          |
| **Jakarta Validation** | 3.1.1   | Bean validation for data integrity        |

### 🔒 Security & Authentication

| Technology              | Version | Purpose                          |
| ----------------------- | ------- | -------------------------------- |
| **Spring Security**     | 3.4.5   | Comprehensive security framework |
| **JWT (JJWT)**          | 0.12.6  | JSON Web Token implementation    |
| **JJWT API**            | 0.12.6  | JWT creation and parsing         |
| **JJWT Implementation** | 0.12.6  | Runtime JWT processing           |
| **JJWT Jackson**        | 0.12.6  | JSON processing for JWT          |

### 📧 Communication & AI

| Technology            | Version | Purpose                        |
| --------------------- | ------- | ------------------------------ |
| **Spring Boot Mail**  | 3.4.5   | Email sending capabilities     |
| **OpenRouter AI API** | Latest  | AI-powered content enhancement |

### 🛠️ Development & Utilities

| Technology               | Version | Purpose                         |
| ------------------------ | ------- | ------------------------------- |
| **Lombok**               | Latest  | Boilerplate code reduction      |
| **Spring Boot DevTools** | 3.4.5   | Development-time enhancements   |
| **Spring Boot Test**     | 3.4.5   | Testing framework and utilities |
| **Spring Security Test** | 3.4.5   | Security testing support        |

---

## ⚙️ IDE Setup

### 🌟 Eclipse STS (Spring Tool Suite) Setup

#### Prerequisites

-   **Java 21** or higher
-   **Eclipse STS 4.x** or higher
-   **MySQL 8.0** or higher
-   **Maven 3.6** or higher

#### Step-by-Step Installation

1. **Open Eclipse STS**

    - Launch Eclipse STS
    - Select or create a workspace

1. **Import the Project**

    ```
    File → Import → Git → Projects from Git (with smart import) → Clone URI
    ```

    - Paste `https://github.com/debosmit10/BlogSphere-Backend.git` in URI → `Next >`
    - Select branches to clone → `Next >`
    - Select local destination → `Next >`
    - `Finish`

1. **Configure Project Properties**

    - Right-click project → Properties
    - Go to "Java Build Path" → "Libraries"
    - Ensure Java 21 is configured
    - If not, click "Modulepath" → "Add Library" → "JRE System Library"

1. **Database Setup**

    ```sql
    CREATE DATABASE blogspheredb;
    ```

1. **Create .env**

    - Create a .env file in the root directory of the project — the same level as your pom.xml and .gitignore.

    - Configure .env file

    ```properties
    DB_USERNAME=your_db_username
    DB_PASSWORD=your_db_password

    OPENROUTER_API_KEY=your_openrouter_key

    MAIL_USERNAME=you@gmail.com
    MAIL_PASSWORD=gmail_app_pwd
    ```

1. **Run the Application**

-   Right-click on `BlogsphereApplication.java`
-   Select "Run As" → "Java Application"
-   Or use Maven: `mvn spring-boot:run`

### 💡 IntelliJ IDEA Setup

#### Prerequisites

-   **IntelliJ IDEA 2023.x** or higher (Community/Ultimate)
-   **Java 21** or higher
-   **MySQL 8.0** or higher

#### Step-by-Step Installation

1. **Clone the Repository**

```bash
git clone https://github.com/yourusername/BlogSphere-Backend.git
```

2. **Open IntelliJ IDEA**

    - Choose "Open" from the welcome screen
    - Navigate to the cloned directory and select it
    - Click "OK"

3. **Project SDK Configuration**

    ```
    File → Project Structure → Project Settings → Project
    ```

    - Set Project SDK to Java 21
    - Set Project language level to "21 - Pattern matching for switch, record patterns"

4. **Maven Configuration**

    - IntelliJ should automatically detect the Maven project
    - If not, go to View → Tool Windows → Maven
    - Click the refresh button to reload dependencies

5. **Database Setup**

    - Use the same SQL commands as Eclipse STS setup above

6. **Enable Annotation Processing**

    ```
    File → Settings → Build, Execution, Deployment → Compiler → Annotation Processors
    ```

    - Check "Enable annotation processing" (for Lombok)

7. **Create .env**

    - Same as Eclipse STS configuration above

8. **Run the Application**
    - Open `BlogsphereApplication.java`
    - Click the green play button next to the class name
    - Or use the Maven toolbar: `Plugins → spring-boot → spring-boot:run`

---

## 🔧 Troubleshooting

### 🚨 Common Issues & Solutions

<details>
<summary><strong>📋 Import & Dependencies Issues</strong></summary>

#### Maven Dependencies Not Downloading

```bash
# Solution 1: Force dependency update
mvn clean install -U

# Solution 2: Clear local repository
rm -rf ~/.m2/repository
mvn clean install
```

#### Lombok Not Working

**Eclipse STS:**

1. Download lombok.jar from https://projectlombok.org/
2. Run: `java -jar lombok.jar`
3. Install into Eclipse STS installation directory
4. Restart Eclipse STS

**IntelliJ IDEA:**

1. Install Lombok plugin: `File → Settings → Plugins → Search "Lombok"`
2. Enable annotation processing (see setup steps above)
3. Restart IntelliJ

#### Java Version Mismatch

```bash
# Check Java version
java -version
javac -version

# Set JAVA_HOME (Windows)
set JAVA_HOME=C:\Program Files\Java\jdk-21

# Set JAVA_HOME (Linux/Mac)
export JAVA_HOME=/usr/lib/jvm/java-21-openjdk
```

</details>

<!--
<details>
<summary><strong>🗄️ Database Connection Issues</strong></summary>

#### MySQL Connection Refused
```bash
# Check MySQL service status
sudo systemctl status mysql

# Start MySQL service
sudo systemctl start mysql

# Verify MySQL is running on port 3306
netstat -tulpn | grep 3306
```

#### Database Authentication Failed
```sql
-- Reset MySQL password
ALTER USER 'root'@'localhost' IDENTIFIED BY 'new_password';
FLUSH PRIVILEGES;

-- Create specific user for application
CREATE USER 'blogsphere'@'localhost' IDENTIFIED BY 'secure_password';
GRANT ALL PRIVILEGES ON blogspheredb.* TO 'blogsphere'@'localhost';
```

#### Schema/Table Not Found
```properties
# Enable DDL auto-creation
spring.jpa.hibernate.ddl-auto=create-drop

# For production, use:
spring.jpa.hibernate.ddl-auto=validate
```

</details>
-->

<details>
<summary><strong>🔒 Security & Authentication Issues</strong></summary>

#### JWT Token Issues

```properties
# Ensure consistent secret key
jwt.secret=your-very-long-secret-key-at-least-256-bits-long

# Check token expiration
jwt.expiration=86400000
```

#### CORS Configuration

```java
// Add to WebConfig if needed
@CrossOrigin(origins = "http://localhost:3000")
```

#### Email Configuration Issues

```properties
# Gmail App Password Setup Required
spring.mail.username=your-email@gmail.com
spring.mail.password=your-16-character-app-password
```

</details>

<!--
<details>
<summary><strong>🚀 Runtime & Performance Issues</strong></summary>

#### Port Already in Use
```bash
# Kill process on port 8080
sudo lsof -t -i:8080 | xargs sudo kill -9

# Or use different port
server.port=8081
```

#### OutOfMemoryError
```bash
# Increase heap size
java -Xmx2048m -jar target/blogsphere-1.11.0.jar

# Or in IDE VM options
-Xmx2048m -Xms512m
```

#### Slow Database Queries
```properties
# Enable query logging for debugging
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
logging.level.org.hibernate.SQL=DEBUG
```

</details>
-->

### 📞 Additional Support

If you encounter issues not covered here:

1. Check the [Issues](https://github.com/debosmit10/BlogSphere-Backend/issues) page
2. Search existing solutions or create a new issue
3. Include error logs, environment details, and reproduction steps

---

## 👥 Contribute

<!--
<div align="center">

### 🏆 Development Team

| Avatar | Name | Role | GitHub | LinkedIn |
|--------|------|------|--------|----------|
| <img src="https://github.com/debosmit10.png" width="50" height="50" style="border-radius: 50%"> | **Your Name** | Lead Backend Developer | [@yourusername](https://github.com/yourusername) | [LinkedIn](https://linkedin.com/in/yourprofile) |
| <img src="https://github.com/contributor1.png" width="50" height="50" style="border-radius: 50%"> | **Contributor 1** | Backend Developer | [@contributor1](https://github.com/contributor1) | [LinkedIn](https://linkedin.com/in/contributor1) |
| <img src="https://github.com/contributor2.png" width="50" height="50" style="border-radius: 50%"> | **Contributor 2** | DevOps Engineer | [@contributor2](https://github.com/contributor2) | [LinkedIn](https://linkedin.com/in/contributor2) |

</div>
-->

### 🤝 How to Contribute

We welcome contributions from the community! Please follow these steps:

1. **Fork the Repository**

    ```bash
    git clone https://github.com/yourusername/BlogSphere-Backend.git
    ```

2. **Create Feature Branch**

    ```bash
    git checkout -b feature/amazing-new-feature
    ```

3. **Development Guidelines**

    - Follow Java naming conventions
    - Write comprehensive unit tests
    - Add JavaDoc comments for public methods
    - Ensure code passes all existing tests

4. **Commit Changes**

    ```bash
    git commit -m "feat: add amazing new feature"
    ```

5. **Submit Pull Request**
    - Provide clear description of changes
    - Include test coverage information
    - Reference any related issues

### 📝 Contributing Guidelines

-   **Code Style**: Follow Spring Boot best practices
-   **Testing**: Maintain minimum 80% test coverage

---

## 📈 Changelog

### 📊 Version History

| Version     | Release Date | Status     | Features            |
| ----------- | ------------ | ---------- | ------------------- |
| **v1.11.0** | 18-6-2025    | ✅ Release | Full production API |

<details>
<summary><strong>📋 Read More - Detailed Changelog</strong></summary>

#### 🚀 v1.11.0 (18-6-2025)

-   **🎉 Major Features**

    -   ✅ **Complete Authentication System**

        -   JWT-based authentication with refresh token support
        -   Role-based authorization (USER, ADMIN)
        -   Secure password hashing with BCrypt
        -   Account verification via email

    -   ✅ **Advanced Blog Management**

        -   Full CRUD operations for blog posts
        -   Rich text content support with file attachments
        -   Topic-based categorization and filtering
        -   View tracking and analytics
        -   Blog saving/bookmarking functionality

    -   ✅ **AI-Powered Features**

        -   Content enhancement using OpenRouter AI API
        -   Intelligent text improvement suggestions
        -   Automated content optimization

    -   ✅ **Social Interaction System**

        -   User following/unfollowing functionality
        -   Comment system with nested replies
        -   Like system for blogs and comments
        -   User profile management with customizable information

    -   ✅ **File Management**
        -   Secure file upload with validation
        -   Profile picture management
        -   File size and type restrictions
        -   Optimized storage handling

-   **🔧 Technical Achievements**

    -   ✅ **Spring Boot 3.4.5** - Latest stable release with performance improvements
    -   ✅ **Java 21** - Modern language features and enhanced performance
    -   ✅ **Advanced Security** - Comprehensive protection against common vulnerabilities
    -   ✅ **Database Optimization** - Efficient JPA queries and relationship management
    -   ✅ **Email Integration** - SMTP configuration for notifications and password reset

-   **🐛 Critical Fixes**
    -   ✅ Fixed JWT token expiration handling
    -   ✅ Resolved CORS configuration issues
    -   ✅ Corrected database transaction management
    -   ✅ Enhanced error handling and logging

---

<!--
#### 🛡️ v0.0.0 (00-00-2025)

- **🔒 Security Improvements**
  - ✅ Enhanced JWT security with stronger algorithms

- **🎯 New Features**
  - ✅ Admin dashboard API endpoints

- **🔄 Improvements**
  - ✅ Enhanced exception handling with detailed error responses

- **🐛 Bug Fixes**
  - ✅ Fixed memory leaks in file upload process

---
-->
</details>

---

<div align="center">

### 🌟 Star this repository if you found it helpful!

**Built with ❤️ using Spring Boot and Java 21**

[⬆ Back to Top](#-blogsphere-backend)

</div>
