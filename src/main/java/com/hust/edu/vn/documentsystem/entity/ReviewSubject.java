package com.hust.edu.vn.documentsystem.entity;

import com.hust.edu.vn.documentsystem.common.type.ApproveType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "Review_subjects", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"owner_id", "subject_id"})
})
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReviewSubject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String review;

    @Column(name = "is_done")
    private boolean isDone = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @Column(name = "created_at")
    private Date createdAt = new Date();

    private boolean isHidden = false;

    @Enumerated(EnumType.STRING)
    private ApproveType approved = ApproveType.NEW;
}
