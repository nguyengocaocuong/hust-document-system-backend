package com.hust.edu.vn.documentsystem.entity;
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
    @Column( unique = true)
    private String emailHust;
    @Column( unique = true)
    private String emailPrivate;
    private String phoneNumber;
    private Date createdAt = new Date();
    private String avatar;
    private Date dob;
    private String description;
    @OneToMany(mappedBy = "teacher", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<ReviewTeacher> reviews = new ArrayList<>();
}




