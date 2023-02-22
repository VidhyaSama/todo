package com.course.model;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonFilter("DynamicFilter")
public class CourseDto {
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Integer id;

	@Pattern(regexp = "(^$|[0-9]{3})", message = "code should be in 3 digits")
	private String courseCode;

	@Size(max = 16, min = 3, message = "courseDescription should contain atleast 3 and atmost 16 in length")
	private String courseDescription;

	private BigDecimal price;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private List<StudentDto> students;

	public List<StudentDto> getStudents() {
		return students;
	}

	public void setStudents(List<StudentDto> students) {
		this.students = students;
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCourseCode() {
		return courseCode;
	}

	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	@Override
	public String toString() {
		return "CourseDto [id=" + id + ", courseCode=" + courseCode + ", courseDescription=" + courseDescription
				+ ", price=" + price + "]";
	}

	public CourseDto(Integer id, String courseCode, String courseDescription, BigDecimal price,
			List<StudentDto> students) {
		super();
		this.id = id;
		this.courseCode = courseCode;
		this.courseDescription = courseDescription;
		this.price = price;
		this.students = students;
	}

	public CourseDto() {
		// TODO Auto-generated constructor stub
	}

}
