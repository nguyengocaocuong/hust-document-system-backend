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
    @JoinColumn(name = "subject_document_id", foreignKey = @ForeignKey(name = "fk_SharePrivate_SubjectDocument"))
    private SubjectDocument subjectDocument;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_SharePrivate_User"))
    private User user;

    @Column()
    private Date sharedAt = new Date();

    @Column()
    private Date expirationTime;
}
