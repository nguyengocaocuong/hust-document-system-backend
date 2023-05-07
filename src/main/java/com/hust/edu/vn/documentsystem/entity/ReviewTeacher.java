package com.hust.edu.vn.documentsystem.entity;
import com.hust.edu.vn.documentsystem.common.type.ApproveType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "Review_teachers", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"owner_id", "owner_id"})
})
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReviewTeacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String review;

    @Column(name = "is_done")
    private boolean done = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @Column(name = "created_at")
    private Date createdAt = new Date();

    private boolean isHidden = false;

    @Enumerated(EnumType.STRING)
    private ApproveType approved = ApproveType.NEW;
}