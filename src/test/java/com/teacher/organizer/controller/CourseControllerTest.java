package com.teacher.organizer.controller;

import com.teacher.organizer.domain.entity.Course;
import com.teacher.organizer.service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CourseController.class)
class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
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
    void list_returnsCoursesView() throws Exception {
        when(courseService.search(null)).thenReturn(List.of(course));

        mockMvc.perform(get("/courses"))
                .andExpect(status().isOk())
                .andExpect(view().name("courses/list"))
                .andExpect(model().attributeExists("courses"));
    }

    @Test
    void newForm_returnsFormView() throws Exception {
        mockMvc.perform(get("/courses/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("courses/form"))
                .andExpect(model().attributeExists("course"));
    }

    @Test
    void create_validInput_redirectsToList() throws Exception {
        when(courseService.save(any())).thenReturn(course);

        mockMvc.perform(post("/courses")
                        .param("name", "Math 101")
                        .param("dayOfWeek", "MONDAY")
                        .param("startTime", "08:00")
                        .param("endTime", "09:30")
                        .param("maxStudents", "20"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/courses"));
    }

    @Test
    void create_invalidInput_returnsFormView() throws Exception {
        mockMvc.perform(post("/courses")
                        .param("name", "")
                        .param("maxStudents", "-1"))
                .andExpect(status().isOk())
                .andExpect(view().name("courses/form"));
    }

    @Test
    void editForm_existingCourse_returnsFormView() throws Exception {
        when(courseService.findById(1L)).thenReturn(course);

        mockMvc.perform(get("/courses/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("courses/form"))
                .andExpect(model().attributeExists("course"));
    }

    @Test
    void delete_existingCourse_redirectsToList() throws Exception {
        doNothing().when(courseService).delete(1L);

        mockMvc.perform(post("/courses/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/courses"));
    }
}
