package com.teacher.organizer.service;

import com.teacher.organizer.domain.entity.Course;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {

    private final StudentService studentService;
    private final CourseService courseService;
    private final EnrollmentService enrollmentService;

    public long getTotalStudents() {
        return studentService.count();
    }

    public long getTotalCourses() {
        return courseService.count();
    }

    public long getActiveEnrollments() {
        return enrollmentService.countActive();
    }

    public List<Course> getUpcomingCourses() {
        return courseService.findAll();
    }
}
