package com.rmv.oop.university.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseResponse {
    private Long id;
    private String name;
    private String surname;
    private String firstName;

    private Integer mark;
    private String review;
}