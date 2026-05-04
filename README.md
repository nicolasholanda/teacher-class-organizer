# Teacher's Class Organizer

A web application that helps teachers manage their students, courses, enrollments, grades, and attendance — all in one place.

## Tech Stack

- **Java 21** + **Spring Boot 3.3.4**
- **Spring Data JPA** + **H2** (file-based database)
- **Thymeleaf** + **Thymeleaf Layout Dialect**
- **Flyway** for database migrations
- **Bootstrap 5** for the UI
- **Lombok** + **Bean Validation**
- **Maven**

## Features

- **Dashboard** — at-a-glance stats: total students, courses, and active enrollments, plus a full course schedule
- **Students** — create, edit, search, and delete students
- **Courses** — manage course details including day/time scheduling and room assignments
- **Enrollments** — enroll students in courses, update grades, track enrollment status (Enrolled / Completed / Dropped)
- **Attendance** — record and review per-session attendance with an automatic attendance rate calculator

## How to Run

**Prerequisites:** Java 21 and Maven installed.

```bash
git clone https://github.com/nicolasholanda/teacher-class-organizer.git
cd teacher-class-organizer
mvn spring-boot:run
```

Open [http://localhost:8080](http://localhost:8080) in your browser.

The app ships with sample data (5 students, 4 courses, 6 enrollments) loaded automatically on first start. The H2 database is stored in a `data/` directory in the project root and persists between restarts.

The H2 console is available at [http://localhost:8080/h2-console](http://localhost:8080/h2-console) with JDBC URL `jdbc:h2:file:./data/organizer` and username `sa`.
