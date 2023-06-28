package com.hust.edu.vn.documentsystem.service;

import java.util.List;

import com.hust.edu.vn.documentsystem.data.model.AnswerPostModel;
import com.hust.edu.vn.documentsystem.entity.AnswerPost;

public interface AnswerPostService {
    List<AnswerPost> findAllAnswerForPost(Long postId);

    AnswerPost createAnswerForPost(Long postId, AnswerPostModel answerPostModel);

    List<Object> readAnswerPost(Long answerId, Long postId);

    boolean deleteAnswerForPost(Long answerId, Long postId);

}
