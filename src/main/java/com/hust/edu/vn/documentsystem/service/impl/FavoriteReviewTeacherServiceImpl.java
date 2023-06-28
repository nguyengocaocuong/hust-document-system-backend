package com.hust.edu.vn.documentsystem.service.impl;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.hust.edu.vn.documentsystem.entity.FavoriteReviewTeacher;
import com.hust.edu.vn.documentsystem.entity.ReviewTeacher;
import com.hust.edu.vn.documentsystem.entity.User;
import com.hust.edu.vn.documentsystem.repository.FavoriteReviewTeacherRepository;
import com.hust.edu.vn.documentsystem.repository.ReviewTeacherRepository;
import com.hust.edu.vn.documentsystem.repository.UserRepository;
import com.hust.edu.vn.documentsystem.service.FavoriteReviewTeacherService;

@Service
public class FavoriteReviewTeacherServiceImpl implements FavoriteReviewTeacherService {

    private final ReviewTeacherRepository reviewTeacherRepository;
    private final UserRepository userRepository;
    private final FavoriteReviewTeacherRepository favoriteReviewTeacherRepository;

    public FavoriteReviewTeacherServiceImpl(ReviewTeacherRepository reviewTeacherRepository,
            UserRepository userRepository, FavoriteReviewTeacherRepository favoriteReviewTeacherRepository) {
        this.reviewTeacherRepository = reviewTeacherRepository;
        this.userRepository = userRepository;
        this.favoriteReviewTeacherRepository = favoriteReviewTeacherRepository;
    }

    @Override
    public boolean toggleFavoriteReviewTeacher(Long reviewTeacherId) {
        ReviewTeacher reviewTeacher = reviewTeacherRepository.findById(reviewTeacherId).orElse(null);
        if (reviewTeacher == null)
            return false;
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        FavoriteReviewTeacher favoriteReviewTeacher = favoriteReviewTeacherRepository
                .findByReviewTeacherAndUser(reviewTeacher, user);
        if (favoriteReviewTeacher != null) {
            favoriteReviewTeacherRepository.delete(favoriteReviewTeacher);
            return true;
        }
        favoriteReviewTeacher = new FavoriteReviewTeacher();
        favoriteReviewTeacher.setReviewTeacher(reviewTeacher);
        favoriteReviewTeacher.setUser(user);
        favoriteReviewTeacherRepository.save(favoriteReviewTeacher);
        return true;
    }

    @Override
    public List<FavoriteReviewTeacher> getAllFavoriteReviewTeacher(Long reviewTeacherId) {
        return favoriteReviewTeacherRepository.findAllByReviewTeacherId(reviewTeacherId);
    }

}
