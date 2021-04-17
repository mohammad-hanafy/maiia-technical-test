package com.maiia.pro.exception;


public class AppointmentConflictException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public AppointmentConflictException() {
		super("Appointment Conflicts with other appointments");
	}

}
