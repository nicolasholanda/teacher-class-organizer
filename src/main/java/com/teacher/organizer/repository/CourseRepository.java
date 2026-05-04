package com.teacher.organizer.repository;

import com.teacher.organizer.domain.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByDayOfWeek(DayOfWeek dayOfWeek);

    @Query("SELECT c FROM Course c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Course> searchByName(String name);

    @Query("SELECT c FROM Course c ORDER BY c.dayOfWeek, c.startTime")
    List<Course> findAllOrderedBySchedule();
}
