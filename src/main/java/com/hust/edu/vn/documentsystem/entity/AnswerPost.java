package com.hust.edu.vn.documentsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "AnswerPosts")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AnswerPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition = "TEXT")
    private String contentEn;

    @Column(nullable = false)
    private String type;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", foreignKey = @ForeignKey(name = "fk_answer_post_document"))
    private Document document;

    private String name;

    @Column(nullable = false)
    private Date createdAt = new Date();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false , foreignKey = @ForeignKey(name = "fk_AnswerPost_Post"))
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false , foreignKey = @ForeignKey(name = "fk_AnswerPost_User"))
    private User owner;

    private boolean isHidden = false;
}
