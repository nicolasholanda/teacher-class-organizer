CREATE TABLE enrollments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    enrolled_at TIMESTAMP NOT NULL,
    grade DECIMAL(4, 2),
    status VARCHAR(20) NOT NULL,
    CONSTRAINT fk_enrollment_student FOREIGN KEY (student_id) REFERENCES students(id),
    CONSTRAINT fk_enrollment_course FOREIGN KEY (course_id) REFERENCES courses(id),
    CONSTRAINT uq_enrollment_student_course UNIQUE (student_id, course_id)
);
