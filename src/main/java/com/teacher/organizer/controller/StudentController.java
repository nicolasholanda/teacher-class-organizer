package com.teacher.organizer.controller;

import com.teacher.organizer.domain.entity.Student;
import com.teacher.organizer.dto.StudentDto;
import com.teacher.organizer.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public String list(@RequestParam(required = false) String search, Model model) {
        model.addAttribute("students", studentService.search(search));
        model.addAttribute("search", search);
        return "students/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("student", new StudentDto());
        return "students/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("student") StudentDto dto,
                         BindingResult result,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "students/form";
        }
        try {
            Student student = new Student();
            mapDtoToEntity(dto, student);
            studentService.save(student);
            redirectAttributes.addFlashAttribute("successMessage", "Student created successfully.");
        } catch (IllegalArgumentException e) {
            result.rejectValue("email", "duplicate.email", e.getMessage());
            return "students/form";
        }
        return "redirect:/students";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Student student = studentService.findById(id);
        StudentDto dto = new StudentDto();
        dto.setId(student.getId());
        dto.setFirstName(student.getFirstName());
        dto.setLastName(student.getLastName());
        dto.setEmail(student.getEmail());
        dto.setDateOfBirth(student.getDateOfBirth());
        dto.setPhone(student.getPhone());
        model.addAttribute("student", dto);
        return "students/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("student") StudentDto dto,
                         BindingResult result,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "students/form";
        }
        try {
            Student updated = new Student();
            mapDtoToEntity(dto, updated);
            studentService.update(id, updated);
            redirectAttributes.addFlashAttribute("successMessage", "Student updated successfully.");
        } catch (IllegalArgumentException e) {
            result.rejectValue("email", "duplicate.email", e.getMessage());
            return "students/form";
        }
        return "redirect:/students";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        studentService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Student deleted.");
        return "redirect:/students";
    }

    private void mapDtoToEntity(StudentDto dto, Student student) {
        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        student.setEmail(dto.getEmail());
        student.setDateOfBirth(dto.getDateOfBirth());
        student.setPhone(dto.getPhone());
    }
}
