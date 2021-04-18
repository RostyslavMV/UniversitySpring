package com.rmv.oop.university.repo;

import com.rmv.oop.university.entity.Course;
import com.rmv.oop.university.entity.Enrollment;
import com.rmv.oop.university.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnrollmentsRepo extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findAllByStudent(Student student);

    List<Enrollment> findAllByCourse(Course course);

    Enrollment findByStudentAndCourse(Student student, Course course);
}
