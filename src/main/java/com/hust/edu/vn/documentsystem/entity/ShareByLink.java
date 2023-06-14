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
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_ShareByLink_SubjectDocument"))
    private SubjectDocument subjectDocument;

    @Column()
    private Date sharedAt = new Date();

    private Date expirationTime;

    @Column(columnDefinition = "TEXT")
    private String token;

}
