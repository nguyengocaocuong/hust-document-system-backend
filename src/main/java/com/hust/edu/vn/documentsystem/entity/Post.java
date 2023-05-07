package com.hust.edu.vn.documentsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "Posts")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner", nullable = false)
    private User owner;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String descriptionEn;

    @Column(columnDefinition = "text")
    private String content;

    @Column(columnDefinition = "text")
    private String contentEn;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "document_id", nullable = false)
    private Document document;

    @Column(name = "created_at", nullable = false)
    private Date createdAt = new Date();

    @OneToOne(fetch = FetchType.LAZY)
    private Subject subject;

    private boolean isDone = false;

    private boolean isHidden = false;
}