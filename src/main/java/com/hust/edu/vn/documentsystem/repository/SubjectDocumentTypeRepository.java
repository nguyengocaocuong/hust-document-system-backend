package com.hust.edu.vn.documentsystem.repository;

import com.hust.edu.vn.documentsystem.entity.SubjectDocumentType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectDocumentTypeRepository extends JpaRepository<SubjectDocumentType, Long> {
    boolean existsByType(String type);
}