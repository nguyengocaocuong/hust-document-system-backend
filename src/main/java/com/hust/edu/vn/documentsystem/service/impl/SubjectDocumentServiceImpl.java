package com.hust.edu.vn.documentsystem.service.impl;

import com.google.cloud.storage.Blob;
import com.hust.edu.vn.documentsystem.common.type.DocumentType;
import com.hust.edu.vn.documentsystem.common.type.TargetLanguageType;
import com.hust.edu.vn.documentsystem.entity.*;
import com.hust.edu.vn.documentsystem.repository.*;
import com.hust.edu.vn.documentsystem.service.GoogleCloudStorageService;
import com.hust.edu.vn.documentsystem.service.GoogleCloudTranslateService;
import com.hust.edu.vn.documentsystem.service.SubjectDocumentService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class SubjectDocumentServiceImpl implements SubjectDocumentService {
    private final AnswerSubjectDocumentRepository answerSubjectDocumentRepository;
    private final SubjectDocumentRepository subjectDocumentRepository;
    private final UserRepository userRepository;
    private final ShareByLinkRepository shareByLinkRepository;

    private final GoogleCloudStorageService googleCloudStorageService;

    private final GoogleCloudTranslateService googleCloudTranslateService;

    private final HistoryRepository historyRepository;


    public SubjectDocumentServiceImpl(SubjectDocumentRepository subjectDocumentRepository,
                                      UserRepository userRepository,
                                      ShareByLinkRepository shareByLinkRepository, GoogleCloudStorageService googleCloudStorageService, GoogleCloudTranslateService googleCloudTranslateService, HistoryRepository historyRepository,
                                      AnswerSubjectDocumentRepository answerSubjectDocumentRepository) {
        this.subjectDocumentRepository = subjectDocumentRepository;
        this.userRepository = userRepository;
        this.shareByLinkRepository = shareByLinkRepository;
        this.googleCloudStorageService = googleCloudStorageService;
        this.googleCloudTranslateService = googleCloudTranslateService;
        this.historyRepository = historyRepository;
        this.answerSubjectDocumentRepository = answerSubjectDocumentRepository;
    }

    @Override
    public List<Object> readSubjectDocumentFile(Long subjectDocumentId, String token) {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        SubjectDocument subjectDocument = subjectDocumentRepository.findByIdAndUserEmailAndToken(subjectDocumentId, user.getId(), token);
        if (subjectDocument == null || subjectDocument.getType() == DocumentType.LINK)
            return null;

        byte[] data = googleCloudStorageService.readBlobByPath(subjectDocument.getDocument().getPath());
        if (data == null || data.length == 0)
            return null;
        History history = new History();
        history.setSubjectDocument(subjectDocument);
        history.setUser(user);
        historyRepository.save(history);
        subjectDocument.setTotalView(subjectDocument.getTotalView() + 1);
        subjectDocumentRepository.save(subjectDocument);
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
    public byte[] translateSubjectDocumentByFile(MultipartFile file, TargetLanguageType targetLanguageType) throws IOException {
        return googleCloudTranslateService.translateSubjectDocumentByFile(file,
                targetLanguageType);
    }

    @Override
    public Blob getSubjectDocumentBlob(Long subjectDocumentId) {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        SubjectDocument subjectDocument = subjectDocumentRepository.findByIdAndUserEmailCanDownload(subjectDocumentId, user.getId());
        if (subjectDocument == null) return null;
        subjectDocument.setTotalView(subjectDocument.getTotalDownload() + 1);
        subjectDocumentRepository.save(subjectDocument);
        return googleCloudStorageService.getBlobByPath(subjectDocument.getDocument().getPath());
    }

    @Override
    public List<Blob> getSubjectDocumentBlobAndAnswers(Long subjectDocumentId, List<Long> answerIds) {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        SubjectDocument subjectDocument = subjectDocumentRepository.findByIdAndUserEmailCanDownload(subjectDocumentId, user.getId());
        List<AnswerSubjectDocument> answerDocuments = answerSubjectDocumentRepository.findAllBySubjectDocumentIdAndListId(subjectDocumentId, answerIds);
        if (subjectDocument == null) return null;
        List<Blob> resultBlobs = new ArrayList<>();
        resultBlobs.add(googleCloudStorageService.getBlobByPath(subjectDocument.getDocument().getPath()));
        subjectDocument.setTotalDownload(subjectDocument.getTotalDownload() + 1);
        subjectDocumentRepository.save(subjectDocument);
        for (AnswerSubjectDocument answer : answerDocuments) {
            Blob blob = googleCloudStorageService.getBlobByPath(answer.getDocument().getPath());
            if (blob != null)
                resultBlobs.add(blob);
        }
        return resultBlobs;
    }
}

