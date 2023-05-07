package com.hust.edu.vn.documentsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "CommentSubjectDocuments")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommentSubjectDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String comment;

    private Integer rating;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @ManyToOne
    @JoinColumn(name = "document_id", nullable = false)
    private SubjectDocument subjectDocument;

    @Column(nullable = false)
    private Date created_at = new Date();

    private boolean isHidden = false;


}
