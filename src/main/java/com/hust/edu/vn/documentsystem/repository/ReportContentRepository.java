package com.hust.edu.vn.documentsystem.repository;

import com.hust.edu.vn.documentsystem.entity.Document;
import com.hust.edu.vn.documentsystem.entity.ReportContent;
import com.hust.edu.vn.documentsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportContentRepository extends JpaRepository<ReportContent, Long> {
    ReportContent findByDocumentAndOwner(Document document, User user);

    @Query("SELECT DISTINCT rc FROM ReportContent rc GROUP BY rc.document HAVING COUNT(rc.id) > 5")
    List<ReportContent> findAllReports();
}