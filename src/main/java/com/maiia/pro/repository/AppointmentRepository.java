package com.maiia.pro.repository;

import com.maiia.pro.entity.Appointment;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, String> {
    List<Appointment> findByPractitionerId(String practitionerId);
    List<Appointment> findAll();
    List<Appointment> findByPractitionerIdOrderByStartDate(Integer practitionerId);
    List<Appointment> findByPractitionerIdAndStartDateAfterOrderByStartDate(Integer practitionerId, LocalDateTime startDateAfter);
    List<Appointment> findByPractitionerIdAndStartDateGreaterThanEqualAndStartDateLessThan(Integer practitionerId, LocalDateTime startDate, LocalDateTime endDate);
    List<Appointment> findByPractitionerIdAndEndDateGreaterThanAndEndDateLessThanEqual(Integer practitionerId, LocalDateTime startDate, LocalDateTime endDate);

}

