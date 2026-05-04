package com.teacher.organizer.service;

import com.teacher.organizer.domain.entity.Course;
import com.teacher.organizer.repository.CourseRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseService courseService;

    private Course course;

    @BeforeEach
    void setUp() {
        course = new Course();
        course.setId(1L);
        course.setName("Math 101");
        course.setDayOfWeek(DayOfWeek.MONDAY);
        course.setStartTime(LocalTime.of(8, 0));
        course.setEndTime(LocalTime.of(9, 30));
        course.setMaxStudents(20);
    }

    @Test
    void findAll_returnsOrderedCourses() {
        when(courseRepository.findAllOrderedBySchedule()).thenReturn(List.of(course));

        List<Course> result = courseService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Math 101");
    }

    @Test
    void findById_existingId_returnsCourse() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        Course result = courseService.findById(1L);

        assertThat(result.getDayOfWeek()).isEqualTo(DayOfWeek.MONDAY);
    }

    @Test
    void findById_missingId_throwsEntityNotFoundException() {
        when(courseRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> courseService.findById(99L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void save_persistsCourse() {
        when(courseRepository.save(course)).thenReturn(course);

        Course result = courseService.save(course);

        assertThat(result).isEqualTo(course);
        verify(courseRepository).save(course);
    }

    @Test
    void delete_existingCourse_deletesSuccessfully() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        courseService.delete(1L);

        verify(courseRepository).delete(course);
    }

    @Test
    void count_returnsRepositoryCount() {
        when(courseRepository.count()).thenReturn(3L);

        assertThat(courseService.count()).isEqualTo(3L);
    }
}
