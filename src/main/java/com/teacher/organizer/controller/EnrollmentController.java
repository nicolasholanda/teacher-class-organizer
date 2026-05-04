package com.teacher.organizer.controller;

import com.teacher.organizer.domain.entity.Enrollment;
import com.teacher.organizer.domain.enums.EnrollmentStatus;
import com.teacher.organizer.dto.EnrollmentDto;
import com.teacher.organizer.service.CourseService;
import com.teacher.organizer.service.EnrollmentService;
import com.teacher.organizer.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;
    private final StudentService studentService;
    private final CourseService courseService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("enrollments", enrollmentService.findAll());
        return "enrollments/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("enrollment", new EnrollmentDto());
        model.addAttribute("students", studentService.findAll());
        model.addAttribute("courses", courseService.findAll());
        return "enrollments/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("enrollment") EnrollmentDto dto,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("students", studentService.findAll());
            model.addAttribute("courses", courseService.findAll());
            return "enrollments/form";
        }
        try {
            enrollmentService.enroll(dto.getStudentId(), dto.getCourseId());
            redirectAttributes.addFlashAttribute("successMessage", "Student enrolled successfully.");
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("students", studentService.findAll());
            model.addAttribute("courses", courseService.findAll());
            return "enrollments/form";
        }
        return "redirect:/enrollments";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Enrollment enrollment = enrollmentService.findById(id);
        EnrollmentDto dto = new EnrollmentDto();
        dto.setId(enrollment.getId());
        dto.setStudentId(enrollment.getStudent().getId());
        dto.setCourseId(enrollment.getCourse().getId());
        dto.setGrade(enrollment.getGrade());
        dto.setStatus(enrollment.getStatus());
        model.addAttribute("enrollment", dto);
        model.addAttribute("statuses", EnrollmentStatus.values());
        model.addAttribute("studentName", enrollment.getStudent().getFullName());
        model.addAttribute("courseName", enrollment.getCourse().getName());
        return "enrollments/form";
    }

    @PostMapping("/{id}/grade")
    public String updateGrade(@PathVariable Long id,
                              @Valid @ModelAttribute("enrollment") EnrollmentDto dto,
                              BindingResult result,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        if (result.hasFieldErrors("grade")) {
            Enrollment enrollment = enrollmentService.findById(id);
            model.addAttribute("statuses", EnrollmentStatus.values());
            model.addAttribute("studentName", enrollment.getStudent().getFullName());
            model.addAttribute("courseName", enrollment.getCourse().getName());
            return "enrollments/form";
        }
        enrollmentService.updateGrade(id, dto.getGrade());
        if (dto.getStatus() != null) {
            enrollmentService.updateStatus(id, dto.getStatus());
        }
        redirectAttributes.addFlashAttribute("successMessage", "Enrollment updated.");
        return "redirect:/enrollments";
    }

    @PostMapping("/{id}/drop")
    public String drop(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        enrollmentService.drop(id);
        redirectAttributes.addFlashAttribute("successMessage", "Enrollment dropped.");
        return "redirect:/enrollments";
    }
}
