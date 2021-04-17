package com.maiia.pro.service;

import com.maiia.pro.configuration.AppProperties;
import com.maiia.pro.entity.Appointment;
import com.maiia.pro.entity.Availability;
import com.maiia.pro.entity.TimeSlot;
import com.maiia.pro.exception.NotImplementedException;
import com.maiia.pro.repository.AppointmentRepository;
import com.maiia.pro.repository.AvailabilityRepository;
import com.maiia.pro.repository.TimeSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import javax.transaction.Transactional;

@Service
public class ProAvailabilityService {

    @Autowired
    private AvailabilityRepository availabilityRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private TimeSlotRepository timeSlotRepository;
    
    @Autowired
    private AppProperties appProperties;

    public List<Availability> findByPractitionerId(Integer practitionerId) {
        return availabilityRepository.findByPractitionerId(practitionerId);
    }

    @Transactional
    public List<Availability> generateAvailabilities(Integer practitionerId) throws NotImplementedException {
    	// delete old availabilities
    	availabilityRepository.deleteByPractitionerId(practitionerId);
    	
    	List<TimeSlot> practionerTimeSlots = timeSlotRepository.findByPractitionerIdOrderByStartDate(practitionerId);
    	List<TimeSlot> practionerMergedTimeSlots = mergeSortedTimeSlots(practionerTimeSlots);
    	List<Appointment> practionerAppointments = appointmentRepository.findByPractitionerIdOrderByStartDate(practitionerId);
    	List<Availability> availabilities =  calculateAvailabilitiesFromTimeSlotsAndAppointments(practionerMergedTimeSlots,practionerAppointments, appProperties.getAppointmentDuration());
    	// save new availabilities
    	availabilities = (List<Availability>) availabilityRepository.saveAll(availabilities);
    	
    	return availabilities;
    }
    
    private List<TimeSlot> mergeSortedTimeSlots(List<TimeSlot> timeSlots){
    	// handling null availableTimeSlots
    	if(timeSlots == null)
    		return null;
    	
    	LinkedList<TimeSlot> mergedTimeSlots = new LinkedList<>();
    	
    	// handling empty availableTimeSlots
    	if(timeSlots.size() <= 0)
    		return mergedTimeSlots;
    	
    	//initialize with first timeSlot
    	mergedTimeSlots.add(timeSlots.get(0));
    	
    	for(TimeSlot timeSlot: timeSlots) {
    		TimeSlot latestMergedTimeSlot = mergedTimeSlots.getLast();
    		if(isSlotsOverLaps(latestMergedTimeSlot, timeSlot)) {
    			// update latestMergedTimeSlot
    			TimeSlot mergedSlot = mergeTimeSlots(timeSlot, latestMergedTimeSlot);
    			mergedTimeSlots.removeLast();
    			mergedTimeSlots.add(mergedSlot);
    			
    		}else {
    			mergedTimeSlots.add(timeSlot);
    		}	
    	}
    	return mergedTimeSlots;
    }
    
    private boolean isSlotsOverLaps(TimeSlot firstTimeSlot, TimeSlot secondTimeSlot) {
    	
    	if(secondTimeSlot.getStartDate().isBefore(firstTimeSlot.getEndDate()) ||
    			secondTimeSlot.getStartDate().isEqual(firstTimeSlot.getEndDate())) {
    		return true;
    	}else {
    		return false;
    	}
    	
    }
    
    private TimeSlot mergeTimeSlots(TimeSlot timeSlot1, TimeSlot timeSlot2) {
    	TimeSlot mergedTimeSlot = TimeSlot.builder()
    			.startDate(minLocalDateTime(timeSlot1.getStartDate(), timeSlot2.getStartDate()))
    			.endDate(maxLocalDateTime(timeSlot1.getEndDate(), timeSlot2.getEndDate()))
    			.practitionerId(timeSlot1.getPractitionerId())
    			.build();
    	return mergedTimeSlot;
    }

	private List<Availability> calculateAvailabilitiesFromTimeSlotsAndAppointments(List<TimeSlot> mergedTimeSlots, List<Appointment> sortedAppointments, Integer appoitmentDuration) {
    	List<Availability> availabilities = new LinkedList<Availability>();
    	
    	for(TimeSlot currentTimeSlot: mergedTimeSlots) {   		
    		// find appointments within time slots
    		List<TimeSlot> subTimeSlots = removeAppointmentsFromTimeSlot(currentTimeSlot, sortedAppointments);
    		availabilities.addAll(generateAvailabilitiesFromSubTimeSlots(subTimeSlots, appoitmentDuration));		
    	}
		return availabilities;
	}

	private List<TimeSlot> removeAppointmentsFromTimeSlot(TimeSlot currentTimeSlot, List<Appointment> sortedAppointments) {
		
		List<TimeSlot> subTimeSlots = new LinkedList<>();
		subTimeSlots.add(currentTimeSlot);
		subTimeSlots = subtractAppointmentsFromTimeSlots(subTimeSlots, sortedAppointments);
		return subTimeSlots;
	}

	private List<Availability> generateAvailabilitiesFromSubTimeSlots(List<TimeSlot> mergedTimeSlots,
			Integer appointmentTimeByMinutes) {
    	
    	List<Availability> availabilities = new LinkedList<Availability>();
    	ListIterator<TimeSlot> timeSlotsIterator = mergedTimeSlots.listIterator();
    	while(timeSlotsIterator.hasNext()) {
    		TimeSlot currentTimeSlot = timeSlotsIterator.next();
    		Duration duration = Duration.between(currentTimeSlot.getStartDate(), currentTimeSlot.getEndDate());
    		int durationInMinutes = (int) duration.toMinutes();
    		int numberOfAppintmentsInSlot = durationInMinutes / appointmentTimeByMinutes;
			for(int j=0; j< numberOfAppintmentsInSlot;j++) {
				LocalDateTime availabilityStartDate = currentTimeSlot.getStartDate().plusMinutes(j * appointmentTimeByMinutes);
				LocalDateTime availabilityEndDate = availabilityStartDate.plusMinutes(appointmentTimeByMinutes);
				Integer practionerId = currentTimeSlot.getPractitionerId();
				Availability availability = Availability.builder()
						.startDate(availabilityStartDate)
						.endDate(availabilityEndDate)
						.practitionerId(practionerId)
						.build();
				availabilities.add(availability);
			}

			if(!timeSlotsIterator.hasNext()) {
				if(durationInMinutes % appointmentTimeByMinutes > 0) {
					LocalDateTime availabilityStartDate = currentTimeSlot.getStartDate().plusMinutes(numberOfAppintmentsInSlot * appointmentTimeByMinutes);
					LocalDateTime availabilityEndDate = availabilityStartDate.plusMinutes(appointmentTimeByMinutes);
					Integer practionerId = currentTimeSlot.getPractitionerId();
					Availability availability = Availability.builder()
							.startDate(availabilityStartDate)
							.endDate(availabilityEndDate)
							.practitionerId(practionerId)
							.build();
					availabilities.add(availability);
				}
			}
    		
		}
    	
		return availabilities;
	}
    
    
    private LocalDateTime minLocalDateTime(LocalDateTime localDateTime1, LocalDateTime localDateTime2) {
    	if(localDateTime1.isBefore(localDateTime2)) {
    		return localDateTime1;
    	} else {
    		return localDateTime2;
    	}
    }
    
    private LocalDateTime maxLocalDateTime(LocalDateTime localDateTime1, LocalDateTime localDateTime2) {
    	if(localDateTime1.isAfter(localDateTime2)) {
    		return localDateTime1;
    	} else {
    		return localDateTime2;
    	}
    }
    
    private List<TimeSlot> subtractAppointmentsFromTimeSlots(List<TimeSlot> timeSlots, List<Appointment> appointments){
    	
    	for (Appointment appointment : appointments) {
			
    		ListIterator<TimeSlot> timeSlotIterator = timeSlots.listIterator();
    		while(timeSlotIterator.hasNext()) {
    			TimeSlot currentTimeSlot = timeSlotIterator.next();
    			if(isAppointmentWithinTimeSlot(currentTimeSlot,appointment)) {
    				timeSlotIterator.remove();
    				boolean isThereSlotBeforeAppointment = appointment.getStartDate().isAfter(currentTimeSlot.getStartDate());
    				boolean isThereSlotAfterAppointment = appointment.getEndDate().isBefore(currentTimeSlot.getEndDate());
    				TimeSlot cutTimeSlotBefore = TimeSlot
							.builder()
							.startDate(currentTimeSlot.getStartDate())
							.endDate(appointment.getStartDate())
							.practitionerId(currentTimeSlot.getPractitionerId())
							.build();
					
					TimeSlot cutTimeSlotAfter = TimeSlot
							.builder()
							.startDate(appointment.getEndDate())
							.endDate(currentTimeSlot.getEndDate())
							.practitionerId(currentTimeSlot.getPractitionerId())
							.build();
    				
    				if(isThereSlotBeforeAppointment && isThereSlotAfterAppointment) {
    					LinkedList<TimeSlot> chunckedSlots = new LinkedList<>();
    					chunckedSlots.add(cutTimeSlotBefore);
    					chunckedSlots.add(cutTimeSlotAfter);
    					
    					timeSlotIterator.add(cutTimeSlotBefore);
    					timeSlotIterator.add(cutTimeSlotAfter);				
    				} else if(isThereSlotBeforeAppointment) {
    					timeSlotIterator.add(cutTimeSlotBefore);
    				} else if(isThereSlotAfterAppointment) {
    					timeSlotIterator.add(cutTimeSlotAfter);
    				}    				
    			}
    		}
		}
    	return timeSlots;
    }
    
    
    private boolean isAppointmentWithinTimeSlot(TimeSlot timeSlot, Appointment appointment) {
    	if((appointment.getStartDate().isEqual(timeSlot.getStartDate())|| appointment.getStartDate().isAfter(timeSlot.getStartDate()))
    			&&(appointment.getEndDate().isEqual(timeSlot.getEndDate()) || appointment.getEndDate().isBefore(timeSlot.getEndDate())))
    		return true;
    	else {
    		return false;
    	}		
    }
    
    
    
}
