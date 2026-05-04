INSERT INTO students (first_name, last_name, email, date_of_birth, phone, created_at) VALUES
('Alice', 'Johnson', 'alice.johnson@example.com', '2001-03-15', '555-1001', CURRENT_TIMESTAMP),
('Bob', 'Smith', 'bob.smith@example.com', '2000-07-22', '555-1002', CURRENT_TIMESTAMP),
('Clara', 'Davis', 'clara.davis@example.com', '2002-11-08', '555-1003', CURRENT_TIMESTAMP),
('David', 'Martinez', 'david.martinez@example.com', '2001-05-30', '555-1004', CURRENT_TIMESTAMP),
('Emma', 'Wilson', 'emma.wilson@example.com', '2003-01-19', '555-1005', CURRENT_TIMESTAMP);

INSERT INTO courses (name, description, day_of_week, start_time, end_time, room, max_students, created_at) VALUES
('Mathematics 101', 'Introduction to algebra and calculus', 'MONDAY', '08:00:00', '09:30:00', 'Room A1', 20, CURRENT_TIMESTAMP),
('English Literature', 'Classic and modern literature analysis', 'WEDNESDAY', '10:00:00', '11:30:00', 'Room B2', 18, CURRENT_TIMESTAMP),
('Physics Fundamentals', 'Basic mechanics and electromagnetism', 'FRIDAY', '13:00:00', '14:30:00', 'Lab C1', 15, CURRENT_TIMESTAMP),
('History of Art', 'Survey of art movements from antiquity to present', 'TUESDAY', '14:00:00', '15:30:00', 'Room D3', 25, CURRENT_TIMESTAMP);

INSERT INTO enrollments (student_id, course_id, enrolled_at, grade, status) VALUES
(1, 1, CURRENT_TIMESTAMP, 8.50, 'ENROLLED'),
(2, 1, CURRENT_TIMESTAMP, 7.00, 'ENROLLED'),
(3, 2, CURRENT_TIMESTAMP, NULL, 'ENROLLED'),
(4, 3, CURRENT_TIMESTAMP, 9.20, 'ENROLLED'),
(5, 4, CURRENT_TIMESTAMP, NULL, 'ENROLLED'),
(1, 2, CURRENT_TIMESTAMP, 6.80, 'COMPLETED');
