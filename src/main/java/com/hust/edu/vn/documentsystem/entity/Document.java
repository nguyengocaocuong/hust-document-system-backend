package com.hust.edu.vn.documentsystem.entity;

import com.hust.edu.vn.documentsystem.common.type.DocumentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "Documents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "text")
    private String content;

    @Column(columnDefinition = "text")
    private String contentEn;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DocumentType type;

    private String path;

    @Column(nullable = false)
    private Date createdAt = new Date();

    private String name;

    @Column(nullable = false)
    private String contentType;
}
