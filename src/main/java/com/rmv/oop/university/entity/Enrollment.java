package com.rmv.oop.university.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "enrollments", schema = "public")
@NoArgsConstructor
public class Enrollment {
    @Id
    @GeneratedValue(generator = "enrollments_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "enrollments_id_seq", sequenceName = "enrollments_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(name = "mark")
    private Integer mark;

    @Column(name = "review")
    private String review;
}
