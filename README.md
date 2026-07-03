# Cloud Storage Application

A secure cloud storage web application built with **Spring Boot**, **Thymeleaf**, **MyBatis**, and **PostgreSQL**.

This application allows users to:
- Sign up and log in securely
- Upload, download, and delete files
- Create, update, and delete notes
- Create, update, and delete credentials
- Store credentials securely using encryption
- Access only their own data

---

## Requirements

Before running the project, make sure you have:

- Java 17
- Maven 3+
- PostgreSQL
- Google Chrome (for Selenium tests)

## Features

### User Authentication
- Secure signup and login
- Password hashing with salt
- Session management using Spring Security

### File Management
- Upload files
- Download files
- Delete files
- Prevent duplicate filenames
- Handle large uploads gracefully

### Notes Management
- Add notes
- Edit notes
- Delete notes

### Credential Management
- Add credentials
- Edit credentials
- Delete credentials
- Encrypt passwords before storing in database
- Decrypt passwords while editing

### Security
- Route protection
- User-specific data isolation
- Ownership validation for files, notes, and credentials

---

## Tech Stack

- Java 17
- Spring Boot
- Spring Security
- Thymeleaf
- MyBatis
- PostgreSQL
- Selenium
- Maven

---

## Clone Repository

```bash
git clone https://github.com/deepakpatel45/Cloud-Storage-Application.git
```

Move into project:

```bash
cd Cloud-Storage-Application
```

Move into source folder:

```bash
cd starter/cloudstorage
```

---

## Database Setup

Create PostgreSQL database:

```sql
CREATE DATABASE cloudstorage;
```

Update database credentials in:

```text
src/main/resources/application.properties
```

Example:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/cloudstorage
spring.datasource.username=your_username
spring.datasource.password=your_password
```

---

## Run Application

From:

```text
starter/cloudstorage
```

Run:

```bash
mvn spring-boot:run
```

Application URL:

```text
http://localhost:8080
```

---

## Run Tests

From:

```text
starter/cloudstorage
```

Run:

```bash
mvn test
```

Expected:

```text
BUILD SUCCESS
```

---

## Project Structure

```text
starter/cloudstorage
├── src/main/java
│   ├── controller
│   ├── services
│   ├── mapper
│   ├── model
├── src/main/resources
│   ├── templates
│   ├── static
│   ├── application.properties
├── src/test/java
├── pom.xml
├── upload5m.zip
```

---

## Test Coverage

- Login page test
- Signup redirection test
- Invalid URL handling test
- Large file upload test

---

## Author

Paidakula Sai Deepak

GitHub: https://github.com/deepakpatel45
