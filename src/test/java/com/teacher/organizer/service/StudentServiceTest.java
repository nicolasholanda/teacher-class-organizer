package com.teacher.organizer.service;

import com.teacher.organizer.domain.entity.Student;
import com.teacher.organizer.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    private Student student;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setId(1L);
        student.setFirstName("Alice");
        student.setLastName("Smith");
        student.setEmail("alice@example.com");
    }

    @Test
    void findAll_returnsAllStudents() {
        when(studentRepository.findAll()).thenReturn(List.of(student));

        List<Student> result = studentService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEmail()).isEqualTo("alice@example.com");
    }

    @Test
    void findById_existingId_returnsStudent() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        Student result = studentService.findById(1L);

        assertThat(result.getFirstName()).isEqualTo("Alice");
    }

    @Test
    void findById_missingId_throwsEntityNotFoundException() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.findById(99L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void save_uniqueEmail_savesStudent() {
        when(studentRepository.existsByEmail("alice@example.com")).thenReturn(false);
        when(studentRepository.save(student)).thenReturn(student);

        Student result = studentService.save(student);

        assertThat(result).isEqualTo(student);
        verify(studentRepository).save(student);
    }

    @Test
    void save_duplicateEmail_throwsIllegalArgumentException() {
        when(studentRepository.existsByEmail("alice@example.com")).thenReturn(true);

        assertThatThrownBy(() -> studentService.save(student))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Email already in use");
    }

    @Test
    void delete_existingStudent_deletesSuccessfully() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        studentService.delete(1L);

        verify(studentRepository).delete(student);
    }

    @Test
    void count_returnsRepositoryCount() {
        when(studentRepository.count()).thenReturn(5L);

        assertThat(studentService.count()).isEqualTo(5L);
    }
}
