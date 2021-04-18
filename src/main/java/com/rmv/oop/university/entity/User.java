package com.rmv.oop.university.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "users", schema = "public")
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(generator = "users_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "users_id_seq", sequenceName = "users_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String userName;

    @OneToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @OneToOne
    @JoinColumn(name = "lecturer_id")
    private Lecturer lecturer;
}
