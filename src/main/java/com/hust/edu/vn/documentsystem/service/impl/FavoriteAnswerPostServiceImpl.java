package com.hust.edu.vn.documentsystem.service.impl;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.hust.edu.vn.documentsystem.entity.AnswerPost;
import com.hust.edu.vn.documentsystem.entity.FavoriteAnswerPost;
import com.hust.edu.vn.documentsystem.entity.User;
import com.hust.edu.vn.documentsystem.repository.AnswerPostRepository;
import com.hust.edu.vn.documentsystem.repository.FavoriteAnswerPostRepository;
import com.hust.edu.vn.documentsystem.repository.UserRepository;
import com.hust.edu.vn.documentsystem.service.FavoriteAnswerPostService;

@Service
public class FavoriteAnswerPostServiceImpl implements FavoriteAnswerPostService {
    private final AnswerPostRepository answerPostRepository;
    private final FavoriteAnswerPostRepository favoriteAnswerPostRepository;
    private final UserRepository userRepository;

    public FavoriteAnswerPostServiceImpl(AnswerPostRepository answerPostRepository,
            FavoriteAnswerPostRepository favoriteAnswerPostRepository, UserRepository userRepository) {
        this.answerPostRepository = answerPostRepository;
        this.favoriteAnswerPostRepository = favoriteAnswerPostRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<FavoriteAnswerPost> getAllFavoriteForAnswer(Long answerId) {
        return favoriteAnswerPostRepository.findAllByAnswerId(answerId);
    }

    @Override
    public boolean toggleFavoriteAnswerPost(Long answerId) {
        AnswerPost answerPost = answerPostRepository.findById(answerId).orElse(null);
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if (answerPost == null)
            return false;
        FavoriteAnswerPost favoriteAnswerPost = favoriteAnswerPostRepository.findByIdAndUser(answerId, user);
        if (favoriteAnswerPost != null) {
            favoriteAnswerPostRepository.delete(favoriteAnswerPost);
            return true;
        }
        favoriteAnswerPost = new FavoriteAnswerPost();
        favoriteAnswerPost.setAnswerPost(answerPost);
        favoriteAnswerPost.setUser(user);
        favoriteAnswerPostRepository.save(favoriteAnswerPost);
        return true;
    }

}
