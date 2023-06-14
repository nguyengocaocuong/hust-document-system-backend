package com.hust.edu.vn.documentsystem.entity;

import com.hust.edu.vn.documentsystem.common.type.NotificationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@Entity
@Table(name = "FavoriteAnswerPosts")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FavoriteAnswerPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_FavoriteAnswerPost_AnswerPost"))
    private AnswerPost answerPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn( nullable = false)
    private User user;

    private Date createAt = new Date();

}
