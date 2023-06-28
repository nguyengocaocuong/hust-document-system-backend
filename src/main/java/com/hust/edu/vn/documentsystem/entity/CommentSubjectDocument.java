package com.hust.edu.vn.documentsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "CommentSubjectDocuments")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CommentSubjectDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String comment;

    @ManyToOne
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_CommentSubjectDocument_User"))
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_CommentSubjectDocument_SubjectDocument"))
    private SubjectDocument subjectDocument;

    @Column(nullable = false)
    private Date created_at = new Date();

    private boolean isHidden = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_CommentSubjectDocument_CommentSubjectDocument"))
    @JsonIgnore
    private CommentSubjectDocument parentComment = null;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.REMOVE)
    private List<CommentSubjectDocument> childComment = new ArrayList<>();

}
