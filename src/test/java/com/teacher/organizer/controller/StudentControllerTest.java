package com.teacher.organizer.controller;

import com.teacher.organizer.domain.entity.Student;
import com.teacher.organizer.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
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
    void list_returnsStudentsView() throws Exception {
        when(studentService.search(null)).thenReturn(List.of(student));

        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(view().name("students/list"))
                .andExpect(model().attributeExists("students"));
    }

    @Test
    void newForm_returnsFormView() throws Exception {
        mockMvc.perform(get("/students/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("students/form"))
                .andExpect(model().attributeExists("student"));
    }

    @Test
    void create_validInput_redirectsToList() throws Exception {
        when(studentService.save(any())).thenReturn(student);

        mockMvc.perform(post("/students")
                        .param("firstName", "Alice")
                        .param("lastName", "Smith")
                        .param("email", "alice@example.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/students"));
    }

    @Test
    void create_invalidInput_returnsFormView() throws Exception {
        mockMvc.perform(post("/students")
                        .param("firstName", "")
                        .param("lastName", "")
                        .param("email", "not-an-email"))
                .andExpect(status().isOk())
                .andExpect(view().name("students/form"));
    }

    @Test
    void editForm_existingStudent_returnsFormView() throws Exception {
        when(studentService.findById(1L)).thenReturn(student);

        mockMvc.perform(get("/students/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("students/form"))
                .andExpect(model().attributeExists("student"));
    }

    @Test
    void delete_existingStudent_redirectsToList() throws Exception {
        doNothing().when(studentService).delete(1L);

        mockMvc.perform(post("/students/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/students"));
    }
}
