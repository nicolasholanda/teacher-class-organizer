package com.teacher.organizer.dto;

import com.teacher.organizer.domain.enums.EnrollmentStatus;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class EnrollmentDto {

    private Long id;

    @NotNull(message = "Student is required")
    private Long studentId;

    @NotNull(message = "Course is required")
    private Long courseId;

    @DecimalMin(value = "0.00", message = "Grade must be at least 0")
    @DecimalMax(value = "10.00", message = "Grade must be at most 10")
    private BigDecimal grade;

    private EnrollmentStatus status;
}
