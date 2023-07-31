package com.hust.edu.vn.documentsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hust.edu.vn.documentsystem.common.type.DocumentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private String description;
    @Column(nullable = false)
    private DocumentType type;
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
    @OneToMany(mappedBy = "answerSubjectDocument", cascade = CascadeType.REMOVE)
    private List<FavoriteAnswerSubjectDocument> favorites = new ArrayList<>();
}
