package com.hust.edu.vn.documentsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hust.edu.vn.documentsystem.common.type.DocumentType;
import com.hust.edu.vn.documentsystem.common.type.SubjectDocumentType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "SubjectDocuments")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SubjectDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column( nullable = false)
    @Enumerated(EnumType.STRING)
    private SubjectDocumentType subjectDocumentType;

    private DocumentType type;

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
    private Date createdAt = new Date();
    private Date lastEditedAt = new Date();

    @OneToMany(mappedBy = "subjectDocument", cascade = CascadeType.REMOVE)
    private List<CommentSubjectDocument> comments = new ArrayList<>();
    @OneToMany(mappedBy = "subjectDocument", cascade = CascadeType.REMOVE)
    private List<AnswerSubjectDocument> answers = new ArrayList<>();
    @OneToMany(mappedBy = "subjectDocument", cascade = CascadeType.REMOVE)
    private List<FavoriteSubjectDocument> favorites = new ArrayList<>();
    @OneToMany(mappedBy = "subjectDocument", cascade = CascadeType.REMOVE)
    List<SharePrivate> shared = new ArrayList<>();

    @OneToMany(mappedBy = "subjectDocument", cascade = CascadeType.REMOVE)
    List<ShareByLink> shareByLinks = new ArrayList<>();

    private Date deletedAt = new Date();

    private boolean isDelete = false;
}
