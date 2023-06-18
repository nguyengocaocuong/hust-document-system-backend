package com.hust.edu.vn.documentsystem.repository;

import com.hust.edu.vn.documentsystem.entity.ReportDuplicateSubjectDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportDuplicateSubjectDocumentRepository extends JpaRepository<ReportDuplicateSubjectDocument, Long> {
}