package com.teacher.organizer.service;

import com.teacher.organizer.domain.entity.Attendance;
import com.teacher.organizer.domain.entity.Enrollment;
import com.teacher.organizer.repository.AttendanceRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final EnrollmentService enrollmentService;

    public List<Attendance> findByEnrollmentId(Long enrollmentId) {
        return attendanceRepository.findByEnrollmentIdOrderByAttendanceDateDesc(enrollmentId);
    }

    public Attendance findById(Long id) {
        return attendanceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Attendance record not found with id: " + id));
    }

    @Transactional
    public Attendance record(Long enrollmentId, LocalDate date, boolean present) {
        Enrollment enrollment = enrollmentService.findById(enrollmentId);

        if (attendanceRepository.existsByEnrollmentIdAndAttendanceDate(enrollmentId, date)) {
            throw new IllegalArgumentException("Attendance already recorded for this date.");
        }

        Attendance attendance = new Attendance();
        attendance.setEnrollment(enrollment);
        attendance.setAttendanceDate(date);
        attendance.setPresent(present);
        return attendanceRepository.save(attendance);
    }

    @Transactional
    public Attendance update(Long id, boolean present) {
        Attendance attendance = findById(id);
        attendance.setPresent(present);
        return attendanceRepository.save(attendance);
    }

    @Transactional
    public void delete(Long id) {
        Attendance attendance = findById(id);
        attendanceRepository.delete(attendance);
    }

    public double calculateAttendanceRate(Long enrollmentId) {
        long total = attendanceRepository.countTotalByEnrollmentId(enrollmentId);
        if (total == 0) return 0.0;
        long present = attendanceRepository.countPresentByEnrollmentId(enrollmentId);
        return (double) present / total * 100;
    }
}
