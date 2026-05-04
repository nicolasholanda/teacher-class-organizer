package com.teacher.organizer.service;

import com.teacher.organizer.domain.entity.Course;
import com.teacher.organizer.domain.entity.Enrollment;
import com.teacher.organizer.domain.entity.Student;
import com.teacher.organizer.domain.enums.EnrollmentStatus;
import com.teacher.organizer.repository.EnrollmentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnrollmentServiceTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private StudentService studentService;

    @Mock
    private CourseService courseService;

    @InjectMocks
    private EnrollmentService enrollmentService;

    private Student student;
    private Course course;
    private Enrollment enrollment;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setId(1L);
        student.setFirstName("Alice");
        student.setLastName("Smith");

        course = new Course();
        course.setId(1L);
        course.setName("Math 101");
        course.setMaxStudents(20);

        enrollment = new Enrollment();
        enrollment.setId(1L);
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setStatus(EnrollmentStatus.ENROLLED);
    }

    @Test
    void enroll_validStudentAndCourse_savesEnrollment() {
        when(studentService.findById(1L)).thenReturn(student);
        when(courseService.findById(1L)).thenReturn(course);
        when(enrollmentRepository.existsByStudentIdAndCourseId(1L, 1L)).thenReturn(false);
        when(enrollmentRepository.save(any())).thenReturn(enrollment);

        Enrollment result = enrollmentService.enroll(1L, 1L);

        assertThat(result.getStudent()).isEqualTo(student);
        verify(enrollmentRepository).save(any());
    }

    @Test
    void enroll_alreadyEnrolled_throwsIllegalArgumentException() {
        when(studentService.findById(1L)).thenReturn(student);
        when(courseService.findById(1L)).thenReturn(course);
        when(enrollmentRepository.existsByStudentIdAndCourseId(1L, 1L)).thenReturn(true);

        assertThatThrownBy(() -> enrollmentService.enroll(1L, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("already enrolled");
    }

    @Test
    void enroll_courseAtCapacity_throwsIllegalArgumentException() {
        course.setMaxStudents(0);
        when(studentService.findById(1L)).thenReturn(student);
        when(courseService.findById(1L)).thenReturn(course);
        when(enrollmentRepository.existsByStudentIdAndCourseId(1L, 1L)).thenReturn(false);

        assertThatThrownBy(() -> enrollmentService.enroll(1L, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("capacity");
    }

    @Test
    void drop_setsStatusDropped() {
        when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment));
        when(enrollmentRepository.save(any())).thenReturn(enrollment);

        enrollmentService.drop(1L);

        assertThat(enrollment.getStatus()).isEqualTo(EnrollmentStatus.DROPPED);
        verify(enrollmentRepository).save(enrollment);
    }

    @Test
    void findById_missingId_throwsEntityNotFoundException() {
        when(enrollmentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> enrollmentService.findById(99L))
                .isInstanceOf(EntityNotFoundException.class);
    }
}
