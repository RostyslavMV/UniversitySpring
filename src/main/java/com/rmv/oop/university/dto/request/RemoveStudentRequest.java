package com.rmv.oop.university.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RemoveStudentRequest {
    private Long studentId;
    private Long courseId;
}
