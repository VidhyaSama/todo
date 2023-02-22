package com.course.model;

import java.math.BigDecimal;

import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CourseDetails {

	@Pattern(regexp = "(^$|[0-9]{3})", message = "code should be in 3 digits")
	private String courseCode;
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private String courseDescription;
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private BigDecimal price;
	public String getCourseCode() {
		return courseCode;
	}
	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}
	public String getCourseDescription() {
		return courseDescription;
	}
	public void setCourseDescription(String courseDescription) {
		this.courseDescription = courseDescription;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	@Override
	public String toString() {
		return "courseDetails [courseCode=" + courseCode + ", courseDescription=" + courseDescription + ", price="
				+ price + "]";
	}

}
