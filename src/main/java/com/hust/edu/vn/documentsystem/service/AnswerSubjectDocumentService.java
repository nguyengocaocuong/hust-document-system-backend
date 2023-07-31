package com.hust.edu.vn.documentsystem.service;

import com.hust.edu.vn.documentsystem.data.model.AnswerSubjectDocumentModel;
import com.hust.edu.vn.documentsystem.entity.AnswerSubjectDocument;

import java.util.List;

public interface AnswerSubjectDocumentService {
    AnswerSubjectDocument saveAnswerForSubjectDocument(Long subjectDocumentId, AnswerSubjectDocumentModel answerSubjectDocumentModel);

    List<AnswerSubjectDocument> getAllAnswerSubjectDocument(Long subjectDocumentId);

    List<Object> readAnswerSubjectDocument(Long answerSubjectDocumentId, Long subjectDocumentId);
}
