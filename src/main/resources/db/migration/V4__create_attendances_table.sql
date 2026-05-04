CREATE TABLE attendances (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    enrollment_id BIGINT NOT NULL,
    attendance_date DATE NOT NULL,
    present BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_attendance_enrollment FOREIGN KEY (enrollment_id) REFERENCES enrollments(id),
    CONSTRAINT uq_attendance_enrollment_date UNIQUE (enrollment_id, attendance_date)
);
