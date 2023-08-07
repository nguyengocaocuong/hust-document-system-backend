package com.hust.edu.vn.documentsystem.repository;

import com.hust.edu.vn.documentsystem.entity.ReportDuplicateSubjectDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ReportDuplicateSubjectDocumentRepository extends JpaRepository<ReportDuplicateSubjectDocument, Long> {
    @Query("""
            SELECT r
            FROM ReportDuplicateSubjectDocument r
            WHERE r.owner.email = :email
            """)
    List<ReportDuplicateSubjectDocument> getAllReported(String email);
}