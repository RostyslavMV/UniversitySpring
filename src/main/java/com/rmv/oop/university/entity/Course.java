package com.rmv.oop.university.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "courses", schema = "public")
@NoArgsConstructor
public class Course {

    @Id
    @GeneratedValue(generator = "courses_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "courses_id_seq", sequenceName = "courses_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "lecturer_id")
    private Lecturer lecturer;
}
