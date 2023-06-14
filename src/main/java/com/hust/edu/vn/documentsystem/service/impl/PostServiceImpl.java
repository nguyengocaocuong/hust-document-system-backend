package com.hust.edu.vn.documentsystem.service.impl;

import co.elastic.thumbnails4j.core.ThumbnailingException;
import com.hust.edu.vn.documentsystem.common.type.DocumentType;
import com.hust.edu.vn.documentsystem.common.type.NotificationType;
import com.hust.edu.vn.documentsystem.common.type.RoleType;
import com.hust.edu.vn.documentsystem.common.type.TargetLanguageType;
import com.hust.edu.vn.documentsystem.data.model.AnswerPostModel;
import com.hust.edu.vn.documentsystem.data.model.CommentPostModel;
import com.hust.edu.vn.documentsystem.data.model.PostModel;
import com.hust.edu.vn.documentsystem.entity.*;
import com.hust.edu.vn.documentsystem.event.NotifyEvent;
import com.hust.edu.vn.documentsystem.repository.*;
import com.hust.edu.vn.documentsystem.service.DocumentService;
import com.hust.edu.vn.documentsystem.service.GoogleCloudStorageService;
import com.hust.edu.vn.documentsystem.service.GoogleCloudTranslateService;
import com.hust.edu.vn.documentsystem.service.PostService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import com.itextpdf.text.DocumentException;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class PostServiceImpl implements PostService {
    private final FavoriteAnswerPostRepository favoriteAnswerPostRepository;
    private final FavoritePostRepository favoritePostRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ModelMapperUtils modelMapperUtils;
    private final GoogleCloudTranslateService googleCloudTranslateService;
    private final GoogleCloudStorageService googleCloudStorageService;
    private final DocumentRepository documentRepository;
    private final SubjectRepository subjectRepository;
    private final AnswerPostRepository answerPostRepository;
    private final CommentPostRepository commentPostRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final DocumentService documentService;

    @Autowired
    public PostServiceImpl(
            PostRepository postRepository,
            UserRepository userRepository,
            ModelMapperUtils modelMapperUtils,
            GoogleCloudTranslateService googleCloudTranslateService,
            GoogleCloudStorageService googleCloudStorageService,
            DocumentRepository documentRepository,
            SubjectRepository subjectRepository,
            AnswerPostRepository answerPostRepository,
            CommentPostRepository commentPostRepository,
            ApplicationEventPublisher applicationEventPublisher,
            DocumentService documentService,
            FavoritePostRepository favoritePostRepository,
            FavoriteAnswerPostRepository favoriteAnswerPostRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.modelMapperUtils = modelMapperUtils;
        this.googleCloudTranslateService = googleCloudTranslateService;
        this.googleCloudStorageService = googleCloudStorageService;
        this.documentRepository = documentRepository;
        this.subjectRepository = subjectRepository;
        this.answerPostRepository = answerPostRepository;
        this.commentPostRepository = commentPostRepository;
        this.applicationEventPublisher = applicationEventPublisher;
        this.documentService = documentService;
        this.favoritePostRepository = favoritePostRepository;
        this.favoriteAnswerPostRepository = favoriteAnswerPostRepository;
    }

    @Override
    public List<Object[]> getAllPosts() {
        return postRepository.getPostForHomePage();
    }


    @Override
    public Post getPostById(@NotNull Long id) {
        return postRepository.findById(id).orElse(null);
    }

    @Override
    public Post createPost(PostModel postModel) {
        Subject subject = subjectRepository.findById(postModel.getSubjectId()).orElse(null);
        if (subject == null || postModel.getDocuments().length < 1 || postModel.getDescription() == null || postModel.getDescription().length() < 1)
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
        if (post == null || user == null) return null;
        CommentPost commentPost = new CommentPost();
        commentPost.setComment(commentPostModel.getComment());
        commentPost.setPost(post);
        commentPost.setOwner(user);
        if (commentPostModel.getParentCommentId() != null) {
            CommentPost parent = commentPostRepository.findById(commentPostModel.getParentCommentId()).orElse(null);
            if (parent == null) return null;
            commentPost.setParentComment(parent);
        }
        commentPostRepository.save(commentPost);
        return commentPost;
    }


    @Override
    public boolean deleteCommentForPost(@NotNull Long id) {
        CommentPost comment = commentPostRepository.findById(id).orElse(null);
        if (comment == null || !comment.getOwner().getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName()))
            return false;
        commentPostRepository.delete(comment);
        return true;
    }

    @Override
    public boolean hiddenCommentForPost(Long id) {
        CommentPost comment = commentPostRepository.findByIdAndIsHidden(id, true);
        if (comment == null || comment.getOwner().getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName()))
            return false;
        comment.setHidden(true);
        commentPostRepository.save(comment);
        applicationEventPublisher.publishEvent(new NotifyEvent(NotificationType.HIDDEN_COMMENT_PORT, comment));
        return true;
    }

    @Override
    public boolean hiddenAnswerForPost(Long id) {
        AnswerPost answerPost = answerPostRepository.findByIdAndIsHidden(id, false);
        if (answerPost == null || answerPost.getOwner().getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName()))
            return false;
        answerPost.setHidden(true);
        answerPostRepository.save(answerPost);
        applicationEventPublisher.publishEvent(new NotifyEvent(NotificationType.HIDDEN_ANSWER_PORT, answerPost));
        return true;
    }

    @Override
    public boolean hiddenPost(Long id) {
        Post post = postRepository.findByIdAndIsDone(id, true);
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if (post == null || user.getRoleType() != RoleType.ADMIN) return false;
        post.setHidden(true);
        postRepository.save(post);
        applicationEventPublisher.publishEvent(new NotifyEvent(NotificationType.HIDDEN_PORT, post));
        return true;
    }

    @Override
    public boolean activeCommentForPost(Long id) {
        CommentPost comment = commentPostRepository.findByIdAndIsHidden(id, true);
        if (comment == null || !comment.getPost().getOwner().getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName()) || !comment.isHidden())
            return false;
        comment.setHidden(false);
        commentPostRepository.save(comment);
        applicationEventPublisher.publishEvent(new NotifyEvent(NotificationType.ACTIVE_COMMENT_PORT, comment));
        return true;
    }

    @Override
    public boolean activePost(Long id) {
        Post post = postRepository.findByIdAndIsHidden(id, true);
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if (post == null || user.getRoleType() != RoleType.ADMIN || !post.isHidden()) return false;
        post.setHidden(false);
        postRepository.save(post);
        applicationEventPublisher.publishEvent(new NotifyEvent(NotificationType.ACTIVE_PORT, post));
        return true;
    }

    @Override
    public boolean activeAnswerForPost(Long id) {
        AnswerPost answerPost = answerPostRepository.findByIdAndIsHidden(id, true);
        if (answerPost == null || answerPost.getOwner().getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName()))
            return false;
        answerPost.setHidden(false);
        answerPostRepository.save(answerPost);
        applicationEventPublisher.publishEvent(new NotifyEvent(NotificationType.HIDDEN_ANSWER_PORT, answerPost));
        return true;
    }

    @Override
    public List<CommentPost> getAllCommentForPost(Long postId) {
        return commentPostRepository.findAllComment(postId, SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Override
    public CommentPost updateCommentForPost(Long commentId, CommentPostModel commentPostModel) {
        CommentPost commentPost = commentPostRepository.findByIdAndUserEmail(commentId, SecurityContextHolder.getContext().getAuthentication().getName());
        if (commentPost == null) return null;
        commentPost.setComment(commentPostModel.getComment());
        return commentPostRepository.save(commentPost);
    }

    @Override
    public List<AnswerPost> findAllAnswerForPost(Long postId) {
        return answerPostRepository.findAllAnswerForPost(postId);
    }

    @Override
    public AnswerPost createAnswerForPost(Long postId, AnswerPostModel answerPostModel) {
        Post post  = postRepository.findById(postId).orElse(null);
        if(post == null) return null;
        try {
            AnswerPost answerPost = new AnswerPost();
            answerPost.setPost(post);
            Document document;

            if(answerPostModel.getType() != DocumentType.LINK ){
                document = documentService.savePublicDocumentToGoogleCloud(answerPostModel.getDocuments());
            }else{
                document = new Document();
                document.setUrl(answerPostModel.getUrl());
                answerPost.setType(DocumentType.LINK);
                document.setContentType(MediaType.ALL.getType());
                document = documentRepository.save(document);
            }
            answerPost.setDescription(answerPostModel.getDescription());
            answerPost.setDocument(document);
            answerPost.setOwner(userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()));
            return answerPostRepository.save(answerPost);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean toggleFavoriteAnswerPost(Long answerId) {
        AnswerPost answerPost = answerPostRepository.findById(answerId).orElse(null);
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if (answerPost == null) return false;
        FavoriteAnswerPost favoriteAnswerPost = favoriteAnswerPostRepository.findByIdAndUser(answerId, user);
        if(favoriteAnswerPost != null){
            favoriteAnswerPostRepository.delete(favoriteAnswerPost);
            return true;
        }
        favoriteAnswerPost = new FavoriteAnswerPost();
        favoriteAnswerPost.setAnswerPost(answerPost);
        favoriteAnswerPost.setUser(user);
        favoriteAnswerPostRepository.save(favoriteAnswerPost);
        return true;
    }

    @Override
    public List<FavoriteAnswerPost> getAllFavoriteForAnswer(Long answerId) {
        return favoriteAnswerPostRepository.findAllByAnswerId(answerId);
    }

    @Override
    public List<Object> translatePost(Long postId, TargetLanguageType targetLanguage) {
        Post post = postRepository.findById(postId).orElse(null);
        if(post == null) return null;
        String url = googleCloudTranslateService.translatePost(post.getDocument(), targetLanguage);
        return List.of(post.getDocument(), url);
    }

    @Override
    public List<Object> readAnswerPost(Long answerId) {
        AnswerPost answerPost = answerPostRepository.findById(answerId).orElse(null);
        if(answerPost == null || answerPost.getType() == DocumentType.LINK) return null;
        String path = answerPost.getDocument().getPath().split(System.getenv("BUCKET_NAME") + "/")[1];
        byte[] data = googleCloudStorageService.readBlobByPath(path);
        return List.of(answerPost.getDocument(), data);
    }

    @Override
    public List<Post> getAllPostCreatedByUser() {
        return postRepository.findAllPostCreatedByUser(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Override
    public boolean deletePost(Long postId) {
        Post post = postRepository.findByIdAndUserEmail(postId, SecurityContextHolder.getContext().getAuthentication().getName());
        if(post == null) return false;
        postRepository.delete(post);
        return true;
    }

    @Override
    public List<Object[]> getPostForDashboard(Date sevenDaysAgo) {
        return postRepository.getPostForDashboard(sevenDaysAgo);
    }
}
