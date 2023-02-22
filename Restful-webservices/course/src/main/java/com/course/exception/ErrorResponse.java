package com.course.exception;

import java.util.Date;
import java.util.List;

public class ErrorResponse {
	//Customizing exception to a specific structure
	private Date timeStamp;
	private String message;
	private List<String> details;
	public ErrorResponse(Date timeStamp, String message, List<String> details) {
		super();
		this.timeStamp = timeStamp;
		this.message = message;
		this.details = details;
	}
	public Date getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public List<String> getDetails() {
		return details;
	}
	public void setDetails(List<String> details) {
		this.details = details;
	}
	
}
