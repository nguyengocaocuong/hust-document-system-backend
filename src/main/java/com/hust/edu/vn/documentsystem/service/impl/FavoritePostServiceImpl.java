package com.hust.edu.vn.documentsystem.service.impl;

import com.hust.edu.vn.documentsystem.entity.FavoritePost;
import com.hust.edu.vn.documentsystem.entity.Post;
import com.hust.edu.vn.documentsystem.entity.User;
import com.hust.edu.vn.documentsystem.repository.FavoritePostRepository;
import com.hust.edu.vn.documentsystem.repository.PostRepository;
import com.hust.edu.vn.documentsystem.repository.UserRepository;
import com.hust.edu.vn.documentsystem.service.FavoritePostService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class FavoritePostServiceImpl implements FavoritePostService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final FavoritePostRepository favoritePostRepository;

    public FavoritePostServiceImpl(UserRepository userRepository,
                                   PostRepository postRepository,
                                   FavoritePostRepository favoritePostRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.favoritePostRepository = favoritePostRepository;
    }

    @Override
    public boolean toggleFavoritePost(Long postId) {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        Post post = postRepository.findById(postId).orElse(null);
        if(post == null ) return false;
        FavoritePost favoritePost = favoritePostRepository.findByPostAndUser(post, user);
        if(favoritePost != null){
            favoritePostRepository.delete(favoritePost);
            return true;
        }
        favoritePost = new FavoritePost();
        favoritePost.setUser(user);
        favoritePost.setPost(post);
        favoritePostRepository.save(favoritePost);
        return true;
    }
}
