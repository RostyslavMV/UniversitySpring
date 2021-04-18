package com.rmv.oop.university.service;

import com.rmv.oop.university.dto.request.MarkEnrollmentRequest;
import com.rmv.oop.university.dto.response.LecturerCourseResponse;
import com.rmv.oop.university.dto.response.LecturerCoursesResponse;
import com.rmv.oop.university.dto.response.LecturerEnrollmentResponse;
import com.rmv.oop.university.entity.Course;
import com.rmv.oop.university.entity.Enrollment;
import com.rmv.oop.university.entity.Lecturer;
import com.rmv.oop.university.entity.Student;
import com.rmv.oop.university.repo.CourseRepo;
import com.rmv.oop.university.repo.EnrollmentsRepo;
import com.rmv.oop.university.repo.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EnrollmentService {

  private final UserService userService;
  private final EnrollmentsRepo enrollmentsRepo;
  private final CourseRepo courseRepo;
  private final StudentRepo studentRepo;

  @Autowired
  public EnrollmentService(
      UserService userService,
      EnrollmentsRepo enrollmentsRepo,
      CourseRepo courseRepo,
      StudentRepo studentRepo) {
    this.userService = userService;
    this.enrollmentsRepo = enrollmentsRepo;
    this.courseRepo = courseRepo;
    this.studentRepo = studentRepo;
  }

  public Enrollment enroll(String Authorization, Long courseId) {
    Student student = userService.getStudent(Authorization);
    Optional<Course> optionalCourse = courseRepo.findById(courseId);
    Enrollment enrollment = new Enrollment();
    optionalCourse.ifPresent(enrollment::setCourse);
    enrollment.setStudent(student);

    return enrollmentsRepo.save(enrollment);
  }

  public LecturerCoursesResponse getLecturerCoursesResponse(String Authorization) {
    Lecturer lecturer = userService.getLecturer(Authorization);
    LecturerCoursesResponse response = new LecturerCoursesResponse();
    List<Course> courses = courseRepo.findAllByLecturer(lecturer);
    List<LecturerCourseResponse> lecturerCourseResponses = new ArrayList<>();

    for (Course course : courses) {
      LecturerCourseResponse lecturerCourseResponse = new LecturerCourseResponse();
      lecturerCourseResponse.setLecturerEnrollmentResponses(new ArrayList<>());
      lecturerCourseResponse.setCourseName(course.getName());
      lecturerCourseResponse.setCourseId(course.getId());
      List<Enrollment> enrollments = enrollmentsRepo.findAllByCourse(course);
      for (Enrollment enrollment : enrollments) {
        LecturerEnrollmentResponse lecturerEnrollmentResponse = new LecturerEnrollmentResponse();
        lecturerEnrollmentResponse.setMark(enrollment.getMark());
        if (lecturerEnrollmentResponse.getMark() != null) {
          lecturerEnrollmentResponse.setIsReviewed(true);
        }
        lecturerEnrollmentResponse.setReview(enrollment.getReview());
        lecturerEnrollmentResponse.setStudentId(enrollment.getStudent().getId());
        lecturerEnrollmentResponse.setStudentName(
            enrollment.getStudent().getSurname() + " " + enrollment.getStudent().getFirstName());
        lecturerCourseResponse.getLecturerEnrollmentResponses().add(lecturerEnrollmentResponse);
      }
      lecturerCourseResponses.add(lecturerCourseResponse);
    }
    response.setCourseResponses(lecturerCourseResponses);

    return response;
  }

  public Enrollment putMark(MarkEnrollmentRequest markEnrollmentRequest) {
    Optional<Course> optionalCourse = courseRepo.findById(markEnrollmentRequest.getCourseId());
    Optional<Student> optionalStudent = studentRepo.findById(markEnrollmentRequest.getStudentId());

    if (optionalCourse.isEmpty() || optionalStudent.isEmpty()) {
      throw new RuntimeException("No such course / student exists");
    }
    Enrollment enrollment =
        enrollmentsRepo.findByStudentAndCourse(optionalStudent.get(), optionalCourse.get());
    enrollment.setMark(markEnrollmentRequest.getMark());
    enrollment.setReview(markEnrollmentRequest.getReview());
    return enrollmentsRepo.save(enrollment);
  }

  public Enrollment unenroll(String Authorization, Long courseId) {
    Optional<Course> optionalCourse = courseRepo.findById(courseId);
    Student student = userService.getStudent(Authorization);

    if (optionalCourse.isEmpty()) {
      throw new RuntimeException("No such course exists");
    }
    Enrollment enrollment = enrollmentsRepo.findByStudentAndCourse(student, optionalCourse.get());
    enrollmentsRepo.delete(enrollment);

    return enrollment;
  }

  public Enrollment removeStudentEnrollment(Long studentId, Long courseId) {
    Optional<Course> optionalCourse = courseRepo.findById(courseId);
    Optional<Student> optionalStudent = studentRepo.findById(studentId);

    if (optionalCourse.isEmpty() || optionalStudent.isEmpty()) {
      throw new RuntimeException("No such course / student exists");
    }
    Enrollment enrollment =
        enrollmentsRepo.findByStudentAndCourse(optionalStudent.get(), optionalCourse.get());
    enrollmentsRepo.delete(enrollment);

    return enrollment;
  }
}
