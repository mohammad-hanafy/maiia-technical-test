package com.maiia.pro.service;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.time.Month;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.maiia.pro.EntityFactory;
import com.maiia.pro.entity.Appointment;
import com.maiia.pro.entity.Availability;
import com.maiia.pro.entity.Practitioner;
import com.maiia.pro.exception.AppointmentConflictException;
import com.maiia.pro.exception.NoAvailabilityForAppointmentException;
import com.maiia.pro.repository.AvailabilityRepository;
import com.maiia.pro.repository.PractitionerRepository;

@SpringBootTest
class ProAppointmentServiceTest {
	
	private final  EntityFactory entityFactory = new EntityFactory();    
    @Autowired
    private ProAppointmentService proAppointmentService;

    @Autowired
    private AvailabilityRepository availabilityRepository;

    @Autowired
    private PractitionerRepository practitionerRepository;


	@Test
	void createAppointment() throws AppointmentConflictException, NoAvailabilityForAppointmentException {
		Practitioner practitioner = practitionerRepository.save(entityFactory.createPractitioner());
		
		LocalDateTime startDate = LocalDateTime.of(2020, Month.FEBRUARY, 5, 11, 0, 0);
		Availability availability = Availability
				.builder()
				.practitionerId(practitioner.getId())
				.startDate(startDate)
				.endDate(startDate.plusMinutes(15)).build();
		availability = availabilityRepository.save(availability);
		
		Appointment appointment = Appointment
				.builder()
				.practitionerId(practitioner.getId())
				.startDate(startDate)
				.endDate(startDate.plusMinutes(15)).build();
		Appointment actualAppointment = proAppointmentService.addAppointment(appointment);
		
		assertTrue(actualAppointment.getId() != null);
		
	}
	
	
	@Test
	void createAppointmentOverlapOtherAppointment() throws AppointmentConflictException, NoAvailabilityForAppointmentException {
		Practitioner practitioner = practitionerRepository.save(entityFactory.createPractitioner());
				
		LocalDateTime startDate = LocalDateTime.of(2020, Month.FEBRUARY, 5, 11, 0, 0);
		Availability availability = Availability
				.builder()
				.practitionerId(practitioner.getId())
				.startDate(startDate)
				.endDate(startDate.plusMinutes(15)).build();
		availability = availabilityRepository.save(availability);
		
		Appointment appointment1 = Appointment
				.builder()
				.practitionerId(practitioner.getId())
				.startDate(startDate)
				.endDate(startDate.plusMinutes(15)).build();
		
		Appointment appointment2 = Appointment
				.builder()
				.practitionerId(practitioner.getId())
				.startDate(startDate)
				.endDate(startDate.plusMinutes(15)).build();
		
		proAppointmentService.addAppointment(appointment1);
		
		Exception exception = assertThrows(AppointmentConflictException.class, () -> {
			proAppointmentService.addAppointment(appointment2);
	    });
		
		assertTrue(exception.getMessage().equals("Appointment Conflicts with other appointments"));
		
	}
	
	@Test
	void createAppointmentWithNoAvailability() throws AppointmentConflictException, NoAvailabilityForAppointmentException {
		Practitioner practitioner = practitionerRepository.save(entityFactory.createPractitioner());
		
		LocalDateTime startDate = LocalDateTime.of(2020, Month.FEBRUARY, 5, 11, 0, 0);
		
		Appointment appointment = Appointment
				.builder()
				.practitionerId(practitioner.getId())
				.startDate(startDate)
				.endDate(startDate.plusMinutes(15)).build();
		
		Exception exception = assertThrows(NoAvailabilityForAppointmentException.class, () -> {
			proAppointmentService.addAppointment(appointment);
	    });
		
		assertTrue(exception.getMessage().equals("No Availabilty Exists for Appointment"));	
	}
	
	@Test
	void createAppointmentBetweenEdgesOfTwoAvailabilities() throws AppointmentConflictException, NoAvailabilityForAppointmentException {
		Practitioner practitioner = practitionerRepository.save(entityFactory.createPractitioner());
		
		LocalDateTime startDate = LocalDateTime.of(2020, Month.FEBRUARY, 5, 11, 0, 0);
		Availability availability1 = Availability
				.builder()
				.practitionerId(practitioner.getId())
				.startDate(startDate)
				.endDate(startDate.plusMinutes(15)).build();
		availability1 = availabilityRepository.save(availability1);
		Availability availability2 = Availability
				.builder()
				.practitionerId(practitioner.getId())
				.startDate(startDate.plusMinutes(30))
				.endDate(startDate.plusMinutes(45)).build();
		availability1 = availabilityRepository.save(availability2);
		
		Appointment appointment = Appointment
				.builder()
				.practitionerId(practitioner.getId())
				.startDate(startDate.plusMinutes(15))
				.endDate(startDate.plusMinutes(30)).build();
		
		Exception exception = assertThrows(NoAvailabilityForAppointmentException.class, () -> {
			proAppointmentService.addAppointment(appointment);
	    });
		
		assertTrue(exception.getMessage().equals("No Availabilty Exists for Appointment"));	
		
	}
	
	

}
