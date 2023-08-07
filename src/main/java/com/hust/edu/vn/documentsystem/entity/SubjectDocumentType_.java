package com.hust.edu.vn.documentsystem.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "SubjectDocumentTypes")
@Data
public class SubjectDocumentType_ {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true )
    private String type;

    private String name;

    private Date createdAt = new Date();
}
