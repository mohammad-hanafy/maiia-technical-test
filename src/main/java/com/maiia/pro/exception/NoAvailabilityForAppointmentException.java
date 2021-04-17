package com.maiia.pro.exception;

public class NoAvailabilityForAppointmentException extends Exception {
	private static final long serialVersionUID = 1L;

	public NoAvailabilityForAppointmentException() {
		super("No Availabilty Exists for Appointment");
	}

}
