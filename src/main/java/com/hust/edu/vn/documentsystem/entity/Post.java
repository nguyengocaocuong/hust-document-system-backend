package com.hust.edu.vn.documentsystem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

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
    @JoinColumn(name = "owner", nullable = false, foreignKey = @ForeignKey(name = "fk_Post_User"))
    private User owner;

    @Column()
    private String description;

    @Column()
    private String descriptionEn;

    @Column(columnDefinition = "text")
    private String content;

    @Column(columnDefinition = "text")
    private String contentEn;

    @OneToOne(fetch = FetchType.LAZY)
    private Document document;

    @Column(name = "created_at", nullable = false)
    private Date createdAt = new Date();

    @OneToOne(fetch = FetchType.LAZY)
    private Subject subject;

    private boolean isDone = false;

    private boolean isHidden = false;
}