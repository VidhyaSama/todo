package com.course.exception;

@SuppressWarnings("serial")
public class StudentAvailedException extends RuntimeException {
	public StudentAvailedException(String message) {
		super(message);
		
	}
}
