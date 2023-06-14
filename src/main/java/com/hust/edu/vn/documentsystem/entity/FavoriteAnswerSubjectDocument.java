package com.hust.edu.vn.documentsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "FavoriteAnswerSubjectDocuments")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FavoriteAnswerSubjectDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_FavoriteAnswerSubjectDocument_SubjectDocument"))
    private AnswerSubjectDocument answerSubjectDocument;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn( nullable = false)
    private User user;

    private Date createAt = new Date();
}
