package com.hust.edu.vn.documentsystem.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "SharePrivates")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SharePrivate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id")
    private Document document;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @CollectionTable
    private User user;

    @Column(name = "shared_at")
    private LocalDateTime sharedAt;

    @Column(name = "expiration_time")
    private Date expirationTime;
}
