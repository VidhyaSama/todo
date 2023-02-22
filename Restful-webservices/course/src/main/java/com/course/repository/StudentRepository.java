package com.course.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.course.model.Student;


@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

	Student findByRollNumber(String rollNumber);

	void deleteById(Integer id);

}
