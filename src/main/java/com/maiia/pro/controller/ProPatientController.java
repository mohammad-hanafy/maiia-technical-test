package com.maiia.pro.controller;

import com.maiia.pro.dto.PatientDTO;
import com.maiia.pro.entity.Patient;
import com.maiia.pro.mapper.ProPatientMapper;
import com.maiia.pro.service.ProPatientService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/patients", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProPatientController {
    @Autowired
    private ProPatientService proPatientService;
    
    @Autowired
    private ProPatientMapper proPatientMapper;

    @ApiOperation(value = "Get patients")
    @GetMapping
    public ResponseEntity<List<PatientDTO>> getPatients() {
    	List<Patient> patients = proPatientService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(proPatientMapper.toPatientDTOList(patients));
    }
}
