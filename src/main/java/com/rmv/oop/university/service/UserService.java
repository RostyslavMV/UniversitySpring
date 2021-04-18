package com.rmv.oop.university.service;

import com.rmv.oop.university.dto.UserDTO;
import com.rmv.oop.university.entity.Lecturer;
import com.rmv.oop.university.entity.Student;
import com.rmv.oop.university.entity.User;
import com.rmv.oop.university.repo.LecturerRepo;
import com.rmv.oop.university.repo.StudentRepo;
import com.rmv.oop.university.repo.UserRepo;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

  private final UserRepo userRepo;
  private final StudentRepo studentRepo;
  private final LecturerRepo lecturerRepo;

  @Autowired
  public UserService(UserRepo userRepo, StudentRepo studentRepo, LecturerRepo lecturerRepo) {
    this.userRepo = userRepo;
    this.studentRepo = studentRepo;
    this.lecturerRepo = lecturerRepo;
  }

  public UserDTO afterLogIn(String Authorization) {
    UserDTO userDTO = getUserDto(Authorization);
    checkIfUserIsSavedToDataBase(userDTO);
    return userDTO;
  }

  public UserDTO getUserDto(String Authorization) {
    UserDTO userDTO = new UserDTO();
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null) {
      if (authentication.getPrincipal() instanceof KeycloakPrincipal) {
        KeycloakPrincipal<KeycloakSecurityContext> kp =
            (KeycloakPrincipal<KeycloakSecurityContext>) authentication.getPrincipal();

        AccessToken accessToken = kp.getKeycloakSecurityContext().getToken();
        Set<String> roles = accessToken.getResourceAccess().get("university-app").getRoles();
        Optional<String> optionalRole = roles.stream().findFirst();
        optionalRole.ifPresent(userDTO::setRole);
        userDTO.setSurname(accessToken.getFamilyName());
        userDTO.setFirstName(accessToken.getGivenName());
        userDTO.setUsername(accessToken.getPreferredUsername());
      }
    }
    return userDTO;
  }

  public void checkIfUserIsSavedToDataBase(UserDTO userDTO) {
    Optional<User> optionalUser = userRepo.findByUserName(userDTO.getUsername());
    if (optionalUser.isPresent()) {
      return;
    }
    User user = new User();
    user.setUserName(userDTO.getUsername());
    if (userDTO.getRole().equals("student")) {
      Student student = new Student();
      student.setSurname(userDTO.getSurname());
      student.setFirstName(userDTO.getFirstName());
      studentRepo.save(student);
      user.setStudent(student);
      userRepo.save(user);
    }
    if (userDTO.getRole().equals("lecturer")) {
      Lecturer lecturer = new Lecturer();
      lecturer.setSurname(userDTO.getSurname());
      lecturer.setFirstName(userDTO.getFirstName());
      lecturerRepo.save(lecturer);
      user.setLecturer(lecturer);
      userRepo.save(user);
    }
  }

  public Student getStudent(String Authorization) {
    UserDTO userDTO = getUserDto(Authorization);
    Optional<User> optionalUser = userRepo.findByUserName(userDTO.getUsername());
    if (optionalUser.isEmpty()) {
      throw new RuntimeException("no such user");
    }
    User user = optionalUser.get();
    return user.getStudent();
  }

  public Lecturer getLecturer(String Authorization) {
    UserDTO userDTO = getUserDto(Authorization);
    Optional<User> optionalUser = userRepo.findByUserName(userDTO.getUsername());
    if (optionalUser.isEmpty()) {
      throw new RuntimeException("no such user");
    }
    User user = optionalUser.get();
    return user.getLecturer();
  }
}
