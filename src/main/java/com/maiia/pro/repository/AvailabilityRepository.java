package com.maiia.pro.repository;

import com.maiia.pro.entity.Availability;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, String> {
    List<Availability> findByPractitionerId(Integer id);
    void deleteByPractitionerId(Integer id);
    List<Availability> findByPractitionerIdAndStartDateGreaterThanEqualAndEndDateLessThanEqual(Integer id, LocalDateTime startDate, LocalDateTime endDate);
    
}
