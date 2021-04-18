package com.rmv.oop.university.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LecturerEnrollmentResponse {
    private Long studentId;
    private String studentName;
    private Integer mark;
    private String review;
    private Boolean isReviewed;
}