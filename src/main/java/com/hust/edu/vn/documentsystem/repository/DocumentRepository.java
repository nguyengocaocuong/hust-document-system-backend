package com.hust.edu.vn.documentsystem.repository;

import com.hust.edu.vn.documentsystem.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;



public interface DocumentRepository extends JpaRepository<Document, Long> {
}