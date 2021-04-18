package com.rmv.oop.university.controller;

import com.rmv.oop.university.dto.request.CourseRequest;
import com.rmv.oop.university.dto.response.StudentCourseListResponse;
import com.rmv.oop.university.entity.Course;
import com.rmv.oop.university.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

@Controller
@RequestMapping("/courses")
public class CoursesController {

  private final CourseService courseService;

  @Autowired
  public CoursesController(CourseService courseService) {
    this.courseService = courseService;
  }

  @RolesAllowed("student")
  @GetMapping
  public ResponseEntity<StudentCourseListResponse> getCourses(@RequestHeader String Authorization) {
    return ResponseEntity.ok(courseService.getCoursesForStudent(Authorization));
  }

  @RolesAllowed("lecturer")
  @PostMapping
  public ResponseEntity<Course> addCourse(@RequestHeader String Authorization, @RequestBody CourseRequest request){
    return  ResponseEntity.ok(courseService.addCourse(Authorization, request.getName()));
  }
}
