package com.maiia.pro.controller;

import com.maiia.pro.dto.AvailabilityDTO;
import com.maiia.pro.entity.Availability;
import com.maiia.pro.exception.NotImplementedException;
import com.maiia.pro.mapper.ProAvailabilityMapper;
import com.maiia.pro.service.ProAvailabilityService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import javax.validation.constraints.NotNull;

@CrossOrigin
@RestController
@RequestMapping(value = "/availabilities", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProAvailabilityController {
    @Autowired
    private ProAvailabilityService proAvailabilityService;
    
    @Autowired
    private ProAvailabilityMapper proAvailabilityMapper;

    @ApiOperation(value = "Get availabilities by practitionerId")
    @GetMapping
    public ResponseEntity<List<AvailabilityDTO>> getAvailabilities(@RequestParam final @NotNull Integer practitionerId) throws NotImplementedException {
    	List<Availability> availabilities = proAvailabilityService.generateAvailabilities(practitionerId);
        return ResponseEntity.status(HttpStatus.OK).body(proAvailabilityMapper.toAvailabilityDTOList(availabilities));
    }

}
