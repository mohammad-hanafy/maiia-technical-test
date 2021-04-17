package com.maiia.pro.repository;

import com.maiia.pro.entity.TimeSlot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, String> {
    List<TimeSlot> findByPractitionerId(String practitionerId);
    List<TimeSlot> findByPractitionerIdAndEndDateAfterOrderByStartDate(String practitionerId,LocalDateTime endtDate);
	List<TimeSlot> findByPractitionerIdAndEndDateAfterOrderByStartDate(Integer practitionerId, LocalDateTime of);
	List<TimeSlot> findByPractitionerIdOrderByStartDate(Integer practitionerId);
}
