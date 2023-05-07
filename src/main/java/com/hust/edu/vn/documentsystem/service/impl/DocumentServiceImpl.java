package com.hust.edu.vn.documentsystem.service.impl;

import com.google.cloud.storage.Blob;
import com.hust.edu.vn.documentsystem.entity.Document;
import com.hust.edu.vn.documentsystem.repository.DocumentRepository;
import com.hust.edu.vn.documentsystem.repository.UserRepository;
import com.hust.edu.vn.documentsystem.service.DocumentService;
import com.hust.edu.vn.documentsystem.service.GoogleCloudStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class DocumentServiceImpl implements DocumentService {
    private final GoogleCloudStorageService googleCloudStorageService;
    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;

    @Autowired
    public DocumentServiceImpl(GoogleCloudStorageService googleCloudStorageService,
                               DocumentRepository documentRepository,
                               UserRepository userRepository) {
        this.googleCloudStorageService = googleCloudStorageService;
        this.documentRepository = documentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Iterable<Blob> getAllFileByDocumentId(Long documentId) {
        Document document = documentRepository.findById(documentId).orElse(null);
        // TODO:  check owner
        if (document == null)
            return null;
        String path = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).getRootPath() + "documents/" + document.getPath();
        return googleCloudStorageService.getBlobListByPath(path);
    }

    @Override
    public Document findDocumentById(Long documentId) {
        Document document = documentRepository.findById(documentId).orElse(null);
        // TODO: check owner
        return document;
    }
}
