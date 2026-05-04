package com.teacher.organizer.controller;

import com.teacher.organizer.domain.entity.Enrollment;
import com.teacher.organizer.dto.AttendanceDto;
import com.teacher.organizer.service.AttendanceService;
import com.teacher.organizer.service.EnrollmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final EnrollmentService enrollmentService;

    @GetMapping("/enrollment/{enrollmentId}")
    public String list(@PathVariable Long enrollmentId, Model model) {
        Enrollment enrollment = enrollmentService.findById(enrollmentId);
        model.addAttribute("enrollment", enrollment);
        model.addAttribute("attendances", attendanceService.findByEnrollmentId(enrollmentId));
        model.addAttribute("attendanceRate", attendanceService.calculateAttendanceRate(enrollmentId));
        return "attendance/list";
    }

    @GetMapping("/enrollment/{enrollmentId}/new")
    public String newForm(@PathVariable Long enrollmentId, Model model) {
        AttendanceDto dto = new AttendanceDto();
        dto.setEnrollmentId(enrollmentId);
        model.addAttribute("attendance", dto);
        model.addAttribute("enrollment", enrollmentService.findById(enrollmentId));
        return "attendance/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("attendance") AttendanceDto dto,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("enrollment", enrollmentService.findById(dto.getEnrollmentId()));
            return "attendance/form";
        }
        try {
            attendanceService.record(dto.getEnrollmentId(), dto.getAttendanceDate(), dto.isPresent());
            redirectAttributes.addFlashAttribute("successMessage", "Attendance recorded.");
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("enrollment", enrollmentService.findById(dto.getEnrollmentId()));
            return "attendance/form";
        }
        return "redirect:/attendance/enrollment/" + dto.getEnrollmentId();
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id,
                         @RequestParam Long enrollmentId,
                         RedirectAttributes redirectAttributes) {
        attendanceService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Attendance record deleted.");
        return "redirect:/attendance/enrollment/" + enrollmentId;
    }
}
