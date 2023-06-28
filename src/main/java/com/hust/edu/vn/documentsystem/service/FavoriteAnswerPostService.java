package com.hust.edu.vn.documentsystem.service;

import java.util.List;

import com.hust.edu.vn.documentsystem.entity.FavoriteAnswerPost;

public interface FavoriteAnswerPostService {
    boolean toggleFavoriteAnswerPost(Long answerId);

    List<FavoriteAnswerPost> getAllFavoriteForAnswer(Long answerId);
}
