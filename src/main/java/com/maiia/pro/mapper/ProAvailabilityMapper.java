package com.maiia.pro.mapper;

import java.util.List;

import org.springframework.stereotype.Component;
import com.maiia.pro.dto.AvailabilityDTO;
import com.maiia.pro.entity.Availability;

@Component
public class ProAvailabilityMapper extends ProAbstractMapper {

	public List<AvailabilityDTO> toAvailabilityDTOList(List<Availability> availabilities) {
		List<AvailabilityDTO> availabilitiesDTO = this.mapList(availabilities, AvailabilityDTO.class);	
		return availabilitiesDTO;
	}

}
