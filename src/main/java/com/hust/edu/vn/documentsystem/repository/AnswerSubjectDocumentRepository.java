package com.hust.edu.vn.documentsystem.repository;

import com.hust.edu.vn.documentsystem.entity.AnswerSubjectDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface AnswerSubjectDocumentRepository extends JpaRepository<AnswerSubjectDocument, Long> {
    @Query(value = "SELECT a FROM AnswerSubjectDocument a WHERE a.subjectDocument.id = :subjectDocumentId")
    List<AnswerSubjectDocument> findAllBySubjectDocumentId(Long subjectDocumentId);

    @Query(value = "SELECT a FROM AnswerSubjectDocument a WHERE a.subjectDocument.id = :subjectDocumentId AND a.id = :answerSubjectDocumentId")
    AnswerSubjectDocument findByIdAndSubjectDocumentId(Long answerSubjectDocumentId, Long subjectDocumentId);

    @Query("""
                SELECT a
                FROM AnswerSubjectDocument a
                WHERE a.subjectDocument.id = :subjectDocumentId
                AND a.id IN :answerIds
            """)
    List<AnswerSubjectDocument> findAllBySubjectDocumentIdAndListId(Long subjectDocumentId, List<Long> answerIds);

    @Query("""
            SELECT asd
            FROM AnswerSubjectDocument  asd
            WHERE asd.id = :answerSubjectDocumentId AND asd.subjectDocument.id = :subjectDocumentID
            """)
    AnswerSubjectDocument fingByIdAndSubjectDocumentId(Long answerSubjectDocumentId, Long subjectDocumentID);
}
