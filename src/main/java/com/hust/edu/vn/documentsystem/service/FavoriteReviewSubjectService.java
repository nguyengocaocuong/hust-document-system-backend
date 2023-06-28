package com.hust.edu.vn.documentsystem.service;

import java.util.List;

import com.hust.edu.vn.documentsystem.entity.FavoriteReviewSubject;

public interface FavoriteReviewSubjectService {
            boolean toggleFavoriteReviewSubject(Long reviewSubjectId);
                    List<FavoriteReviewSubject> getAllFavoriteForReviewSubject(Long reviewSubjectId);

}
