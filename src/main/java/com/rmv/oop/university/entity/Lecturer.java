package com.rmv.oop.university.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "lecturers", schema = "public")
@NoArgsConstructor
public class Lecturer {
    @Id
    @GeneratedValue(generator = "lecturers_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "lecturers_id_seq", sequenceName = "lecturers_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "surname")
    private String surname;

    @Column(name = "first_name")
    private String firstName;
}