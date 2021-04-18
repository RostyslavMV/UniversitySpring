package com.rmv.oop.university.controller;

import com.rmv.oop.university.dto.request.EnrollmentRequest;
import com.rmv.oop.university.dto.request.MarkEnrollmentRequest;
import com.rmv.oop.university.dto.request.RemoveStudentRequest;
import com.rmv.oop.university.dto.request.UnenrollmentRequest;
import com.rmv.oop.university.dto.response.LecturerCoursesResponse;
import com.rmv.oop.university.entity.Enrollment;
import com.rmv.oop.university.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

@Controller
@RequestMapping("/enrollment")
public class EnrollmentController {

  private final EnrollmentService enrollmentService;

  @Autowired
  public EnrollmentController(EnrollmentService enrollmentService) {
    this.enrollmentService = enrollmentService;
  }

  @RolesAllowed("student")
  @PostMapping
  public ResponseEntity<Enrollment> enroll(
      @RequestHeader String Authorization, @RequestBody EnrollmentRequest request) {
    return ResponseEntity.ok(enrollmentService.enroll(Authorization, request.getCourseId()));
  }

  @RolesAllowed("lecturer")
  @GetMapping
  public ResponseEntity<LecturerCoursesResponse> getLecturerCourses(
      @RequestHeader String Authorization) {
    return ResponseEntity.ok(enrollmentService.getLecturerCoursesResponse(Authorization));
  }

  @RolesAllowed("lecturer")
  @PostMapping("/mark")
  public ResponseEntity<Enrollment> putMark(
      @RequestHeader String Authorization,
      @RequestBody MarkEnrollmentRequest markEnrollmentRequest) {
    return ResponseEntity.ok(enrollmentService.putMark(markEnrollmentRequest));
  }

  @RolesAllowed("student")
  @PostMapping("/unenroll")
  public ResponseEntity<Enrollment> unenroll(
      @RequestHeader String Authorization, @RequestBody UnenrollmentRequest unenrollmentRequest) {
    return ResponseEntity.ok(
        enrollmentService.unenroll(Authorization, unenrollmentRequest.getCourseId()));
  }

  @RolesAllowed("lecturer")
  @PostMapping("/lecturer/remove")
  public ResponseEntity<Enrollment> removeStudent(
      @RequestHeader String Authorization, @RequestBody RemoveStudentRequest removeStudentRequest) {
    return ResponseEntity.ok(
        enrollmentService.removeStudentEnrollment(
            removeStudentRequest.getStudentId(), removeStudentRequest.getCourseId()));
  }
}
