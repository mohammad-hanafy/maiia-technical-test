package com.maiia.pro.mapper;

import java.util.List;

import org.springframework.stereotype.Component;
import com.maiia.pro.dto.PractitionerDTO;
import com.maiia.pro.entity.Practitioner;

@Component
public class ProPractitionerMapper extends ProAbstractMapper {

	public List<PractitionerDTO> toPractitionerDTOList(List<Practitioner> practitioners) {
		List<PractitionerDTO> practitionersDTO = this.mapList(practitioners, PractitionerDTO.class);	
		return practitionersDTO;
	}
}
