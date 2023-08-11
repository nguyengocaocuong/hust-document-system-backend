package com.hust.edu.vn.documentsystem.entity;

import com.hust.edu.vn.documentsystem.common.type.DocumentType;
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
    @ManyToOne
    @JoinColumn(nullable = false)
    private SubjectDocumentType subjectDocumentType;
    private DocumentType type;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_SubjectDocument_Document"))
    private Document document;
    private String description;
    private String descriptionEn;
    private String descriptionNoDiacritics;

    @Column(nullable = false)
    private String semester;

    private Long tesSemester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_SubjectDocument_Subject"))
    private Subject subject;
    private boolean isPublic = false;
    private Date createdAt = new Date();

    private Long totalDownload = 0L;

    private Long totalView = 0L;

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
    @OneToMany(mappedBy = "subjectDocument", cascade = CascadeType.REMOVE)
    private List<ReportContentSubjectDocument> reportContents;
    @OneToMany(mappedBy = "subjectDocumentFirst", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<ReportDuplicateSubjectDocument> reportDuplicatesFirst;
    @OneToMany(mappedBy = "subjectDocumentSecond", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<ReportDuplicateSubjectDocument> reportDuplicatesSecond;
    private Date deletedAt = new Date();
    private boolean isDelete = false;
}
