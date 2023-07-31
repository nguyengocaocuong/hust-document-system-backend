package com.hust.edu.vn.documentsystem.entity;

import com.hust.edu.vn.documentsystem.common.type.NotificationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "FavoriteReviewSubjects")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FavoriteReviewSubject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn( nullable = false, foreignKey = @ForeignKey(name = "fk_FavoriteReviewSubject_ReviewSubject"))
    private ReviewSubject reviewSubject;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn( nullable = false, foreignKey = @ForeignKey(name = "fk_FavoriteReviewSubject_User"))
    private User user;
    private Date createAt = new Date();
    @Enumerated(EnumType.STRING)
    private NotificationType notificationType = NotificationType.ALL;
}
