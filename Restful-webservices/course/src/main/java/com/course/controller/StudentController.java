package com.course.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.course.exception.StudentNotFoundException;
import com.course.model.Course;
import com.course.model.StudentDto;
import com.course.service.StudentService;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

@RestController
@RequestMapping("/student")
public class StudentController {
	@Autowired
	private StudentService studentService;

	@GetMapping("/{id}")
	public MappingJacksonValue getStudentDetails(@PathVariable Integer id) {
		StudentDto student = studentService.getStudentCourseDetails(id);
		if (student == null)
			throw new StudentNotFoundException("Student not Found");
		SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("name", "rollNumber", "course");
		return filterCourse(filter, student);
	}

	@GetMapping()
	public MappingJacksonValue getStudentDetails() {
		List<StudentDto> students= studentService.getStudentsCourseDetails();
		SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("name", "rollNumber", "course");
		return filterCourse(filter, students);
	}

	@PostMapping()
	public ResponseEntity<Course> saveStudent(@Valid @RequestBody StudentDto studentDto) {
		StudentDto student = studentService.saveDetails(studentDto);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(student.getId()).toUri();
		return ResponseEntity.created(location).build();
	}

	@PutMapping("/{id}")
	public MappingJacksonValue updateStudentDetails(@Valid @RequestBody StudentDto studentDto,
			@PathVariable Integer id) {
		StudentDto student = studentService.updateStudent(studentDto, id);
		if (student == null)
			throw new StudentNotFoundException("Student not Found");
		SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("name", "rollNumber", "course");
		return filterCourse(filter, student);

	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deleteById(@PathVariable Integer id) {
		StudentDto student = studentService.deleteStudent(id);
		if (student == null)
			throw new StudentNotFoundException("Student not Found");
		return ResponseEntity.noContent().build();
	}

	public static MappingJacksonValue filterCourse(SimpleBeanPropertyFilter filter, Object object) {
		FilterProvider filterProvider = new SimpleFilterProvider().addFilter("DynamicFilter", filter);
		MappingJacksonValue mapping = new MappingJacksonValue(object);
		mapping.setFilters(filterProvider);
		return mapping;
	}
}
