package com.maiia.pro.dto;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class TimeSlotDTO {
	private Integer id;
	private Integer practitionerId;
	private LocalDateTime startDate;
	private LocalDateTime endDate;

}
