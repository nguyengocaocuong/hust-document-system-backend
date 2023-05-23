package com.hust.edu.vn.documentsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "AnswerSubjectDocuments")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AnswerSubjectDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private Date createdAt = new Date();

    @OneToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_AnswerSubjectDocument_Document"))
    private Document document;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false,  foreignKey = @ForeignKey(name = "fk_AnswerSubjectDocument_SubjectDocument"))
    @JsonIgnore
    private SubjectDocument subjectDocument;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_AnswerSubject_DocumentUser"))
    private User owner;
}
