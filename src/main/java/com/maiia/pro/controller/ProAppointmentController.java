package com.maiia.pro.controller;

import com.maiia.pro.dto.AppointmentDTO;
import com.maiia.pro.entity.Appointment;
import com.maiia.pro.exception.AppointmentConflictException;
import com.maiia.pro.exception.NoAvailabilityForAppointmentException;
import com.maiia.pro.service.ProAppointmentService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.maiia.pro.mapper.ProAppointmentMapper;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@CrossOrigin
@RestController
@RequestMapping(value = "/appointments", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProAppointmentController {
    @Autowired
    private ProAppointmentService proAppointmentService;
    @Autowired
    private ProAppointmentMapper appointmentMapper;

    @ApiOperation(value = "Get appointments by practitionerId")
    @GetMapping("/{practitionerId}")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByPractitioner(@PathVariable @NotNull final String practitionerId) {
    	List<Appointment> appointments = proAppointmentService.findByPractitionerId(practitionerId);
        return ResponseEntity.status(HttpStatus.OK).body(appointmentMapper.toAppointmentDTOList(appointments));
    }

    @ApiOperation(value = "Get all appointments")
    @GetMapping
    public ResponseEntity<List<AppointmentDTO>> getAppointments() {
    	List<Appointment> appointments = proAppointmentService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(appointmentMapper.toAppointmentDTOList(appointments));
    }
    
    @ApiOperation(value = "Create an Appointment")
    @PostMapping
    public ResponseEntity<AppointmentDTO> addAppointment(@RequestBody @Valid AppointmentDTO appointmentDTO) throws AppointmentConflictException, NoAvailabilityForAppointmentException {
    	Appointment appointment = appointmentMapper.toAppointment(appointmentDTO);
    	Appointment createdAppointment =  proAppointmentService.addAppointment(appointment);
    	return  ResponseEntity.status(HttpStatus.CREATED).body(appointmentMapper.toAppointmentDTO(createdAppointment));
    }
    
    
}
