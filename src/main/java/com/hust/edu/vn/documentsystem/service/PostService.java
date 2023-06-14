package com.hust.edu.vn.documentsystem.service;

import com.hust.edu.vn.documentsystem.common.type.TargetLanguageType;
import com.hust.edu.vn.documentsystem.data.model.AnswerPostModel;
import com.hust.edu.vn.documentsystem.data.model.CommentPostModel;
import com.hust.edu.vn.documentsystem.data.model.PostModel;
import com.hust.edu.vn.documentsystem.entity.AnswerPost;
import com.hust.edu.vn.documentsystem.entity.CommentPost;
import com.hust.edu.vn.documentsystem.entity.FavoriteAnswerPost;
import com.hust.edu.vn.documentsystem.entity.Post;

import java.util.Date;
import java.util.List;

public interface PostService {
    List<Object[]> getAllPosts();

    Post getPostById(Long id);

    Post createPost(PostModel postModel);

    CommentPost createComment(Long postId, CommentPostModel commentPostModel);

    boolean deleteCommentForPost(Long id);

    boolean hiddenCommentForPost(Long id);

    boolean hiddenAnswerForPost(Long id);

    boolean hiddenPost(Long id);

    boolean activeCommentForPost(Long id);

    boolean activePost(Long id);

    boolean activeAnswerForPost(Long id);

    List<CommentPost> getAllCommentForPost(Long postId);

    CommentPost updateCommentForPost(Long commentId, CommentPostModel commentPostModel);

    List<AnswerPost> findAllAnswerForPost(Long postId);

    AnswerPost createAnswerForPost(Long postId, AnswerPostModel answerPostModel);

    boolean toggleFavoriteAnswerPost(Long answerId);

    List<FavoriteAnswerPost> getAllFavoriteForAnswer(Long answerId);

    List<Object> translatePost(Long postId, TargetLanguageType targetLanguageType);

    List<Object> readAnswerPost(Long answerId);

    List<Post> getAllPostCreatedByUser();

    boolean deletePost(Long postId);

    List<Object[]> getPostForDashboard(Date sevenDaysAgo);
}
