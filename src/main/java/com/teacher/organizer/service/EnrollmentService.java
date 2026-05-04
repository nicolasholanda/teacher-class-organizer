package com.teacher.organizer.service;

import com.teacher.organizer.domain.entity.Course;
import com.teacher.organizer.domain.entity.Enrollment;
import com.teacher.organizer.domain.entity.Student;
import com.teacher.organizer.domain.enums.EnrollmentStatus;
import com.teacher.organizer.repository.EnrollmentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentService studentService;
    private final CourseService courseService;

    public List<Enrollment> findAll() {
        return enrollmentRepository.findAll();
    }

    public Enrollment findById(Long id) {
        return enrollmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Enrollment not found with id: " + id));
    }

    public List<Enrollment> findByStudentId(Long studentId) {
        return enrollmentRepository.findByStudentId(studentId);
    }

    public List<Enrollment> findByCourseId(Long courseId) {
        return enrollmentRepository.findByCourseId(courseId);
    }

    @Transactional
    public Enrollment enroll(Long studentId, Long courseId) {
        Student student = studentService.findById(studentId);
        Course course = courseService.findById(courseId);

        if (enrollmentRepository.existsByStudentIdAndCourseId(studentId, courseId)) {
            throw new IllegalArgumentException("Student is already enrolled in this course.");
        }
        if (!course.hasAvailableSpots()) {
            throw new IllegalArgumentException("Course has reached its maximum capacity.");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setStatus(EnrollmentStatus.ENROLLED);
        return enrollmentRepository.save(enrollment);
    }

    @Transactional
    public Enrollment updateGrade(Long id, BigDecimal grade) {
        Enrollment enrollment = findById(id);
        enrollment.setGrade(grade);
        return enrollmentRepository.save(enrollment);
    }

    @Transactional
    public Enrollment updateStatus(Long id, EnrollmentStatus status) {
        Enrollment enrollment = findById(id);
        enrollment.setStatus(status);
        return enrollmentRepository.save(enrollment);
    }

    @Transactional
    public void drop(Long id) {
        Enrollment enrollment = findById(id);
        enrollment.setStatus(EnrollmentStatus.DROPPED);
        enrollmentRepository.save(enrollment);
    }

    public long countActive() {
        return enrollmentRepository.countByStatus(EnrollmentStatus.ENROLLED);
    }
}
