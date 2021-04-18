package com.rmv.oop.university.repo;

import com.rmv.oop.university.entity.Lecturer;
import com.rmv.oop.university.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LecturerRepo extends JpaRepository<Lecturer, Long> {
}
