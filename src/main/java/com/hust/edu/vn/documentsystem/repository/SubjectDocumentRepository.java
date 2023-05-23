package com.hust.edu.vn.documentsystem.repository;

import com.hust.edu.vn.documentsystem.common.type.SubjectDocumentType;
import com.hust.edu.vn.documentsystem.entity.Document;
import com.hust.edu.vn.documentsystem.entity.SubjectDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectDocumentRepository extends JpaRepository<SubjectDocument, Long> {
    SubjectDocument findByDocument(Document document);

    @Query(value = "SELECT DISTINCT sd.type FROM SubjectDocument AS sd")
    List<SubjectDocumentType> findAllSubjectDocumentType();

    @Query(value = "SELECT DISTINCT sd.semester FROM SubjectDocument AS sd")
    List<String> findAllSemester();
}