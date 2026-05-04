package com.teacher.organizer.controller;

import com.teacher.organizer.dto.DashboardDto;
import com.teacher.organizer.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public String dashboard(Model model) {
        DashboardDto dto = new DashboardDto(
                dashboardService.getTotalStudents(),
                dashboardService.getTotalCourses(),
                dashboardService.getActiveEnrollments(),
                dashboardService.getUpcomingCourses()
        );
        model.addAttribute("dashboard", dto);
        return "dashboard";
    }
}
