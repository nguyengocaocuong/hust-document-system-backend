package com.hust.edu.vn.documentsystem.repository;

import com.hust.edu.vn.documentsystem.entity.ReportDuplicateDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportDuplicateDocumentRepository extends JpaRepository<ReportDuplicateDocument, Long> {
    @Query("SELECT DISTINCT rd FROM ReportDuplicateDocument rd GROUP BY rd.documentFirst, rd.documentSecond HAVING COUNT(rd.id) > 5")
    List<ReportDuplicateDocument> findAllReportDuplicateDocuments();
}