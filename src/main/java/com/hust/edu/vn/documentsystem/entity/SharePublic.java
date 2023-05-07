package com.hust.edu.vn.documentsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "SharePublics")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SharePublic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id",nullable = false)
    private Document document;

    @Column(name = "shared_at")
    private Date sharedAt = new Date();
}
