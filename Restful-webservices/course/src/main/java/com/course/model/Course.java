package com.course.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Course {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String courseCode;
	
	private String courseDescription;
	
	private BigDecimal price;
	
	@OneToMany(cascade = CascadeType.ALL,mappedBy = "course",fetch =FetchType.LAZY )
	private List<Student> students = new ArrayList<Student>();

	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> student) {
		this.students = student;
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

	

	public Course(Integer id, String courseCode, String courseDescription, BigDecimal price, List<Student> students) {
		super();
		this.id = id;
		this.courseCode = courseCode;
		this.courseDescription = courseDescription;
		this.price = price;
		this.students = students;
	}

	@Override
	public String toString() {
		return "Course [id=" + id + ", courseCode=" + courseCode + ", courseDescription=" + courseDescription
				+ ", price=" + price + "]";
	}

	public Course() {
		// TODO Auto-generated constructor stub
	}

}
