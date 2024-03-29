package com.hust.edu.vn.documentsystem.service.impl;

import com.hust.edu.vn.documentsystem.common.type.RoleType;
import com.hust.edu.vn.documentsystem.common.type.TargetLanguageType;
import com.hust.edu.vn.documentsystem.data.dto.PageDto;
import com.hust.edu.vn.documentsystem.data.dto.PostDto;
import com.hust.edu.vn.documentsystem.data.model.CommentPostModel;
import com.hust.edu.vn.documentsystem.data.model.PostModel;
import com.hust.edu.vn.documentsystem.dto.PostInfoDto;
import com.hust.edu.vn.documentsystem.entity.*;
import com.hust.edu.vn.documentsystem.repository.*;
import com.hust.edu.vn.documentsystem.service.DocumentService;
import com.hust.edu.vn.documentsystem.service.GoogleCloudTranslateService;
import com.hust.edu.vn.documentsystem.service.PostService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import jakarta.validation.constraints.NotNull;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ModelMapperUtils modelMapperUtils;
    private final GoogleCloudTranslateService googleCloudTranslateService;
    private final SubjectRepository subjectRepository;
    private final AnswerPostRepository answerPostRepository;
    private final CommentPostRepository commentPostRepository;
    private final DocumentService documentService;


    public PostServiceImpl(
            PostRepository postRepository,
            UserRepository userRepository,
            ModelMapperUtils modelMapperUtils,
            GoogleCloudTranslateService googleCloudTranslateService,
            SubjectRepository subjectRepository,
            AnswerPostRepository answerPostRepository,
            CommentPostRepository commentPostRepository,
            DocumentService documentService) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.modelMapperUtils = modelMapperUtils;
        this.googleCloudTranslateService = googleCloudTranslateService;

        this.subjectRepository = subjectRepository;
        this.answerPostRepository = answerPostRepository;
        this.commentPostRepository = commentPostRepository;
        this.documentService = documentService;
    }

    @Override
    public PageDto<PostInfoDto> getAllPostForHomePage(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Post> pages = postRepository.findAll(pageRequest);
        List<PostInfoDto> posts = postRepository.getPostForHomePage(SecurityContextHolder.getContext().getAuthentication().getName(),pageRequest);
        PageDto<PostInfoDto> pageResult = new PageDto<>();
        pageResult.setTotalPages(pages.getTotalPages());
        pageResult.setTotalItems(pages.getTotalElements());
        pageResult.setItems(posts);
        return pageResult;
    }

    @Override
    public Post getPostById(@NotNull Long id) {
        return postRepository.findById(id).orElse(null);
    }

    @Override
    public Post createPost(PostModel postModel) {
        Subject subject = subjectRepository.findById(postModel.getSubjectId()).orElse(null);
        if (subject == null || postModel.getDocuments().length < 1 || postModel.getDescription() == null
                || postModel.getDescription().length() < 1)
            return null;
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        Post post = modelMapperUtils.mapAllProperties(postModel, Post.class);
        post.setDone(postModel.getDone() == 1);
        post.setOwner(user);
        post.setSubject(subject);
        try {
            Document documentEntity = documentService.savePublicDocumentToGoogleCloud(postModel.getDocuments());
            post.setDocument(documentEntity);
            Post response = postRepository.save(post);
            return response;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CommentPost createComment(Long postId, CommentPostModel commentPostModel) {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        Post post = postRepository.findById(postId).orElse(null);
        if (post == null || user == null)
            return null;
        CommentPost commentPost = new CommentPost();
        commentPost.setComment(commentPostModel.getComment());
        commentPost.setPost(post);
        commentPost.setOwner(user);
        if (commentPostModel.getParentCommentId() != null) {
            CommentPost parent = commentPostRepository.findById(commentPostModel.getParentCommentId()).orElse(null);
            if (parent == null)
                return null;
            commentPost.setParentComment(parent);
        }
        commentPostRepository.save(commentPost);
        return commentPost;
    }

    @Override
    public boolean deleteCommentForPost(@NotNull Long commentId, Long postId) {
        CommentPost comment = commentPostRepository.findByIdAndPostId(commentId, postId);
        if (comment == null || !comment.getOwner().getEmail()
                .equals(SecurityContextHolder.getContext().getAuthentication().getName()))
            return false;
        commentPostRepository.delete(comment);
        return true;
    }

    @Override
    public boolean hiddenCommentForPost(Long commentId, Long postId) {
        CommentPost comment = commentPostRepository.findByIdAndPostIdAndIsHidden(commentId, postId,
                SecurityContextHolder.getContext().getAuthentication().getName(), false);
        if (comment == null)
            return false;
        comment.setHidden(true);
        commentPostRepository.save(comment);
        return true;
    }

    @Override
    public boolean hiddenAnswerForPost(Long id) {
        AnswerPost answerPost = answerPostRepository.findByIdAndIsHidden(id, false);
        if (answerPost == null || answerPost.getOwner().getEmail()
                .equals(SecurityContextHolder.getContext().getAuthentication().getName()))
            return false;
        answerPost.setHidden(true);
        answerPostRepository.save(answerPost);
        return true;
    }

    @Override
    public boolean hiddenPost(Long id) {
        Post post = postRepository.findByIdAndIsDone(id, true);
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if (post == null || user.getRoleType() != RoleType.ADMIN)
            return false;
        post.setHidden(true);
        postRepository.save(post);
        return true;
    }

    @Override
    public boolean activePost(Long id) {
        Post post = postRepository.findByIdAndIsHidden(id, true);
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if (post == null || user.getRoleType() != RoleType.ADMIN || !post.isHidden())
            return false;
        post.setHidden(false);
        postRepository.save(post);
        return true;
    }

    @Override
    public boolean activeAnswerForPost(Long id) {
        AnswerPost answerPost = answerPostRepository.findByIdAndIsHidden(id, true);
        if (answerPost == null || answerPost.getOwner().getEmail()
                .equals(SecurityContextHolder.getContext().getAuthentication().getName()))
            return false;
        answerPost.setHidden(false);
        answerPostRepository.save(answerPost);
        return true;
    }

    @Override
    public List<CommentPost> getAllCommentForPost(Long postId) {
        return commentPostRepository.findAllComment(postId);
    }

    @Override
    public CommentPost updateCommentForPost(Long commentId, Long postId, CommentPostModel commentPostModel) {
        CommentPost commentPost = commentPostRepository.findByIdAndPostIdAndUserEmail(commentId, postId,
                SecurityContextHolder.getContext().getAuthentication().getName());
        if (commentPost == null)
            return null;
        commentPost.setComment(commentPostModel.getComment());
        return commentPostRepository.save(commentPost);
    }

    @Override
    public List<Object> translatePost(Long postId, TargetLanguageType targetLanguage) {
        Post post = postRepository.findById(postId).orElse(null);
        if (post == null)
            return null;
        String url = googleCloudTranslateService.translatePost(post.getDocument(), targetLanguage);
        return List.of(post.getDocument(), url);
    }

    @Override
    public List<Post> getAllPostCreatedByUser() {
        return postRepository
                .findAllPostCreatedByUser(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Override
    public boolean deletePost(Long postId) {
        Post post = postRepository.findByIdAndUserEmail(postId,
                SecurityContextHolder.getContext().getAuthentication().getName());
        if (post == null)
            return false;
        postRepository.delete(post);
        return true;
    }

    @Override
    public List<Object[]> getPostForDashboard(Date sevenDaysAgo) {
        return postRepository.getPostForDashboard(sevenDaysAgo);
    }

    @Override
    public boolean updatePost(Long postId, PostModel postModel) {
        Post post = postRepository.findByIdAndUserEmail(postId, SecurityContextHolder.getContext().getAuthentication().getName());
        if (post == null) return false;
        post.setDescription(postModel.getDescription());
        post.setDone(postModel.getDone() == 1);
        if (postModel.getDocuments().length > 0) {
            Document documentEntity;
            try {
                documentEntity = documentService.savePublicDocumentToGoogleCloud(postModel.getDocuments());
                post.setDocument(documentEntity);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        postRepository.save(post);
        return true;
    }
}
