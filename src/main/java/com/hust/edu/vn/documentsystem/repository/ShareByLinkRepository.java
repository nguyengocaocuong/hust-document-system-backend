package com.hust.edu.vn.documentsystem.repository;

import com.hust.edu.vn.documentsystem.entity.ShareByLink;
import com.hust.edu.vn.documentsystem.entity.SubjectDocument;
import org.springframework.data.jpa.repository.JpaRepository;



public interface ShareByLinkRepository extends JpaRepository<ShareByLink, Long> {
    ShareByLink findBySubjectDocument(SubjectDocument subjectDocument);

    ShareByLink findBySubjectDocumentAndToken(SubjectDocument subjectDocument, String token);
}