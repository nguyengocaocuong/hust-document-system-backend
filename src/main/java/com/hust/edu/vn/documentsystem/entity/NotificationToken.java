package com.hust.edu.vn.documentsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Table(name = "NotificationToken")
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_NotificationToken_User"))
    private User user;
    @Column(nullable = false)
    private String token;
}
