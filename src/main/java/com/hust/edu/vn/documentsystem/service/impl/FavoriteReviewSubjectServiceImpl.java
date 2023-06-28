package com.hust.edu.vn.documentsystem.service.impl;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.hust.edu.vn.documentsystem.entity.FavoriteReviewSubject;
import com.hust.edu.vn.documentsystem.entity.ReviewSubject;
import com.hust.edu.vn.documentsystem.entity.User;
import com.hust.edu.vn.documentsystem.repository.FavoriteReviewSubjectRepository;
import com.hust.edu.vn.documentsystem.repository.ReviewSubjectRepository;
import com.hust.edu.vn.documentsystem.repository.UserRepository;
import com.hust.edu.vn.documentsystem.service.FavoriteReviewSubjectService;

@Service
public class FavoriteReviewSubjectServiceImpl implements FavoriteReviewSubjectService {
    private final FavoriteReviewSubjectRepository favoriteReviewSubjectRepository;
    private final ReviewSubjectRepository reviewSubjectRepository;
    private final UserRepository userRepository;
    
    public FavoriteReviewSubjectServiceImpl(FavoriteReviewSubjectRepository favoriteReviewSubjectRepository,
            ReviewSubjectRepository reviewSubjectRepository, UserRepository userRepository) {
        this.favoriteReviewSubjectRepository = favoriteReviewSubjectRepository;
        this.reviewSubjectRepository = reviewSubjectRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<FavoriteReviewSubject> getAllFavoriteForReviewSubject(Long reviewSubjectId) {
        return favoriteReviewSubjectRepository.findAllByReviewSubjectId(reviewSubjectId);
    }

    @Override
    public boolean toggleFavoriteReviewSubject(Long reviewSubjectId) {
         User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        ReviewSubject reviewSubject = reviewSubjectRepository.findById(reviewSubjectId).orElse(null);
        if (reviewSubject == null) return false;
        FavoriteReviewSubject favoriteReviewSubject = favoriteReviewSubjectRepository.findByReviewSubjectAndUser(reviewSubject, user);
        if (favoriteReviewSubject != null) {
            favoriteReviewSubjectRepository.delete(favoriteReviewSubject);
            return true;
        }
        favoriteReviewSubject = new FavoriteReviewSubject();
        favoriteReviewSubject.setReviewSubject(reviewSubject);
        favoriteReviewSubject.setUser(user);
        favoriteReviewSubjectRepository.save(favoriteReviewSubject);
        return true;
    }

}
