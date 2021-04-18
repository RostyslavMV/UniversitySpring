package com.rmv.oop.university.repo;

import com.rmv.oop.university.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepo extends JpaRepository<Student, Long> {
}
