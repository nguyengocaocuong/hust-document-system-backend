package com.hust.edu.vn.documentsystem.service;

import com.hust.edu.vn.documentsystem.common.type.TargetLanguageType;
import com.hust.edu.vn.documentsystem.data.dto.PageDto;
import com.hust.edu.vn.documentsystem.data.dto.PostDto;
import com.hust.edu.vn.documentsystem.data.model.AnswerPostModel;
import com.hust.edu.vn.documentsystem.data.model.CommentPostModel;
import com.hust.edu.vn.documentsystem.data.model.PostModel;
import com.hust.edu.vn.documentsystem.entity.AnswerPost;
import com.hust.edu.vn.documentsystem.entity.CommentPost;
import com.hust.edu.vn.documentsystem.entity.Post;

import java.util.Date;
import java.util.List;

public interface PostService {
    PageDto<PostDto> getAllPostForHomePage(int page, int size);

    Post getPostById(Long id);

    Post createPost(PostModel postModel);

    CommentPost createComment(Long postId, CommentPostModel commentPostModel);

    boolean deleteCommentForPost(Long commentId, Long postId);

    boolean hiddenCommentForPost(Long commentId, Long postId);

    boolean hiddenAnswerForPost(Long id);

    boolean hiddenPost(Long id);

    boolean activePost(Long id);

    boolean activeAnswerForPost(Long id);

    List<CommentPost> getAllCommentForPost(Long postId);

    CommentPost updateCommentForPost(Long commentId, Long postId, CommentPostModel commentPostModel);

    List<Object> translatePost(Long postId, TargetLanguageType targetLanguageType);

    List<Post> getAllPostCreatedByUser();

    boolean deletePost(Long postId);

    List<Object[]> getPostForDashboard(Date sevenDaysAgo);
}
