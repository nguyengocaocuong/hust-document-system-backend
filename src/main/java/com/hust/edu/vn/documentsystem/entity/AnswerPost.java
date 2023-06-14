package com.hust.edu.vn.documentsystem.entity;

import com.hust.edu.vn.documentsystem.common.type.DocumentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_answer_post_document"))
    private Document document;

    private DocumentType type;

    private String description;
    @Column(nullable = false)
    private Date createdAt = new Date();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false , foreignKey = @ForeignKey(name = "fk_AnswerPost_Post"))
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false , foreignKey = @ForeignKey(name = "fk_AnswerPost_User"))
    private User owner;

    private boolean isHidden = false;

    @OneToMany(mappedBy = "answerPost", cascade = CascadeType.REMOVE)
    private List<FavoriteAnswerPost> favorites = new ArrayList<>();
}
