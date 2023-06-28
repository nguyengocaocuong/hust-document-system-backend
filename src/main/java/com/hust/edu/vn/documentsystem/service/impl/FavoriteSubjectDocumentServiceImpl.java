package com.hust.edu.vn.documentsystem.service.impl;

import org.springframework.security.core.context.SecurityContextHolder;

import com.hust.edu.vn.documentsystem.common.type.NotificationType;
import com.hust.edu.vn.documentsystem.entity.FavoriteSubjectDocument;
import com.hust.edu.vn.documentsystem.entity.SubjectDocument;
import com.hust.edu.vn.documentsystem.entity.User;
import com.hust.edu.vn.documentsystem.repository.FavoriteSubjectDocumentRepository;
import com.hust.edu.vn.documentsystem.repository.SubjectDocumentRepository;
import com.hust.edu.vn.documentsystem.repository.UserRepository;
import com.hust.edu.vn.documentsystem.service.FavoriteSubjectDocumentService;

public class FavoriteSubjectDocumentServiceImpl implements FavoriteSubjectDocumentService {
    private final UserRepository userRepository;
    private final SubjectDocumentRepository subjectDocumentRepository;
    private final FavoriteSubjectDocumentRepository favoriteSubjectDocumentRepository;

    public FavoriteSubjectDocumentServiceImpl(UserRepository userRepository,
            SubjectDocumentRepository subjectDocumentRepository,
            FavoriteSubjectDocumentRepository favoriteSubjectDocumentRepository) {
        this.userRepository = userRepository;
        this.favoriteSubjectDocumentRepository = favoriteSubjectDocumentRepository;
        this.subjectDocumentRepository = subjectDocumentRepository;
    }

    @Override
    public boolean favoriteSubjectDocument(Long subjectDocumentId) {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        FavoriteSubjectDocument favoriteSubjectDocument = favoriteSubjectDocumentRepository.findByUser(user);
        if (favoriteSubjectDocument != null
                && favoriteSubjectDocument.getSubjectDocument().getId().equals(subjectDocumentId)) {
            favoriteSubjectDocumentRepository.delete(favoriteSubjectDocument);
            return true;
        }
        if (favoriteSubjectDocument == null) {
            SubjectDocument subjectDocument = subjectDocumentRepository.findById(subjectDocumentId).orElse(null);
            if (subjectDocument != null) {
                favoriteSubjectDocument = new FavoriteSubjectDocument();
                favoriteSubjectDocument.setSubjectDocument(subjectDocument);
                favoriteSubjectDocument.setUser(user);
                favoriteSubjectDocument.setNotificationType(NotificationType.ALL);
                favoriteSubjectDocumentRepository.save(favoriteSubjectDocument);
                return true;
            }
        }
        return false;
    }

}
