package com.course.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.course.exception.CourseAvailableException;
import com.course.exception.CourseNotFoundException;
import com.course.exception.StudentNotFoundException;
import com.course.model.Course;
import com.course.model.CourseDto;
import com.course.model.Student;
import com.course.model.StudentDto;
import com.course.repository.CourseRepository;
import com.course.repository.StudentRepository;

@Service
public class CourseService {
	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private StudentRepository studentRepository;

	Course course_one = new Course();

	CourseDto courseDto_one = new CourseDto();

	public CourseDto saveDetails(CourseDto courseDto) {
		validateCourse(courseDto.getCourseCode());
		BeanUtils.copyProperties(courseDto, course_one);
		Course course_new = courseRepository.save(course_one);
		BeanUtils.copyProperties(course_new, courseDto);
		return courseDto;

	}

	public CourseDto updateCourse(CourseDto courseDto, Integer id) {
		Course course = courseRepository.findById(id);
		if (course == null)
			return null;
		if (courseDto.getCourseCode() != null) {
			validateCourse(courseDto.getCourseCode());
		}
		course = validateProperties(courseDto, course);
		BeanUtils.copyProperties(course, courseDto_one);
		courseRepository.save(course);
		return courseDto_one;
	}

	public void validateCourse(String courseCode) {
		Course course = courseRepository.findByCourseCode(courseCode);
		if (course != null)
			throw new CourseAvailableException("Course with the " + courseCode + " already available");
	}

	public Course validateProperties(CourseDto courseDto, Course course) {
		if (courseDto.getCourseCode() != null)
			course.setCourseCode(courseDto.getCourseCode());
		if (courseDto.getCourseDescription() != null)
			course.setCourseDescription(courseDto.getCourseDescription());
		if (courseDto.getPrice() != null)
			course.setPrice(courseDto.getPrice());

		return course;
	}

	public List<CourseDto> getAllcourses() {
		List<Course> courses = courseRepository.findAll();
		if (courses.size() == 0)
			throw new CourseNotFoundException("Courses are not available");
		List<CourseDto> courseList = courses.stream().map(course -> {
			CourseDto courseDto = new CourseDto();
			BeanUtils.copyProperties(course, courseDto);
			return courseDto;
		}).collect(Collectors.toList());

		return courseList;
	}

	@Transactional
	public CourseDto deleteCourse(Integer id) {
		Course course = courseRepository.findById(id);
		if (course == null)
			return null;
		if (course.getStudents().size() != 0) {
			for (Student student : course.getStudents()) {
				studentRepository.deleteById(student.getId());
			}
		}
		BeanUtils.copyProperties(course, courseDto_one);
		courseRepository.deleteById(course.getId());
		return courseDto_one;
	}

	public CourseDto getStudentDetails(Integer id) {
		Course course = courseRepository.findById(id);
		if (course == null)
			throw new CourseNotFoundException("Course Not Found");
		if(course.getStudents().size()==0)
			throw new StudentNotFoundException("Yet no student registered with this course");
		BeanUtils.copyProperties(course, courseDto_one);		
		List<StudentDto> students= course.getStudents().stream().map(student -> {
			StudentDto studentDto = new StudentDto();
			studentDto.setId(student.getId());
			studentDto.setName(student.getName());
			studentDto.setRollNumber(student.getRollNumber());
			return studentDto;
			
		}).collect(Collectors.toList());
		courseDto_one.setStudents(students);
		return courseDto_one;

	}

}
