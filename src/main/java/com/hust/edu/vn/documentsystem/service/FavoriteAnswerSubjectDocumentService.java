package com.hust.edu.vn.documentsystem.service;

import java.util.List;

import com.hust.edu.vn.documentsystem.entity.FavoriteAnswerSubjectDocument;

public interface FavoriteAnswerSubjectDocumentService {
    boolean favoriteAnswerSubjectDocument(Long answerSubjectDocumentID);

    List<FavoriteAnswerSubjectDocument> getAllFavoriteAnswerSubjectDocument(Long answerSubjectDocumentId);
}
