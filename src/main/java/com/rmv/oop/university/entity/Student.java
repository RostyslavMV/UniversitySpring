package com.rmv.oop.university.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "students", schema = "public")
@NoArgsConstructor
public class Student {
    @Id
    @GeneratedValue(generator = "students_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "students_id_seq", sequenceName = "students_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "surname")
    private String surname;

    @Column(name = "first_name")
    private String firstName;
}
