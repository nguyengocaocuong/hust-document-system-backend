package com.hust.edu.vn.documentsystem.repository;

import com.hust.edu.vn.documentsystem.entity.CommentSubjectDocument;
import com.hust.edu.vn.documentsystem.entity.SubjectDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentSubjectDocumentRepository extends JpaRepository<CommentSubjectDocument, Long> {
    @Query("SELECT csd FROM  CommentSubjectDocument csd WHERE csd.subjectDocument.id = :subjectDocumentId AND csd.parentComment = null")
    List<CommentSubjectDocument> findAllCommentById(Long subjectDocumentId);
}
