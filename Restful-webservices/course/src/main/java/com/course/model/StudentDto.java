package com.course.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonFilter("DynamicFilter")
public class StudentDto {
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Integer id;

	@NotBlank(message="Name cann0t be blank")
	private String name;

	@Pattern(regexp = "^[0-9]{2}[a-z]{2}[0-9]{2}$", message = "Roll number should be alphanumeric with length 6 eg:12jj34")
	private String rollNumber;

	private CourseDetails course;

	public StudentDto(Integer id, String name, String rollNumber, CourseDetails course) {
		super();
		this.id = id;
		this.name = name;
		this.rollNumber = rollNumber;
		this.course = course;
	}

	public CourseDetails getCourse() {
		return course;
	}
	
	public void setCourse(CourseDetails course) {
		this.course = course;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRollNumber() {
		return rollNumber;
	}

	public void setRollNumber(String rollNumber) {
		this.rollNumber = rollNumber;
	}

	@Override
	public String toString() {
		return "StudentDto [id=" + id + ", name=" + name + ", rollNumber=" + rollNumber + ", course=" + course + "]";
	}

	public StudentDto() {
		// TODO Auto-generated constructor stub
	}

}
