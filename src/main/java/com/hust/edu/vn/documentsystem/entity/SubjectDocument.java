package com.hust.edu.vn.documentsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hust.edu.vn.documentsystem.common.type.SubjectDocumentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "SubjectDocuments")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SubjectDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column( nullable = false)
    @Enumerated(EnumType.STRING)
    private SubjectDocumentType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false,foreignKey = @ForeignKey(name = "fk_SubjectDocument_Document"))
    private Document document;

    @Column(nullable = false)
    private String description = "";

    @Column(nullable = false)
    private String descriptionEn = "";
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false,foreignKey = @ForeignKey(name = "fk_SubjectDocument_Subject"))
    @JsonIgnore
    private Subject subject;

    @Column()
    private boolean isPublic = false;

    @Column()
    private String semester;

    @OneToMany(mappedBy = "subjectDocument", fetch = FetchType.LAZY)
    private List<CommentSubjectDocument> commentSubjectDocumentList = new ArrayList<>();
    @OneToMany(mappedBy = "subjectDocument", fetch = FetchType.LAZY)
    private List<AnswerSubjectDocument> answerSubjectDocumentList = new ArrayList<>();
}
