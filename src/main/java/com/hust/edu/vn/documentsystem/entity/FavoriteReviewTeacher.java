package com.hust.edu.vn.documentsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@Entity
@Table(name = "FavoriteReviewTeachers")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FavoriteReviewTeacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_FavoriteReviewTeacher_ReviewTeacher"))
    private ReviewTeacher reviewTeacher;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn( nullable = false, foreignKey = @ForeignKey(name = "fk_FavoriteReviewTeacher_User"))
    private User user;
    private Date createAt = new Date();
}
