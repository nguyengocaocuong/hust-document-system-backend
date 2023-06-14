package com.hust.edu.vn.documentsystem.entity;

import com.hust.edu.vn.documentsystem.common.type.ApproveType;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Review_subjects", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"owner_id", "subject_id"})
})
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ReviewSubject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String review;

    @Column(name = "is_done")
    private boolean isDone = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id",foreignKey = @ForeignKey(name = "fk_ReviewSubject_User") )
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_ReviewSubject_Subject"))
    private Subject subject;

    @Column()
    private Date createdAt = new Date();

    private boolean isHidden = false;

    @Enumerated(EnumType.STRING)
    private ApproveType approved = ApproveType.NEW;

    @OneToMany(mappedBy = "reviewSubject", cascade = CascadeType.REMOVE)
    private List<CommentReviewSubject> comments;

    @OneToMany(mappedBy = "reviewSubject", cascade = CascadeType.REMOVE)
    private List<FavoriteReviewSubject> favorites;

    private boolean isDelete = false;
}
