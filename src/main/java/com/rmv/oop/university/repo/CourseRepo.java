package com.rmv.oop.university.repo;

import com.rmv.oop.university.entity.Course;
import com.rmv.oop.university.entity.Lecturer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepo extends JpaRepository<Course, Long> {
    List<Course> findAllByLecturer(Lecturer lecturer);
}
