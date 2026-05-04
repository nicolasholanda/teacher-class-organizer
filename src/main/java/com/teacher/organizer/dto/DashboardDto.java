package com.teacher.organizer.dto;

import com.teacher.organizer.domain.entity.Course;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class DashboardDto {

    private long totalStudents;
    private long totalCourses;
    private long activeEnrollments;
    private List<Course> upcomingCourses;
}
