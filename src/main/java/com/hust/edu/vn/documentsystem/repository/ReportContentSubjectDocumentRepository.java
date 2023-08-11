package com.hust.edu.vn.documentsystem.repository;

import com.hust.edu.vn.documentsystem.entity.ReportContentSubjectDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ReportContentSubjectDocumentRepository extends JpaRepository<ReportContentSubjectDocument, Long> {

    @Query("""
            SELECT r
            FROM ReportContentSubjectDocument r
            WHERE r.owner.email = :email
            """)
    List<ReportContentSubjectDocument> getAllReported(String email);


    @Query("""
            SELECT r
            FROM ReportContentSubjectDocument r
            WHERE r.id = :reportContentSubjectDocumentId AND r.owner.id = :userId AND r.subjectDocument.id =:subjectDocumentId 
            """)
    ReportContentSubjectDocument findByIdAndSubjectDocumentIdAndUserId(Long reportContentSubjectDocumentId, Long subjectDocumentId, Long userId);
}