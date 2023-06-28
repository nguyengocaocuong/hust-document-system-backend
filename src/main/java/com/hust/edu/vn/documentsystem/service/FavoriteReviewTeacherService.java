package com.hust.edu.vn.documentsystem.service;

import java.util.List;

import com.hust.edu.vn.documentsystem.entity.FavoriteReviewTeacher;

public interface FavoriteReviewTeacherService {
    boolean toggleFavoriteReviewTeacher(Long reviewTeacherId);

    List<FavoriteReviewTeacher> getAllFavoriteReviewTeacher(Long reviewTeacherId);
}
