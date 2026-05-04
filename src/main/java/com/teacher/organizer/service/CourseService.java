package com.teacher.organizer.service;

import com.teacher.organizer.domain.entity.Course;
import com.teacher.organizer.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseService {

    private final CourseRepository courseRepository;

    public List<Course> findAll() {
        return courseRepository.findAllOrderedBySchedule();
    }

    public Course findById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Course not found with id: " + id));
    }

    public List<Course> search(String name) {
        if (name == null || name.isBlank()) {
            return courseRepository.findAllOrderedBySchedule();
        }
        return courseRepository.searchByName(name.trim());
    }

    @Transactional
    public Course save(Course course) {
        return courseRepository.save(course);
    }

    @Transactional
    public Course update(Long id, Course updated) {
        Course existing = findById(id);
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setDayOfWeek(updated.getDayOfWeek());
        existing.setStartTime(updated.getStartTime());
        existing.setEndTime(updated.getEndTime());
        existing.setRoom(updated.getRoom());
        existing.setMaxStudents(updated.getMaxStudents());
        return courseRepository.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        Course course = findById(id);
        courseRepository.delete(course);
    }

    public long count() {
        return courseRepository.count();
    }
}
