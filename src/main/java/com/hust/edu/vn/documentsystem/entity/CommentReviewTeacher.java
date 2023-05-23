package com.hust.edu.vn.documentsystem.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "CommentReviewTeachers")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CommentReviewTeacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "comment", nullable = false)
    private String comment;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false, foreignKey = @ForeignKey(name = "fk_CommentReviewTeacher_User"))
    private User owner;

    @Column(name = "created_at", nullable = false)
    private Date createdAt = new Date();

    @ManyToOne
    @JoinColumn(name = "review_teacher_id", nullable = false, foreignKey = @ForeignKey(name = "fk_CommentReviewTeacher_ReviewTeacher"))
    private ReviewTeacher reviewTeacher;

    private boolean isHidden = false;
}