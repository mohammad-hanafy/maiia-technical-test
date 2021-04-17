package com.maiia.pro.mapper;

import java.util.List;
import org.springframework.stereotype.Component;
import com.maiia.pro.dto.AppointmentDTO;
import com.maiia.pro.entity.Appointment;


@Component
public class ProAppointmentMapper extends ProAbstractMapper {


    public AppointmentDTO toAppointmentDTO(Appointment appointment) {
		AppointmentDTO appointmentDTO = this.map(appointment, AppointmentDTO.class);	
		return appointmentDTO;
	}
	
	public Appointment toAppointment(AppointmentDTO appointmentDTO) {
		Appointment appointment = this.map(appointmentDTO, Appointment.class);	
		return appointment;
	}
	
	public List<Appointment> toAppointmentList(List<AppointmentDTO> appointmentsDTO) {
		List<Appointment> appointments = this.mapList(appointmentsDTO, Appointment.class);	
		return appointments;
	}
	
	public List<AppointmentDTO> toAppointmentDTOList(List<Appointment> appointments) {
		List<AppointmentDTO> appointmentsDTO = this.mapList(appointments, AppointmentDTO.class);	
		return appointmentsDTO;
	}
    
	
}
