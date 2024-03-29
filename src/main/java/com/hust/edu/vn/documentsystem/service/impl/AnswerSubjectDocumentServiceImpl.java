package com.hust.edu.vn.documentsystem.service.impl;

import co.elastic.thumbnails4j.core.ThumbnailingException;
import com.hust.edu.vn.documentsystem.common.type.DocumentType;
import com.hust.edu.vn.documentsystem.data.model.AnswerSubjectDocumentModel;
import com.hust.edu.vn.documentsystem.entity.*;
import com.hust.edu.vn.documentsystem.repository.AnswerSubjectDocumentRepository;
import com.hust.edu.vn.documentsystem.repository.DocumentRepository;
import com.hust.edu.vn.documentsystem.repository.SubjectDocumentRepository;
import com.hust.edu.vn.documentsystem.repository.UserRepository;
import com.hust.edu.vn.documentsystem.service.AnswerSubjectDocumentService;
import com.hust.edu.vn.documentsystem.service.DocumentService;
import com.hust.edu.vn.documentsystem.service.GoogleCloudStorageService;
import com.itextpdf.text.DocumentException;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class AnswerSubjectDocumentServiceImpl implements AnswerSubjectDocumentService {
    private final SubjectDocumentRepository subjectDocumentRepository;
    private final UserRepository userRepository;
    private final DocumentRepository documentRepository;

    private final DocumentService documentService;
    private final AnswerSubjectDocumentRepository answerSubjectDocumentRepository;
    private final GoogleCloudStorageService googleCloudStorageService;


    public AnswerSubjectDocumentServiceImpl(SubjectDocumentRepository subjectDocumentRepository,
                                            UserRepository userRepository,
                                            DocumentRepository documentRepository, DocumentService documentService,
                                            AnswerSubjectDocumentRepository answerSubjectDocumentRepository, GoogleCloudStorageService googleCloudStorageService) {
        this.subjectDocumentRepository = subjectDocumentRepository;
        this.userRepository = userRepository;
        this.documentRepository = documentRepository;
        this.documentService = documentService;
        this.answerSubjectDocumentRepository = answerSubjectDocumentRepository;
        this.googleCloudStorageService = googleCloudStorageService;
    }

    @Override
    public AnswerSubjectDocument saveAnswerForSubjectDocument(Long subjectDocumentId,
                                                              AnswerSubjectDocumentModel answerSubjectDocumentModel) {
        SubjectDocument subjectDocument = subjectDocumentRepository.findById(subjectDocumentId).orElse(null);
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if (subjectDocument == null || (answerSubjectDocumentModel.getDescription().isEmpty()
                && answerSubjectDocumentModel.getDocuments().length == 0))
            return null;
        try {
            AnswerSubjectDocument answerSubjectDocument = new AnswerSubjectDocument();
            Document document;
            if (answerSubjectDocumentModel.getType() == DocumentType.LINK) {
                document = new Document();
                document.setUrl(answerSubjectDocumentModel.getUrl());
                answerSubjectDocument.setType(DocumentType.LINK);
                document.setContentType(MediaType.ALL.getType());
                document = documentRepository.save(document);
            } else {
                document = documentService.savePrivateDocumentToGoogleCloud(answerSubjectDocumentModel.getDocuments());
            }
            answerSubjectDocument.setDescription(answerSubjectDocumentModel.getDescription());
            answerSubjectDocument.setDocument(document);
            answerSubjectDocument.setSubjectDocument(subjectDocument);
            answerSubjectDocument.setOwner(user);
            answerSubjectDocument.setType(answerSubjectDocumentModel.getType());
            answerSubjectDocumentRepository.save(answerSubjectDocument);
            return answerSubjectDocument;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        } catch (ThumbnailingException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<AnswerSubjectDocument> getAllAnswerSubjectDocument(Long subjectDocumentId) {
        return answerSubjectDocumentRepository.findAllBySubjectDocumentId(subjectDocumentId);
    }

    @Override
    public List<Object> readAnswerSubjectDocument(Long answerSubjectDocumentId, Long subjectDocumentId) {
        AnswerSubjectDocument answerPost = answerSubjectDocumentRepository.findByIdAndSubjectDocumentId(answerSubjectDocumentId, subjectDocumentId);
        if (answerPost == null || answerPost.getType() == DocumentType.LINK)
            return null;
        byte[] data = googleCloudStorageService.readBlobByPath(answerPost.getDocument().getPath());
        return List.of(answerPost.getDocument(), data);
    }

    @Override
    public AnswerSubjectDocument createAnnotationForSubjectDocument(Long subjectDocumentId, AnswerSubjectDocumentModel answerSubjectDocumentModel) throws IOException {
        if (answerSubjectDocumentModel.getDocuments() == null ||  answerSubjectDocumentModel.getDocuments().length == 0) return null;
        SubjectDocument subjectDocument = subjectDocumentRepository.findById(subjectDocumentId).orElse(null);
        if (subjectDocument == null) return null;
        Document document = documentService.saveAnnotationToCGP(answerSubjectDocumentModel.getDocuments()[0]);
        if (document == null) return null;
        AnswerSubjectDocument answerSubjectDocument = new AnswerSubjectDocument();
        answerSubjectDocument.setDescription(answerSubjectDocumentModel.getDescription());
        answerSubjectDocument.setOwner(userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()));
        answerSubjectDocument.setType(DocumentType.ANNOTATE);
        answerSubjectDocument.setDocument(document);
        answerSubjectDocument.setSubjectDocument(subjectDocument);
        return answerSubjectDocumentRepository.save(answerSubjectDocument);
    }

    @Override
    public boolean deleteAnswerSubjectDocument(Long answerSubjectDocumentId, Long subjectDocumentID) {
        AnswerSubjectDocument answerSubjectDocument = answerSubjectDocumentRepository.fingByIdAndSubjectDocumentId(answerSubjectDocumentId, subjectDocumentID);
        if(answerSubjectDocument == null) return false;
        answerSubjectDocumentRepository.delete(answerSubjectDocument);
        return true;
    }
}
