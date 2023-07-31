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
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;
    private String descriptionEn;
    @Column(name = "created_at")
    private Date createdAt = new Date();
    private boolean isDone = false;
    private boolean isHidden = false;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_Post_User"))
    private User owner;
    @ManyToOne(fetch = FetchType.LAZY)
    private Subject subject;
    @ManyToOne(fetch = FetchType.LAZY)
    private Document document;
    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<FavoritePost> favorites = new ArrayList<>();
    @OneToMany(mappedBy = "post" , cascade = CascadeType.REMOVE)
    private List<AnswerPost> answers = new ArrayList<>();
    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<CommentPost> comments = new ArrayList<>();
    private boolean isDelete = false;
}