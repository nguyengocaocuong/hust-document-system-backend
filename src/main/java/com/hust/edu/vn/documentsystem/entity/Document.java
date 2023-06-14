package com.hust.edu.vn.documentsystem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "Documents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String path;

    @Column(columnDefinition = "TEXT")
    private String thumbnail;

    @Column(nullable = false)
    private Date createdAt = new Date();

    @Column(nullable = false)
    private Date updateAt = new Date();

    private String name;

    @Column(nullable = false)
    private String contentType;

    private String url;
}
