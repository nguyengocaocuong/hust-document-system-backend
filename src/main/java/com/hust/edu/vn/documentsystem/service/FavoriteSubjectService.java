package com.hust.edu.vn.documentsystem.service;

import java.util.List;

import com.hust.edu.vn.documentsystem.entity.FavoriteSubject;

public interface FavoriteSubjectService {
    boolean favoriteSubject(Long subjectId);

    List<FavoriteSubject> getAllFavoriteForSubject(Long subjectId);
}
