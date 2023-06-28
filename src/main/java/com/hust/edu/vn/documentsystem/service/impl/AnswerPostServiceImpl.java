package com.hust.edu.vn.documentsystem.service.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.hust.edu.vn.documentsystem.common.type.DocumentType;
import com.hust.edu.vn.documentsystem.data.model.AnswerPostModel;
import com.hust.edu.vn.documentsystem.entity.AnswerPost;
import com.hust.edu.vn.documentsystem.entity.Document;
import com.hust.edu.vn.documentsystem.entity.Post;
import com.hust.edu.vn.documentsystem.repository.AnswerPostRepository;
import com.hust.edu.vn.documentsystem.repository.DocumentRepository;
import com.hust.edu.vn.documentsystem.repository.PostRepository;
import com.hust.edu.vn.documentsystem.repository.UserRepository;
import com.hust.edu.vn.documentsystem.service.AnswerPostService;
import com.hust.edu.vn.documentsystem.service.DocumentService;
import com.hust.edu.vn.documentsystem.service.GoogleCloudStorageService;

@Service
public class AnswerPostServiceImpl implements AnswerPostService {
    private final PostRepository postRepository;
    private final DocumentService documentService;
    private final DocumentRepository documentRepository;
    private final AnswerPostRepository answerPostRepository;
    private final UserRepository userRepository;
    private final GoogleCloudStorageService googleCloudStorageService;

    public AnswerPostServiceImpl(PostRepository postRepository, DocumentService documentService,
            DocumentRepository documentRepository, AnswerPostRepository answerPostRepository,
            UserRepository userRepository, GoogleCloudStorageService googleCloudStorageService) {
        this.postRepository = postRepository;
        this.documentService = documentService;
        this.documentRepository = documentRepository;
        this.answerPostRepository = answerPostRepository;
        this.userRepository = userRepository;
        this.googleCloudStorageService = googleCloudStorageService;
    }

    @Override
    public AnswerPost createAnswerForPost(Long postId, AnswerPostModel answerPostModel) {
        Post post = postRepository.findById(postId).orElse(null);
        if (post == null)
            return null;
        try {
            AnswerPost answerPost = new AnswerPost();
            answerPost.setPost(post);
            Document document;

            if (answerPostModel.getType() != DocumentType.LINK) {
                document = documentService.savePublicDocumentToGoogleCloud(answerPostModel.getDocuments());
            } else {
                document = new Document();
                document.setUrl(answerPostModel.getUrl());
                answerPost.setType(DocumentType.LINK);
                document.setContentType(MediaType.ALL.getType());
                document = documentRepository.save(document);
            }
            answerPost.setDescription(answerPostModel.getDescription());
            answerPost.setDocument(document);
            answerPost.setOwner(
                    userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()));
            return answerPostRepository.save(answerPost);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<AnswerPost> findAllAnswerForPost(Long postId) {
        return answerPostRepository.findAllAnswerForPost(postId);
    }

    @Override
    public List<Object> readAnswerPost(Long answerId, Long postId) {
        AnswerPost answerPost = answerPostRepository.findByIdAndPostId(answerId, postId);
        if (answerPost == null || answerPost.getType() == DocumentType.LINK)
            return null;
        String path = answerPost.getDocument().getPath().split(System.getenv("BUCKET_NAME") + "/")[1];
        byte[] data = googleCloudStorageService.readBlobByPath(path);
        return List.of(answerPost.getDocument(), data);
    }

    @Override
    public boolean deleteAnswerForPost(Long answerId, Long postId) {
        AnswerPost answerPost = answerPostRepository.findByIdAndPostIdAndOnwerEmail(answerId, postId,
                SecurityContextHolder.getContext().getAuthentication().getName());
        if (answerPost == null)
            return false;
        answerPostRepository.delete(answerPost);
        return true;
    }

}
