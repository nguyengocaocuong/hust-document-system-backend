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
@Table(name = "Subjects")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "subject_id", unique = true)
    private String subjectId;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_at")
    private Date createdAt = new Date();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "Teachings",
            joinColumns = @JoinColumn(name = "subject_id"),
            inverseJoinColumns = @JoinColumn(name = "teacher_id"))
    private List<Teacher> teachers = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false,foreignKey = @ForeignKey(name = "fk_Subject_User"))
    private User owner;

    @Column(nullable = false)
    private String subjectCode;

    @OneToMany(mappedBy = "subject", fetch = FetchType.LAZY)
    private List<SubjectDocument> subjectDocuments = new ArrayList<>();

}

