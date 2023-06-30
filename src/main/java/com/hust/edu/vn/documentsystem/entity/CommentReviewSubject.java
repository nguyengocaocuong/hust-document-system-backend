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
@Table(name = "CommentReviewSubjects")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CommentReviewSubject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String comment;

    @ManyToOne
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_CommentReviewSubject_User"))
    private User owner;

    @Column(nullable = false)
    private Date createdAt = new Date();

    @ManyToOne
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_CommentReviewSubject_ReviewSubject"))
    private ReviewSubject reviewSubject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_CommentReviewSubject_CommentReviewSubject"))
    @JsonIgnore
    private CommentReviewSubject parentComment = null;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.REMOVE)
    private List<CommentReviewSubject> childComment = new ArrayList<>();

    private boolean isHidden = false;
}
