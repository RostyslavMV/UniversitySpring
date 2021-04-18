package com.rmv.oop.university.service;

import com.rmv.oop.university.dto.response.CourseResponse;
import com.rmv.oop.university.dto.response.StudentCourseListResponse;
import com.rmv.oop.university.entity.Course;
import com.rmv.oop.university.entity.Enrollment;
import com.rmv.oop.university.entity.Lecturer;
import com.rmv.oop.university.entity.Student;
import com.rmv.oop.university.repo.CourseRepo;
import com.rmv.oop.university.repo.EnrollmentsRepo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CourseServiceTest {

  private static final Long STUDENT_ID = 1L;
  private static final Long LECTURER_ID = 1L;
  private static final Long COURSE_ID = 1L;
  private static final Long ANOTHER_COURSE_ID = 2L;
  private static final Long ENROLLMENT_ID = 1L;
  private static final String SURNAME = "surname";
  private static final String FIRST_NAME = "firstName";
  private static final String COURSE1 = "course1";
  private static final String COURSE2 = "course2";

  @InjectMocks private CourseService courseService;

  @Mock private UserService userService;

  @Mock private CourseRepo courseRepo;

  @Mock private EnrollmentsRepo enrollmentsRepo;

  @Test
  public void getCoursesForStudent() {
    doReturn(getStudent()).when(userService).getStudent(any());
    doReturn(getCoursesList()).when(courseRepo).findAll();
    doReturn(getEnrollmentsList()).when(enrollmentsRepo).findAllByStudent(any());
    doReturn(Optional.of(getCourse())).when(courseRepo).findById(any());

    StudentCourseListResponse actualResponse = courseService.getCoursesForStudent("");
    StudentCourseListResponse expectedResponse = new StudentCourseListResponse();
    CourseResponse enrolledCourseResponse = new CourseResponse();
    enrolledCourseResponse.setId(COURSE_ID);
    enrolledCourseResponse.setName(COURSE1);
    enrolledCourseResponse.setSurname(SURNAME);
    enrolledCourseResponse.setFirstName(FIRST_NAME);
    expectedResponse.setEnrolledCourses(Collections.singletonList(enrolledCourseResponse));
    CourseResponse availableCourseResponse = new CourseResponse();
    availableCourseResponse.setId(ANOTHER_COURSE_ID);
    availableCourseResponse.setName(COURSE2);
    availableCourseResponse.setSurname(SURNAME);
    availableCourseResponse.setFirstName(FIRST_NAME);
    expectedResponse.setAvailableCourses(Collections.singletonList(availableCourseResponse));
    Assertions.assertThat(actualResponse.getAvailableCourses())
        .usingFieldByFieldElementComparator()
        .hasSameElementsAs(expectedResponse.getAvailableCourses());
    Assertions.assertThat(actualResponse.getEnrolledCourses())
        .usingFieldByFieldElementComparator()
        .hasSameElementsAs(expectedResponse.getEnrolledCourses());
  }

  @Test
  public void addCourse() {
    doReturn(getCourse()).when(courseRepo).save(any());
    doReturn(getLecturer()).when(userService).getLecturer(any());
    Assertions.assertThat(courseService.addCourse("", COURSE1)).isEqualTo(getCourse());
  }

  private Student getStudent() {
    Student student = new Student();
    student.setFirstName(FIRST_NAME);
    student.setSurname(SURNAME);
    student.setId(STUDENT_ID);
    return student;
  }

  private Lecturer getLecturer() {
    Lecturer lecturer = new Lecturer();
    lecturer.setFirstName(FIRST_NAME);
    lecturer.setSurname(SURNAME);
    lecturer.setId(LECTURER_ID);
    return lecturer;
  }

  private Course getCourse() {
    Course course = new Course();
    course.setId(COURSE_ID);
    course.setName(COURSE1);
    course.setLecturer(getLecturer());
    return course;
  }

  private List<Course> getCoursesList() {
    Course course1 = getCourse();
    Course course2 = new Course();
    course2.setId(ANOTHER_COURSE_ID);
    course2.setName(COURSE2);
    course2.setLecturer(getLecturer());
    return Arrays.asList(course1, course2);
  }

  private List<Enrollment> getEnrollmentsList() {
    Enrollment enrollment = new Enrollment();
    enrollment.setId(ENROLLMENT_ID);
    enrollment.setStudent(getStudent());
    enrollment.setCourse(getCourse());
    return Collections.singletonList(enrollment);
  }
}
