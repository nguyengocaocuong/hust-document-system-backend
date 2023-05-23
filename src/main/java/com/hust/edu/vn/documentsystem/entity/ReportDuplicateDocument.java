package com.hust.edu.vn.documentsystem.entity;
import com.hust.edu.vn.documentsystem.common.type.ReportStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "ReportDuplicateDocuments", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "first", "second" })
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportDuplicateDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "owner_id", foreignKey = @ForeignKey(name = "fk_ReportDuplicateDocument_User"))
    private User owner;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReportStatus status = ReportStatus.NEW_REPORT;

    private String message;

    @ManyToOne
    @JoinColumn(name = "first", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_ReportDuplicateDocument_DocumentFirst"))
    private Document documentFirst;

    @ManyToOne
    @JoinColumn(name = "second", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_ReportDuplicateDocument_DocumentSecond"))
    private Document documentSecond;

}
