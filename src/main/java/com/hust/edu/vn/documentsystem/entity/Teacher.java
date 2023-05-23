package com.hust.edu.vn.documentsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Teachers")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Teacher {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column( nullable = false, unique = true)
    private String email;

    @Column()
    private String phoneNumber;

    @Column()
    private Date createdAt = new Date();

    @Column()
    private String avatar;

    @Column()
    private Date dob;

    @ManyToMany(mappedBy = "teachers", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Subject> subjects = new ArrayList<>();

    @Column( columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn( nullable = false,foreignKey = @ForeignKey(name = "fk_Teacher_User"))
    private User owner;

}




