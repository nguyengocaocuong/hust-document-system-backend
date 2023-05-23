package com.hust.edu.vn.documentsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    @ManyToOne
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_CommentSubjectDocument_User"))
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_CommentSubjectDocument_SubjectDocument"))
    private SubjectDocument subjectDocument;

    @Column(nullable = false)
    private Date created_at = new Date();

    private boolean isHidden = false;


}
