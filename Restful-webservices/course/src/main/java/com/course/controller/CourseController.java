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

import com.course.exception.CourseNotFoundException;
import com.course.model.Course;
import com.course.model.CourseDto;
import com.course.service.CourseService;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

@RestController
@RequestMapping("/course")
public class CourseController {
	@Autowired
	private CourseService courseService;

	@GetMapping()
	public MappingJacksonValue getcourses() {
		List<CourseDto> courses = courseService.getAllcourses();
		SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("courseCode", "courseDescription",
				"price");
		return filterCourse(filter, courses);
	}

	@GetMapping("/{id}")
	public MappingJacksonValue getStudents(@PathVariable Integer id) {
		CourseDto students = courseService.getStudentDetails(id);
		SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.serializeAllExcept("id","students");
		return filterCourse(filter, students);
	}

	@PostMapping()
	public ResponseEntity<Course> saveCourse(@Valid @RequestBody CourseDto courseDto) {
		CourseDto course = courseService.saveDetails(courseDto);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(course.getId()).toUri();
		return ResponseEntity.created(location).build();
	}

	@PutMapping("/{id}")
	public MappingJacksonValue updateCourseDetails(@Valid@RequestBody CourseDto courseDto, @PathVariable Integer id) {
		CourseDto course = courseService.updateCourse(courseDto, id);
		if (course == null) {
			throw new CourseNotFoundException("Course Not Found");
		}
		SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.serializeAllExcept("id","students");
		return filterCourse(filter, course);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deleteById(@PathVariable Integer id) {
		CourseDto course = courseService.deleteCourse(id);
		if (course == null) {
			throw new CourseNotFoundException("Course Not Found");
		}
		return ResponseEntity.noContent().build();
	}

	public static MappingJacksonValue filterCourse(SimpleBeanPropertyFilter filter, Object object) {
		FilterProvider filterProvider = new SimpleFilterProvider().addFilter("DynamicFilter", filter);
		MappingJacksonValue mapping = new MappingJacksonValue(object);
		mapping.setFilters(filterProvider);

		return mapping;
	}
}
