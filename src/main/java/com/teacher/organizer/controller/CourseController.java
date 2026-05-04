package com.teacher.organizer.controller;

import com.teacher.organizer.domain.entity.Course;
import com.teacher.organizer.dto.CourseDto;
import com.teacher.organizer.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.DayOfWeek;

@Controller
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    public String list(@RequestParam(required = false) String search, Model model) {
        model.addAttribute("courses", courseService.search(search));
        model.addAttribute("search", search);
        model.addAttribute("daysOfWeek", DayOfWeek.values());
        return "courses/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("course", new CourseDto());
        model.addAttribute("daysOfWeek", DayOfWeek.values());
        return "courses/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("course") CourseDto dto,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("daysOfWeek", DayOfWeek.values());
            return "courses/form";
        }
        Course course = new Course();
        mapDtoToEntity(dto, course);
        courseService.save(course);
        redirectAttributes.addFlashAttribute("successMessage", "Course created successfully.");
        return "redirect:/courses";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Course course = courseService.findById(id);
        CourseDto dto = new CourseDto();
        dto.setId(course.getId());
        dto.setName(course.getName());
        dto.setDescription(course.getDescription());
        dto.setDayOfWeek(course.getDayOfWeek());
        dto.setStartTime(course.getStartTime());
        dto.setEndTime(course.getEndTime());
        dto.setRoom(course.getRoom());
        dto.setMaxStudents(course.getMaxStudents());
        model.addAttribute("course", dto);
        model.addAttribute("daysOfWeek", DayOfWeek.values());
        return "courses/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("course") CourseDto dto,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("daysOfWeek", DayOfWeek.values());
            return "courses/form";
        }
        Course updated = new Course();
        mapDtoToEntity(dto, updated);
        courseService.update(id, updated);
        redirectAttributes.addFlashAttribute("successMessage", "Course updated successfully.");
        return "redirect:/courses";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        courseService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Course deleted.");
        return "redirect:/courses";
    }

    private void mapDtoToEntity(CourseDto dto, Course course) {
        course.setName(dto.getName());
        course.setDescription(dto.getDescription());
        course.setDayOfWeek(dto.getDayOfWeek());
        course.setStartTime(dto.getStartTime());
        course.setEndTime(dto.getEndTime());
        course.setRoom(dto.getRoom());
        course.setMaxStudents(dto.getMaxStudents());
    }
}
