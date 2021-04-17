package com.maiia.pro.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class PractitionerDTO {
	
	private Integer id;
    private String firstName;
    private String lastName;
    private String speciality;

}
