package com.hust.edu.vn.documentsystem.repository;

import com.hust.edu.vn.documentsystem.entity.ReportContentSubjectDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ReportContentSubjectDocumentRepository extends JpaRepository<ReportContentSubjectDocument, Long> {

}