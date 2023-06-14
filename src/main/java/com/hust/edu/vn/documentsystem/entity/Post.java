package com.hust.edu.vn.documentsystem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Posts")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_Post_User"))
    private User owner;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    private String descriptionEn;

    @OneToOne(fetch = FetchType.LAZY)
    private Document document;

    @Column(name = "created_at")
    private Date createdAt = new Date();

    @OneToOne(fetch = FetchType.LAZY)
    private Subject subject;

    private boolean isDone = false;

    private boolean isHidden = false;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<FavoritePost> favoritePostList = new ArrayList<>();

    @OneToMany(mappedBy = "post" , cascade = CascadeType.REMOVE)
    private List<AnswerPost> answerPostList = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<CommentPost> commentPosts = new ArrayList<>();

    private boolean isDelete = false;
}