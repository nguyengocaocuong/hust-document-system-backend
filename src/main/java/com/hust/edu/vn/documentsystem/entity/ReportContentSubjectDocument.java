package com.hust.edu.vn.documentsystem.entity;

import com.hust.edu.vn.documentsystem.common.type.ReportStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "ReportContentSubjectDocuments", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "owner_id", "subject_document_id" })
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportContentSubjectDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Date createdAt = new Date();

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReportStatus status = ReportStatus.NEW_REPORT;

    private String message;
    private String processMessage;

    @ManyToOne
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_ReportContent_User"))
    private User owner;

    @ManyToOne
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_ReportContent_SubjectDocument"))
    private SubjectDocument subjectDocument;
}
