package com.rmv.oop.university.service;

import com.rmv.oop.university.dto.response.CourseResponse;
import com.rmv.oop.university.dto.response.StudentCourseListResponse;
import com.rmv.oop.university.entity.Course;
import com.rmv.oop.university.entity.Enrollment;
import com.rmv.oop.university.entity.Lecturer;
import com.rmv.oop.university.entity.Student;
import com.rmv.oop.university.repo.CourseRepo;
import com.rmv.oop.university.repo.EnrollmentsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CourseService {

  private final UserService userService;
  private final CourseRepo courseRepo;
  private final EnrollmentsRepo enrollmentsRepo;

  @Autowired
  public CourseService(
      UserService userService, CourseRepo courseRepo, EnrollmentsRepo enrollmentsRepo) {
    this.userService = userService;
    this.courseRepo = courseRepo;
    this.enrollmentsRepo = enrollmentsRepo;
  }

  public StudentCourseListResponse getCoursesForStudent(String Authorization) {
    StudentCourseListResponse response = new StudentCourseListResponse();
    Student student = userService.getStudent(Authorization);
    response.setAvailableCourses(getAvailableCoursesResponsesForStudent(student));
    response.setEnrolledCourses(getEnrolledCoursesResponsesForStudent(student));

    return response;
  }

  public Course addCourse(String Authorization, String name) {
    Lecturer lecturer = userService.getLecturer(Authorization);
    Course course = new Course();
    course.setName(name);
    course.setLecturer(lecturer);
    return courseRepo.save(course);
  }

  private List<CourseResponse> getAvailableCoursesResponsesForStudent(Student student) {
    List<CourseResponse> courseResponses = new ArrayList<>();
    List<Course> courses = courseRepo.findAll();
    List<Enrollment> enrollments = enrollmentsRepo.findAllByStudent(student);
    Set<Long> allCoursesIds = new HashSet<>();
    for (Course course : courses) {
      allCoursesIds.add(course.getId());
    }
    Set<Long> enrolledCoursesIds = new HashSet<>();
    for (Enrollment enrollment : enrollments) {
      enrolledCoursesIds.add(enrollment.getCourse().getId());
    }

    allCoursesIds.removeAll(enrolledCoursesIds);
    for (Course course : courses) {
      if (allCoursesIds.contains(course.getId())) {
        courseResponses.add(getResponseFromCourse(course));
      }
    }
    return courseResponses;
  }

  private List<CourseResponse> getEnrolledCoursesResponsesForStudent(Student student) {
    List<CourseResponse> courseResponses = new ArrayList<>();
    List<Enrollment> enrollments = enrollmentsRepo.findAllByStudent(student);
    for (Enrollment enrollment : enrollments) {
      Course course;
      Optional<Course> optionalCourse = courseRepo.findById(enrollment.getCourse().getId());
      if (optionalCourse.isPresent()) {
        course = optionalCourse.get();
      } else throw new RuntimeException("e");
      courseResponses.add(getResponseFromCourseAndEnrollment(course, enrollment));
    }

    return courseResponses;
  }

  public CourseResponse getResponseFromCourse(Course course) {
    CourseResponse courseResponse = new CourseResponse();
    courseResponse.setId(course.getId());
    courseResponse.setName(course.getName());
    courseResponse.setFirstName(course.getLecturer().getFirstName());
    courseResponse.setSurname(course.getLecturer().getSurname());
    return courseResponse;
  }

  public CourseResponse getResponseFromCourseAndEnrollment(Course course, Enrollment enrollment) {
    CourseResponse courseResponse = getResponseFromCourse(course);
    courseResponse.setMark(enrollment.getMark());
    courseResponse.setReview(enrollment.getReview());
    return courseResponse;
  }
}
