package com.hust.edu.vn.documentsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "ShareByLinks")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ShareByLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id",foreignKey = @ForeignKey(name = "fk_ShareByLink_Document"))
    private Document document;

    @Column(name = "shared_at")
    private Date sharedAt = new Date();

    private Date expirationTime;

    @Column(name = "token")
    private String token;

}
