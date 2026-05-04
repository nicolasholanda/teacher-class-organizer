package com.teacher.organizer.repository;

import com.teacher.organizer.domain.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    List<Attendance> findByEnrollmentId(Long enrollmentId);

    Optional<Attendance> findByEnrollmentIdAndAttendanceDate(Long enrollmentId, LocalDate date);

    boolean existsByEnrollmentIdAndAttendanceDate(Long enrollmentId, LocalDate date);

    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.enrollment.id = :enrollmentId AND a.present = true")
    long countPresentByEnrollmentId(Long enrollmentId);

    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.enrollment.id = :enrollmentId")
    long countTotalByEnrollmentId(Long enrollmentId);

    List<Attendance> findByEnrollmentIdOrderByAttendanceDateDesc(Long enrollmentId);
}
