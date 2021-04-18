package com.rmv.oop.university.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MarkEnrollmentRequest {
    private Long studentId;
    private Long courseId;
    private Integer mark;
    private String review;
}
