package com.maiia.pro.service;

import com.maiia.pro.entity.Appointment;
import com.maiia.pro.entity.Availability;
import com.maiia.pro.exception.AppointmentConflictException;
import com.maiia.pro.exception.NoAvailabilityForAppointmentException;
import com.maiia.pro.repository.AppointmentRepository;
import com.maiia.pro.repository.AvailabilityRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

@Service
public class ProAppointmentService {
    @Autowired
    private AppointmentRepository appointmentRepository;
    
    @Autowired
    private AvailabilityRepository availabilityRepository;

    public Appointment find(String appointmentId) {
        return appointmentRepository.findById(appointmentId).orElseThrow();
    }

    public List<Appointment> findAll() {
        return appointmentRepository.findAll();
    }

    public List<Appointment> findByPractitionerId(String practitionerId) {
        return appointmentRepository.findByPractitionerId(practitionerId);
    }
    
    @Transactional
    public Appointment addAppointment(Appointment appointmnet) throws AppointmentConflictException, NoAvailabilityForAppointmentException {
    	Integer practionerId = appointmnet.getPractitionerId();
    	LocalDateTime appointmentStartDate = appointmnet.getStartDate();
    	LocalDateTime appointmentEndDate = appointmnet.getEndDate();
    	
    	//check Appointment within Availability
    	List<Availability> availabilities = 
    			availabilityRepository.findByPractitionerIdAndStartDateGreaterThanEqualAndEndDateLessThanEqual(practionerId, appointmentStartDate, appointmentEndDate);
    	if (availabilities.isEmpty()) {
    		throw new NoAvailabilityForAppointmentException();
		}
    	
    	//check No Appointments overlap
		List<Appointment> appointmnetsWithStartDatesConflict = appointmentRepository
				.findByPractitionerIdAndStartDateGreaterThanEqualAndStartDateLessThan(practionerId, appointmentStartDate, appointmentEndDate);
		List<Appointment> appointmnetsWithEndDatesConflict = appointmentRepository
				.findByPractitionerIdAndEndDateGreaterThanAndEndDateLessThanEqual(practionerId, appointmentStartDate, appointmentEndDate);
		
		if(appointmnetsWithStartDatesConflict.isEmpty()
				&& appointmnetsWithEndDatesConflict.isEmpty()) {
			return appointmentRepository.save(appointmnet);
		}else {
			throw new AppointmentConflictException();
		}

	}
}
