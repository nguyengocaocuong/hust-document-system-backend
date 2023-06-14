package com.hust.edu.vn.documentsystem.service.impl;

import com.google.cloud.storage.Acl;
import com.hust.edu.vn.documentsystem.entity.Document;
import com.hust.edu.vn.documentsystem.repository.DocumentRepository;
import com.hust.edu.vn.documentsystem.repository.UserRepository;
import com.hust.edu.vn.documentsystem.service.DocumentService;
import com.hust.edu.vn.documentsystem.service.GoogleCloudStorageService;
import com.hust.edu.vn.documentsystem.service.SignTextOrLogoImageToDocumentService;
import com.hust.edu.vn.documentsystem.service.ThumbnailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentServiceImpl implements DocumentService {
    private final GoogleCloudStorageService googleCloudStorageService;
    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;
    private final ThumbnailService thumbnailService;
    private final SignTextOrLogoImageToDocumentService signTextOrLogoImageToDocumentService;
    private final ResourceLoader resourceLoader;

    @Autowired
    public DocumentServiceImpl(GoogleCloudStorageService googleCloudStorageService,
                               DocumentRepository documentRepository,
                               UserRepository userRepository, ThumbnailService thumbnailService, SignTextOrLogoImageToDocumentService signTextOrLogoImageToDocumentService, ResourceLoader resourceLoader) {
        this.googleCloudStorageService = googleCloudStorageService;
        this.documentRepository = documentRepository;
        this.userRepository = userRepository;
        this.thumbnailService = thumbnailService;
        this.signTextOrLogoImageToDocumentService = signTextOrLogoImageToDocumentService;
        this.resourceLoader = resourceLoader;
    }

    @Override
    public Document savePrivateDocumentToGoogleCloud(MultipartFile[] documents) throws IOException {
        MultipartFile documentFile = documents[0];
        Document document = new Document();
        List<String> paths = googleCloudStorageService.createThumbnailAndUploadDocumentToGCP(documentFile, null);
        if (paths.size() > 1)
            document.setThumbnail(paths.get(1));
        document.setPath(paths.get(0));
        document.setContentType(documentFile.getContentType());
        document.setName(documentFile.getOriginalFilename());
        return documentRepository.save(document);
    }

    @Override
    public Document savePublicDocumentToGoogleCloud(MultipartFile[] documents) throws IOException {
        MultipartFile documentFile = documents[0];
        Document document = new Document();
        ArrayList<Acl> owner = new ArrayList<>();
        owner.add(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));
        List<String> paths = googleCloudStorageService.createThumbnailAndUploadDocumentToGCP(documentFile, owner);
        if (paths.size() > 1)
            document.setThumbnail(paths.get(1));
        document.setPath(paths.get(0));
        document.setContentType(documentFile.getContentType());
        document.setName(documentFile.getOriginalFilename());
        return documentRepository.save(document);
    }
}
