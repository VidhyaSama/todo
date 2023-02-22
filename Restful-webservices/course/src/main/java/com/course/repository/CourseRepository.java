package com.course.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.course.model.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

	Course findByCourseCode(String courseCode);

	void deleteById(Integer id);

	Course findById(Integer id);

}
