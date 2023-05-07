package com.hust.edu.vn.documentsystem.service;

import com.hust.edu.vn.documentsystem.data.model.AnswerPostModel;
import com.hust.edu.vn.documentsystem.data.model.CommentPostModel;
import com.hust.edu.vn.documentsystem.data.model.PostModel;
import com.hust.edu.vn.documentsystem.entity.Post;

import java.util.List;

public interface PostService {
    List<Post> getAllPosts();

    List<Post> getAllPostsCreateByUser();

    Post getPostById(Long id);

    Post createPost(PostModel postModel);

    boolean updatePost(PostModel postModel);

    boolean deletePost(Long postId);

    boolean uploadAnswer(AnswerPostModel answerPostModel);

    boolean createComment(CommentPostModel commentPostModel);

    boolean updateCommentForPost(CommentPostModel commentPostModel);

    boolean deleteCommentForPost(Long id);

    boolean hiddenCommentForPost(Long id);

    boolean hiddenAnswerForPost(Long id);

    boolean hiddenPost(Long id);

    boolean activeCommentForPost(Long id);

    boolean activePost(Long id);

    boolean activeAnswerForPost(Long id);
}
