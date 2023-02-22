package com.course.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.course.exception.CourseNotFoundException;
import com.course.exception.StudentAvailedException;
import com.course.exception.StudentNotFoundException;
import com.course.model.Course;
import com.course.model.CourseDetails;

import com.course.model.Student;
import com.course.model.StudentDto;
import com.course.repository.CourseRepository;
import com.course.repository.StudentRepository;

@Service
public class StudentService {
	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private StudentRepository studentRepository;

	private Student student_one = new Student();

	private StudentDto studentDto_one = new StudentDto();

	CourseDetails course = new CourseDetails();

	public StudentDto saveDetails(@Valid StudentDto studentDto) {
		Course course = validateCourse(studentDto.getCourse().getCourseCode());
		validateRollNumber(studentDto.getRollNumber());
		BeanUtils.copyProperties(studentDto, student_one);
		student_one.setCourse(course);
		student_one = studentRepository.save(student_one);
		BeanUtils.copyProperties(student_one, studentDto_one);
		return studentDto_one;

	}

	public StudentDto getStudentCourseDetails(Integer id) {
		Optional<Student> student = studentRepository.findById(id);
		if (!student.isPresent())
			return null;
		BeanUtils.copyProperties(student.get(), studentDto_one);
		BeanUtils.copyProperties(student.get().getCourse(), course);
		studentDto_one.setCourse(course);
		System.out.println(studentDto_one);
		return studentDto_one;
	}

	public StudentDto deleteStudent(Integer id) {
		Optional<Student> student = studentRepository.findById(id);
		if (!student.isPresent())
			return null;
		BeanUtils.copyProperties(student.get(), studentDto_one);
		BeanUtils.copyProperties(student.get().getCourse(), course);
		studentDto_one.setCourse(course);
		studentRepository.deleteById(student.get().getId());
		return studentDto_one;
	}

	public StudentDto updateStudent(StudentDto studentDto, Integer id) {
		Optional<Student> student = studentRepository.findById(id);
		if (student == null)
			return null;
		if (studentDto.getRollNumber() != null) {
			validateRollNumber(studentDto.getRollNumber());
		}
		Course course1 = new Course();
		if (studentDto.getCourse() != null
				&& studentDto.getCourse().getCourseCode() != student.get().getCourse().getCourseCode()) {
			course1 = validateCourse(studentDto.getCourse().getCourseCode());
			student.get().setCourse(course1);
		}
		;
		BeanUtils.copyProperties(student.get(), student_one);
		student_one = validateProperties(studentDto, student_one);
		student_one = studentRepository.save(student_one);
		BeanUtils.copyProperties(student_one, studentDto_one);
		BeanUtils.copyProperties(course1, course);
		studentDto_one.setCourse(course);
		return studentDto_one;
	}

	public Student validateProperties(StudentDto studentDto, Student student) {
		if (studentDto.getName() != null)
			student.setName(studentDto.getName());
		if (studentDto.getRollNumber() != null)
			student.setRollNumber(studentDto.getRollNumber());
		return student;
	}

	public void validateRollNumber(String rollNumber) {
		Student studentDetails = studentRepository.findByRollNumber(rollNumber);
		if (studentDetails != null)
			throw new StudentAvailedException(rollNumber + " Already registered with course ");
	}

	public Course validateCourse(String courseCode) {
		Course course = courseRepository.findByCourseCode(courseCode);
		if (course == null)
			throw new CourseNotFoundException("Course with the " + courseCode + "not found");
		return course;
	}

	public List<StudentDto> getStudentsCourseDetails() {
		List<Student> students = studentRepository.findAll();
		if (students.size() == 0)
			throw new StudentNotFoundException("Students not available");
		List<StudentDto> studentsDetails = students.stream().map(stu -> {
			StudentDto student = new StudentDto();
			CourseDetails course = new CourseDetails();
			BeanUtils.copyProperties(stu, student);
			BeanUtils.copyProperties(stu.getCourse(), course);
			student.setCourse(course);
			return student;
		}).collect(Collectors.toList());
		return studentsDetails;
	}

}
