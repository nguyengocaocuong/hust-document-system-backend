package com.hust.edu.vn.documentsystem.entity;

import com.hust.edu.vn.documentsystem.common.type.ReportStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "ReportContentReviewSubjects", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "owner_id", "review_subject_id" })
})
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
@ToString
public class ReportContentReviewSubject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_ReportContentReviewSubject_ReviewSubject"))
    private ReviewSubject reviewSubject;
    private Date createdAt = new Date();
    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_ReportContentReviewSubject_User"))
    private User owner;
    private String message;
    private String processMessage;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReportStatus status = ReportStatus.NEW_REPORT;

}
