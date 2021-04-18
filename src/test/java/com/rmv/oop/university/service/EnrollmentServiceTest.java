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
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class EnrollmentServiceTest {

  private static final Long STUDENT_ID = 1L;
  private static final Long LECTURER_ID = 1L;
  private static final Long COURSE_ID = 1L;
  private static final Long ANOTHER_COURSE_ID = 2L;
  private static final Long ENROLLMENT_ID = 1L;
  private static final String SURNAME = "surname";
  private static final String FIRST_NAME = "firstName";
  private static final String COURSE1 = "course1";
  private static final String COURSE2 = "course2";

  @InjectMocks private EnrollmentService enrollmentService;

  @Mock private UserService userService;
  @Mock private EnrollmentsRepo enrollmentsRepo;
  @Mock private CourseRepo courseRepo;
  @Mock private StudentRepo studentRepo;

  @Test
  public void enroll() {
    doReturn(getStudent()).when(userService).getStudent(any());
    doReturn(Optional.of(getCourse())).when(courseRepo).findById(any());
    doReturn(getEnrollment()).when(enrollmentsRepo).save(any());

    Assertions.assertThat(enrollmentService.enroll("", COURSE_ID)).isEqualTo(getEnrollment());
  }

  @Test
  public void getLecturerCoursesResponse() {
    doReturn(getLecturer()).when(userService).getLecturer(any());
    doReturn(getCoursesList()).when(courseRepo).findAllByLecturer(any());
    doReturn(getEnrollmentsList()).when(enrollmentsRepo).findAllByCourse(any());

    LecturerCoursesResponse actualResponse = enrollmentService.getLecturerCoursesResponse("");
    LecturerCoursesResponse expectedResponse = getExpectedLecturerCoursesResponse();

    Assertions.assertThat(actualResponse.getCourseResponses())
        .usingRecursiveFieldByFieldElementComparator()
        .hasSameElementsAs(expectedResponse.getCourseResponses());
  }

  @Test
  public void putMark() {
    doReturn(Optional.of(getCourse())).when(courseRepo).findById(any());
    doReturn(Optional.of(getStudent())).when(studentRepo).findById(any());
    doReturn(getEnrollment()).when(enrollmentsRepo).findByStudentAndCourse(any(), any());

    Enrollment enrollment = getEnrollment();
    enrollment.setMark(100);
    enrollment.setReview("review");

    doReturn(enrollment).when(enrollmentsRepo).save(any());

    MarkEnrollmentRequest request = new MarkEnrollmentRequest();
    request.setMark(100);
    request.setReview("review");
    request.setCourseId(COURSE_ID);
    request.setStudentId(STUDENT_ID);

    Assertions.assertThat(enrollmentService.putMark(request)).isEqualTo(enrollment);
  }

  @Test
  public void unenroll() {
    doReturn(Optional.of(getCourse())).when(courseRepo).findById(any());
    doReturn(getStudent()).when(userService).getStudent(any());
    doNothing().when(enrollmentsRepo).delete(any());
    doReturn(getEnrollment()).when(enrollmentsRepo).findByStudentAndCourse(any(), any());

    Assertions.assertThat(enrollmentService.unenroll("", COURSE_ID)).isEqualTo(getEnrollment());
  }

  @Test
  public void removeStudentEnrollment() {
    doReturn(Optional.of(getCourse())).when(courseRepo).findById(any());
    doReturn(Optional.of(getStudent())).when(studentRepo).findById(any());
    doNothing().when(enrollmentsRepo).delete(any());
    doReturn(getEnrollment()).when(enrollmentsRepo).findByStudentAndCourse(any(), any());

    Assertions.assertThat(enrollmentService.removeStudentEnrollment(STUDENT_ID, COURSE_ID))
        .isEqualTo(getEnrollment());
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

  private Enrollment getEnrollment() {
    Enrollment enrollment = new Enrollment();
    enrollment.setCourse(getCourse());
    enrollment.setStudent(getStudent());
    enrollment.setId(ENROLLMENT_ID);
    return enrollment;
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

  private LecturerCoursesResponse getExpectedLecturerCoursesResponse() {
    LecturerCoursesResponse response = new LecturerCoursesResponse();
    List<LecturerCourseResponse> courseResponses = new ArrayList<>();

    LecturerEnrollmentResponse enrollmentResponse =
        new LecturerEnrollmentResponse(STUDENT_ID, "surname firstName", null, null, null);
    courseResponses.add(
        new LecturerCourseResponse(
            COURSE_ID, COURSE1, Collections.singletonList(enrollmentResponse)));
    LecturerEnrollmentResponse enrollmentResponse2 =
        new LecturerEnrollmentResponse(STUDENT_ID, "surname firstName", null, null, null);
    courseResponses.add(
        new LecturerCourseResponse(
            ANOTHER_COURSE_ID, COURSE2, Collections.singletonList(enrollmentResponse2)));

    response.setCourseResponses(courseResponses);
    return response;
  }
}
