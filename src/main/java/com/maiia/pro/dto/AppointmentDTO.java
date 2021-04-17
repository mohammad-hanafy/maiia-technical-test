package com.maiia.pro.dto;

import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class AppointmentDTO {
	
	private Integer id;
	@NotNull
    private Integer patientId;
	@NotNull
    private Integer practitionerId;
	@NotNull
    private LocalDateTime startDate;
	@NotNull
    private LocalDateTime endDate;

}
