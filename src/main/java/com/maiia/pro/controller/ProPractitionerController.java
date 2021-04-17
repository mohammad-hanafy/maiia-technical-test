package com.maiia.pro.controller;

import com.maiia.pro.dto.PractitionerDTO;
import com.maiia.pro.entity.Practitioner;
import com.maiia.pro.mapper.ProPractitionerMapper;
import com.maiia.pro.service.ProPractitionerService;
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
@RequestMapping(value = "/practitioners", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProPractitionerController {
    @Autowired
    private ProPractitionerService proPractitionerService;
    
    @Autowired
    private ProPractitionerMapper proPractitionerMapper;

    @ApiOperation(value = "Get practitioners")
    @GetMapping
    public ResponseEntity<List<PractitionerDTO>> getPractitioners() {
    	List<Practitioner> practitioners = proPractitionerService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(proPractitionerMapper.toPractitionerDTOList(practitioners));
    }
}
