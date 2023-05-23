package com.hust.edu.vn.documentsystem.service.impl;

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
import com.hust.edu.vn.documentsystem.service.GoogleCloudStorageService;
import com.hust.edu.vn.documentsystem.service.GoogleCloudTranslateService;
import com.hust.edu.vn.documentsystem.service.PostService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class PostServiceImpl implements PostService {
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
            ApplicationEventPublisher applicationEventPublisher
    ) {
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
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findByIsDone(true);
    }

    @Override
    public List<Post> getAllPostsCreateByUser() {
        return postRepository.findByOwner(userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()));
    }

    @Override
    public Post getPostById(@NotNull Long id) {
        return postRepository.findById(id).orElse(null);
    }

    @Override
    public Post createPost(PostModel postModel) {
        if (postModel.getContent() == null && (postModel.getDocuments().length < 1 || postModel.getDescription() == null || postModel.getDescription().length() < 1))
            return null;
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        Post post = modelMapperUtils.mapAllProperties(postModel, Post.class);
        post.setDone(postModel.getDone() == 1);
        post.setOwner(user);
        post.setSubject(subjectRepository.findById(postModel.getSubjectId()).orElse(null));
        if (postModel.getDocuments() != null && postModel.getDocuments().length > 0) {
/*
            try {
                String path = googleCloudStorageService.uploadDocumentsToGCP(postModel.getDocuments(), user.getRootPath());
*/
            Document document = new Document();
                document.setName(postModel.getDocuments()[0].getOriginalFilename());
                document.setContentType(postModel.getDocuments()[0].getContentType());
                document.setPath("path");
                document.setType(DocumentType.getDocumentTypeFromExtension(postModel.getDocuments()[0].getOriginalFilename().substring(postModel.getDocuments()[0].getOriginalFilename().lastIndexOf("."))));
                documentRepository.save(document);
                post.setDocument(document);
/*
            } catch (IOException e) {
                return null;
            }
*/
        }
        Post response = postRepository.save(post);
        applicationEventPublisher.publishEvent(new NotifyEvent(NotificationType.NEW_POST, post));
        return response;
    }

    @Override
    public boolean updatePost(PostModel postModel) {
        Post post = postRepository.findById(postModel.getId()).orElse(null);
        if (post == null) return false;
        if (post.getDescription() == null || !post.getDescription().equalsIgnoreCase(postModel.getDescription())) {
            post.setDescription(postModel.getDescription());
            post.setDescriptionEn(googleCloudTranslateService.translateText(postModel.getDescription(), TargetLanguageType.ENGLISH).get(0));
        }
        if (post.getContent() == null || !post.getContent().equalsIgnoreCase(postModel.getContent())) {
            post.setContent(postModel.getContent());
            post.setContentEn(googleCloudTranslateService.translateText(postModel.getContent(), TargetLanguageType.ENGLISH).get(0));
        }
        post.setSubject(subjectRepository.findById(postModel.getSubjectId()).orElse(null));

        if (postModel.getDocuments().length > 0) {
            User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
            boolean status = googleCloudStorageService.deleteDocumentByRootPath(user.getRootPath() + "documents/" + post.getDocument().getPath());
            if (!status) return false;
            try {
                String path = googleCloudStorageService.uploadDocumentsToGCP(postModel.getDocuments(), user.getRootPath());
                Document document = post.getDocument();
                if (path == null) return false;
                document.setPath(path);
                documentRepository.save(document);
            } catch (IOException e) {
                return false;
            }
        }
        postRepository.save(post);
        applicationEventPublisher.publishEvent(new NotifyEvent(NotificationType.EDIT_POST, post));
        return true;
    }

    @Override
    public boolean deletePost(@NotNull Long postId) {
        Post post = postRepository.findById(postId).orElse(null);
        if (post == null || !post.getOwner().getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName()))
            return false;
        postRepository.delete(post);
        applicationEventPublisher.publishEvent(new NotifyEvent(NotificationType.DELETE_POST, post));
        return true;
    }

    @Override
    public boolean uploadAnswer(AnswerPostModel answerPostModel) {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        Post post = postRepository.findById(answerPostModel.getPostId()).orElse(null);
        if (post == null || user == null) return false;
        AnswerPost answerPost = modelMapperUtils.mapAllProperties(answerPostModel, AnswerPost.class);
        answerPost.setPost(post);
        answerPost.setOwner(user);
        if (answerPostModel.getContent() != null && answerPostModel.getContent().length() > 0) {
            answerPost.setContent(answerPostModel.getContent());
            answerPost.setContentEn(googleCloudTranslateService.translateText(answerPostModel.getContent(), TargetLanguageType.ENGLISH).get(0));
        }
        if (answerPostModel.getAnswerFile().length > 0) {
            try {
                String path = googleCloudStorageService.uploadDocumentsToGCP(answerPostModel.getAnswerFile(), user.getRootPath());
                Document document = new Document();
                document.setName(answerPostModel.getAnswerFile()[0].getOriginalFilename());
                document.setContentType(answerPostModel.getAnswerFile()[0].getContentType());
                document.setPath(path);
                document.setType(DocumentType.getDocumentTypeFromExtension(answerPostModel.getAnswerFile()[0].getOriginalFilename().substring(answerPostModel.getAnswerFile()[0].getOriginalFilename().lastIndexOf("."))));
                documentRepository.save(document);
                answerPost.setDocument(document);
            } catch (IOException e) {
                return false;
            }
        }
        answerPostRepository.save(answerPost);
        applicationEventPublisher.publishEvent(new NotifyEvent(NotificationType.NEW_ANSWER, answerPost));
        return true;
    }

    @Override
    public boolean createComment(CommentPostModel commentPostModel) {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        Post post = postRepository.findById(commentPostModel.getPostId()).orElse(null);
        if (post == null || user == null) return false;
        CommentPost commentPost = modelMapperUtils.mapAllProperties(commentPostModel, CommentPost.class);
        commentPost.setPost(post);
        commentPost.setOwner(user);
        commentPostRepository.save(commentPost);
        applicationEventPublisher.publishEvent(new NotifyEvent(NotificationType.NEW_COMMENT_POST, commentPost));
        return true;
    }

    @Override
    public boolean updateCommentForPost(CommentPostModel commentPostModel) {
        if (commentPostModel.getId() == null) return false;
        CommentPost comment = commentPostRepository.findById(commentPostModel.getId()).orElse(null);
        if (comment == null || !comment.getOwner().getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName()))
            return false;
        comment.setComment(commentPostModel.getComment());
        commentPostRepository.save(comment);
        applicationEventPublisher.publishEvent(new NotifyEvent(NotificationType.EDIT_COMMENT_POST, comment));
        return true;
    }

    @Override
    public boolean deleteCommentForPost(@NotNull Long id) {
        CommentPost comment = commentPostRepository.findById(id).orElse(null);
        if (comment == null || !comment.getOwner().getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName()))
            return false;
        commentPostRepository.delete(comment);
        applicationEventPublisher.publishEvent(new NotifyEvent(NotificationType.EDIT_COMMENT_POST, comment));
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
}
