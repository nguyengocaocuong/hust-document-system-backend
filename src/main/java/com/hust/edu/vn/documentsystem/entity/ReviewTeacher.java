package com.hust.edu.vn.documentsystem.entity;
import com.hust.edu.vn.documentsystem.common.type.ApproveType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "Review_teachers")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReviewTeacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String review;

    @Column(name = "is_done")
    private boolean done = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_ReviewTeacher_User"))
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_ReviewTeacher_Teacher"))
    private Teacher teacher;

    @Column()
    private Date createdAt = new Date();

    private boolean isHidden = false;

    @Enumerated(EnumType.STRING)
    private ApproveType approved = ApproveType.NEW;
}