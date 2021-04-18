package com.rmv.oop.university.service;

import com.rmv.oop.university.dto.UserDTO;
import com.rmv.oop.university.entity.Lecturer;
import com.rmv.oop.university.entity.Student;
import com.rmv.oop.university.entity.User;
import com.rmv.oop.university.repo.LecturerRepo;
import com.rmv.oop.university.repo.StudentRepo;
import com.rmv.oop.university.repo.UserRepo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserServiceTest {

  private static final Long STUDENT_ID = 1L;
  private static final Long LECTURER_ID = 1L;
  private static final String SURNAME = "surname";
  private static final String FIRST_NAME = "firstName";

  @InjectMocks UserService userService;

  @Mock private UserRepo userRepo;
  @Mock private StudentRepo studentRepo;
  @Mock private LecturerRepo lecturerRepo;

  @Test
  public void checkIfUserIsSavedToDataBase_Student() {
    Assertions.assertThatCode(() -> userService.checkIfUserIsSavedToDataBase(getStudentUserDto()))
        .doesNotThrowAnyException();
  }

  @Test
  public void checkIfUserIsSavedToDataBase_Lecturer() {
    Assertions.assertThatCode(() -> userService.checkIfUserIsSavedToDataBase(getLecturerUserDto()))
        .doesNotThrowAnyException();
  }

  @Test
  public void getStudent() {
    doReturn(Optional.of(getUser())).when(userRepo).findByUserName(any());
    Assertions.assertThat(userService.getStudent("")).isEqualTo(getDummyStudent());
  }

  @Test
  public void getLecturer() {
    doReturn(Optional.of(getUser())).when(userRepo).findByUserName(any());
    Assertions.assertThat(userService.getLecturer("")).isEqualTo(getDummyLecturer());
  }

  private UserDTO getStudentUserDto() {
    UserDTO dto = new UserDTO();
    dto.setRole("student");

    return dto;
  }

  private UserDTO getLecturerUserDto() {
    UserDTO dto = new UserDTO();
    dto.setRole("lecturer");

    return dto;
  }

  private Student getDummyStudent() {
    Student student = new Student();
    student.setFirstName(FIRST_NAME);
    student.setSurname(SURNAME);
    student.setId(STUDENT_ID);
    return student;
  }

  private Lecturer getDummyLecturer() {
    Lecturer lecturer = new Lecturer();
    lecturer.setFirstName(FIRST_NAME);
    lecturer.setSurname(SURNAME);
    lecturer.setId(LECTURER_ID);
    return lecturer;
  }

  private User getUser() {
    User user = new User();
    user.setStudent(getDummyStudent());
    user.setLecturer(getDummyLecturer());
    return user;
  }
}
