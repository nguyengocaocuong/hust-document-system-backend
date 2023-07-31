package com.hust.edu.vn.documentsystem.service.impl;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.hust.edu.vn.documentsystem.entity.AnswerSubjectDocument;
import com.hust.edu.vn.documentsystem.entity.FavoriteAnswerSubjectDocument;
import com.hust.edu.vn.documentsystem.entity.User;
import com.hust.edu.vn.documentsystem.repository.AnswerSubjectDocumentRepository;
import com.hust.edu.vn.documentsystem.repository.FavoriteAnswerSubjectDocumentRepository;
import com.hust.edu.vn.documentsystem.repository.UserRepository;
import com.hust.edu.vn.documentsystem.service.FavoriteAnswerSubjectDocumentService;

@Service
public class FavoriteAnswerSubjectDocumentServiceImpl implements FavoriteAnswerSubjectDocumentService {
    private final AnswerSubjectDocumentRepository answerSubjectDocumentRepository;
    private final FavoriteAnswerSubjectDocumentRepository favoriteAnswerSubjectDocumentRepository;
    private final UserRepository userRepository;

    public FavoriteAnswerSubjectDocumentServiceImpl(AnswerSubjectDocumentRepository answerSubjectDocumentRepository,
                                                    FavoriteAnswerSubjectDocumentRepository favoriteAnswerSubjectDocumentRepository,
                                                    UserRepository userRepository) {
        this.answerSubjectDocumentRepository = answerSubjectDocumentRepository;
        this.favoriteAnswerSubjectDocumentRepository = favoriteAnswerSubjectDocumentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public boolean favoriteAnswerSubjectDocument(Long answerSubjectDocumentID) {
        AnswerSubjectDocument answerSubjectDocument = answerSubjectDocumentRepository.findById(answerSubjectDocumentID)
                .orElse(null);
        if (answerSubjectDocument == null)
            return false;
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        FavoriteAnswerSubjectDocument favoriteAnswerSubjectDocument = favoriteAnswerSubjectDocumentRepository
                .findByAnswerSubjectDocumentAndUser(answerSubjectDocument, user);
        if (favoriteAnswerSubjectDocument != null) {
            favoriteAnswerSubjectDocumentRepository.delete(favoriteAnswerSubjectDocument);
            return true;
        }
        favoriteAnswerSubjectDocument = new FavoriteAnswerSubjectDocument();
        favoriteAnswerSubjectDocument.setUser(user);
        favoriteAnswerSubjectDocument.setAnswerSubjectDocument(answerSubjectDocument);
        favoriteAnswerSubjectDocumentRepository.save(favoriteAnswerSubjectDocument);
        return true;
    }

    @Override
    public List<FavoriteAnswerSubjectDocument> getAllFavoriteAnswerSubjectDocument(Long answerSubjectDocumentId) {
        return favoriteAnswerSubjectDocumentRepository.findAllByAnswerSubjectDocumentId(answerSubjectDocumentId);
    }

}
