package com.maiia.pro.dto;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class PatientDTO {
	
	private Integer id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;

}
