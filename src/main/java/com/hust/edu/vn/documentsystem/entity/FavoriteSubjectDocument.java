package com.hust.edu.vn.documentsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hust.edu.vn.documentsystem.common.type.NotificationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "FavoriteSubjectDocuments")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FavoriteSubjectDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_FavoriteSubjectDocument_SubjectDocument"))
    @JsonIgnore
    private SubjectDocument subjectDocument;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_FavoriteSubjectDocument_User"))
    private User user;

    private Date createAt = new Date();

    @Column()
    @Enumerated(EnumType.STRING)
    private NotificationType notificationType = NotificationType.ALL;
}
