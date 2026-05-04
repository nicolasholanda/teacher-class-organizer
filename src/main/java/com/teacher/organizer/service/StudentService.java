package com.teacher.organizer.service;

import com.teacher.organizer.domain.entity.Student;
import com.teacher.organizer.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentService {

    private final StudentRepository studentRepository;

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public Student findById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Student not found with id: " + id));
    }

    public List<Student> search(String name) {
        if (name == null || name.isBlank()) {
            return studentRepository.findAll();
        }
        return studentRepository.searchByName(name.trim());
    }

    @Transactional
    public Student save(Student student) {
        if (studentRepository.existsByEmail(student.getEmail())) {
            throw new IllegalArgumentException("Email already in use: " + student.getEmail());
        }
        return studentRepository.save(student);
    }

    @Transactional
    public Student update(Long id, Student updated) {
        Student existing = findById(id);
        if (studentRepository.existsByEmailAndIdNot(updated.getEmail(), id)) {
            throw new IllegalArgumentException("Email already in use: " + updated.getEmail());
        }
        existing.setFirstName(updated.getFirstName());
        existing.setLastName(updated.getLastName());
        existing.setEmail(updated.getEmail());
        existing.setDateOfBirth(updated.getDateOfBirth());
        existing.setPhone(updated.getPhone());
        return studentRepository.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        Student student = findById(id);
        studentRepository.delete(student);
    }

    public long count() {
        return studentRepository.count();
    }
}
