package com.maiia.pro.mapper;

import java.util.List;

import org.springframework.stereotype.Component;
import com.maiia.pro.dto.PatientDTO;
import com.maiia.pro.entity.Patient;

@Component
public class ProPatientMapper extends ProAbstractMapper {

	public List<PatientDTO> toPatientDTOList(List<Patient> patients) {
		List<PatientDTO> patientsDTO = this.mapList(patients, PatientDTO.class);	
		return patientsDTO;
	}

}
