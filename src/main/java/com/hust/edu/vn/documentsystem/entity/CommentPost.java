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
@Table(name = "CommentPosts")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CommentPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String comment;

    @ManyToOne
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_CommentPost_User"))
    private User owner;

    @ManyToOne
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_CommentPost_Post"))
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_CommentPost_CommentPost"))
    @JsonIgnore
    private CommentPost parentComment = null;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.REMOVE)
    private List<CommentPost> childComment = new ArrayList<>();

    @Column(nullable = false)
    private Date createdAt = new Date();

    private boolean isHidden = false;
}