package com.hust.edu.vn.documentsystem.service.impl;

import com.hust.edu.vn.documentsystem.common.type.DocumentType;
import com.hust.edu.vn.documentsystem.common.type.TargetLanguageType;
import com.hust.edu.vn.documentsystem.entity.*;
import com.hust.edu.vn.documentsystem.repository.*;
import com.hust.edu.vn.documentsystem.service.GoogleCloudStorageService;
import com.hust.edu.vn.documentsystem.service.GoogleCloudTranslateService;
import com.hust.edu.vn.documentsystem.service.SubjectDocumentService;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class SubjectDocumentServiceImpl implements SubjectDocumentService {
    private final SubjectDocumentRepository subjectDocumentRepository;
    private final SharePrivateRepository sharePrivateRepository;
    private final UserRepository userRepository;
    private final ShareByLinkRepository shareByLinkRepository;

    private final GoogleCloudStorageService googleCloudStorageService;

    private final GoogleCloudTranslateService googleCloudTranslateService;

    private final HistoryRepository historyRepository;


    public SubjectDocumentServiceImpl(SubjectDocumentRepository subjectDocumentRepository,
                                      SharePrivateRepository sharePrivateRepository,
                                      UserRepository userRepository,
                                      ShareByLinkRepository shareByLinkRepository, GoogleCloudStorageService googleCloudStorageService, GoogleCloudTranslateService googleCloudTranslateService, HistoryRepository historyRepository) {
        this.subjectDocumentRepository = subjectDocumentRepository;
        this.sharePrivateRepository = sharePrivateRepository;
        this.userRepository = userRepository;
        this.shareByLinkRepository = shareByLinkRepository;
        this.googleCloudStorageService = googleCloudStorageService;
        this.googleCloudTranslateService = googleCloudTranslateService;
        this.historyRepository = historyRepository;
    }

    @Override
    public List<Object> readSubjectDocumentFile(Long subjectDocumentId, String token) {
        SubjectDocument subjectDocument = subjectDocumentRepository.findById(subjectDocumentId).orElse(null);
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        SharePrivate sharePrivate = sharePrivateRepository.findBySubjectDocumentAndUser(subjectDocument,
                user);
        ShareByLink shareByLink = null;
        if (token != null && token.length() > 120) {
            shareByLink = shareByLinkRepository.findBySubjectDocumentAndToken(subjectDocument, token);
        }
        if (subjectDocument == null || subjectDocument.getType() == DocumentType.LINK ||
                !subjectDocument.isPublic()
                        && !subjectDocument.getOwner().getEmail()
                        .equals((SecurityContextHolder.getContext().getAuthentication().getName()))
                        && sharePrivate == null && shareByLink == null)
            return null;

        byte[] data = googleCloudStorageService.readBlobByPath(subjectDocument.getDocument().getPath());
        if (data == null || data.length == 0)
            return null;
        History history = new History();
        history.setSubjectDocument(subjectDocument);
        history.setUser(user);
        historyRepository.save(history);

        return List.of(subjectDocument.getDocument(), data);
    }

    @Override
    public SubjectDocument getSubjectDocumentDetailById(Long subjectDocumentId) {
        return subjectDocumentRepository.findById(subjectDocumentId).orElse(null);
    }

    @Override
    public String generatePublicOnInternetUrlForSubjectDocument(Long subjectDocumentId) {
        SubjectDocument subjectDocument = subjectDocumentRepository.findByIdAndUserEmail(subjectDocumentId,
                SecurityContextHolder.getContext().getAuthentication().getName());
        if (subjectDocument == null)
            return null;
        return googleCloudStorageService.generatePublicUriForAccess(subjectDocument.getDocument().getPath(), 1000)
                .toString();
    }

    @Override
    public String generatePublicOnWebsiteUrlForSubjectDocument(Long subjectDocumentId) {
        SubjectDocument subjectDocument = subjectDocumentRepository.findByIdAndUserEmail(subjectDocumentId,
                SecurityContextHolder.getContext().getAuthentication().getName());
        if (subjectDocument == null)
            return null;
        ShareByLink shareByLink = shareByLinkRepository.findBySubjectDocument(subjectDocument);
        if (shareByLink == null) {
            shareByLink = new ShareByLink();
            shareByLink.setSubjectDocument(subjectDocument);
            StringBuilder tokenTmp = new StringBuilder(UUID.randomUUID().toString());
            tokenTmp.append(UUID.randomUUID());
            tokenTmp.append(UUID.randomUUID());
            tokenTmp.append(UUID.randomUUID());
            tokenTmp.append(UUID.randomUUID());
            tokenTmp.append(UUID.randomUUID());
            tokenTmp.append(UUID.randomUUID());
            shareByLink.setToken(tokenTmp.toString());
            shareByLinkRepository.save(shareByLink);
        }
        return System.getenv("FRONTEND_URL") + "/education/subject-document/" + subjectDocumentId + "?token="
                + shareByLink.getToken();
    }


    @Override
    public boolean deleteSubjectDocumentForever(Long subjectDocumentId) {
        SubjectDocument subjectDocument = subjectDocumentRepository.findByIdAndIsDelete(subjectDocumentId, true);
        if (subjectDocument == null)
            return false;
        subjectDocumentRepository.delete(subjectDocument);
        return true;
    }

    @Override
    public boolean moveSubjectDocumentToTrash(Long subjectDocumentId) {
        SubjectDocument subjectDocument = subjectDocumentRepository.findByIdAndUserEmail(subjectDocumentId,
                SecurityContextHolder.getContext().getAuthentication().getName());
        if (subjectDocument == null)
            return false;
        subjectDocument.setDelete(true);
        subjectDocument.setDeletedAt(new Date());
        subjectDocumentRepository.save(subjectDocument);
        return true;
    }

    @Override
    public boolean restoreSubjectDocument(Long subjectDocumentId) {
        SubjectDocument subjectDocument = subjectDocumentRepository.findByIdAndUserEmail(subjectDocumentId,
                SecurityContextHolder.getContext().getAuthentication().getName());
        if (subjectDocument == null || !subjectDocument.isDelete())
            return false;
        subjectDocument.setDelete(false);
        subjectDocumentRepository.save(subjectDocument);
        return true;
    }

    @Override
    public boolean makeSubjectDocumentPrivate(Long subjectDocumentId) {
        SubjectDocument subjectDocument = subjectDocumentRepository.findByIdAndUserEmailAndIsPublic(subjectDocumentId,
                SecurityContextHolder.getContext().getAuthentication().getName(), true);
        if (subjectDocument == null)
            return false;
        subjectDocument.setPublic(false);
        subjectDocumentRepository.save(subjectDocument);
        return true;
    }


    @Override
    public boolean makeSubjectDocumentPublic(Long subjectDocumentId) {
        SubjectDocument subjectDocument = subjectDocumentRepository.findByIdAndUserEmailAndIsPublic(subjectDocumentId,
                SecurityContextHolder.getContext().getAuthentication().getName(), false);
        if (subjectDocument == null)
            return false;
        subjectDocument.setPublic(true);
        subjectDocumentRepository.save(subjectDocument);
        return true;
    }

    @Override
    public List<Object> translateSubjectDocument(Long subjectDocumentId, TargetLanguageType targetLanguageType) {
        SubjectDocument subjectDocument = subjectDocumentRepository.findById(subjectDocumentId).orElse(null);
        if (subjectDocument == null || subjectDocument.getType() == DocumentType.LINK)
            return null;
        byte[] data = googleCloudTranslateService.translateSubjectDocument(subjectDocument.getDocument(),
                targetLanguageType);
        return List.of(subjectDocument.getDocument(), data);
    }

    @Override
    public  byte[] translateSubjectDocumentByFile(MultipartFile file, TargetLanguageType targetLanguageType) throws IOException {
        return googleCloudTranslateService.translateSubjectDocumentByFile(file,
                targetLanguageType);
    }
}
