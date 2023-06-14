package com.hust.edu.vn.documentsystem.repository;

import com.hust.edu.vn.documentsystem.entity.AnswerSubjectDocument;
import com.hust.edu.vn.documentsystem.entity.FavoriteAnswerSubjectDocument;
import com.hust.edu.vn.documentsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FavoriteAnswerSubjectDocumentRepository extends JpaRepository<FavoriteAnswerSubjectDocument, Long> {
    FavoriteAnswerSubjectDocument findByAnswerSubjectDocumentAndUser(AnswerSubjectDocument answerSubjectDocument, User user);

    @Query(value = "SELECT fasd FROM FavoriteAnswerSubjectDocument fasd WHERE fasd.answerSubjectDocument.id = :answerSubjectDocumentId")
    List<FavoriteAnswerSubjectDocument> findAllByAnswerSubjectDocumentId(Long answerSubjectDocumentId);
}