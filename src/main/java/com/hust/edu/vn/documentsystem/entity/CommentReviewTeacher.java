package com.hust.edu.vn.documentsystem.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    @Column(nullable = false)
    private String comment;
    @ManyToOne
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_CommentReviewTeacher_User"))
    private User owner;
    @Column(nullable = false)
    private Date createdAt = new Date();
    @ManyToOne
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_CommentReviewTeacher_ReviewTeacher"))
    private ReviewTeacher reviewTeacher;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_CommentReviewTeacher_CommentReviewTeacher"))
    private CommentReviewTeacher parentComment = null;
    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.REMOVE)
    private List<CommentReviewTeacher> childComment = new ArrayList<>();
    private boolean isHidden = false;
    private Float score = 0F;
}