package com.hust.edu.vn.documentsystem.service;

import com.google.cloud.storage.Blob;
import com.hust.edu.vn.documentsystem.common.type.TargetLanguageType;
import com.hust.edu.vn.documentsystem.entity.SubjectDocument;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface SubjectDocumentService {

    List<Object> readSubjectDocumentFile(Long subjectDocumentId, String token);

    SubjectDocument getSubjectDocumentDetailById(Long subjectDocumentId);

    String generatePublicOnInternetUrlForSubjectDocument(Long subjectDocumentId);

    String generatePublicOnWebsiteUrlForSubjectDocument(Long subjectDocumentId);

    boolean deleteSubjectDocumentForever(Long subjectDocumentId);

    boolean moveSubjectDocumentToTrash(Long subjectDocumentId);

    boolean restoreSubjectDocument(Long subjectDocumentId);

    boolean makeSubjectDocumentPrivate(Long subjectDocumentId);

    List<Object> translateSubjectDocument(Long subjectDocumentId, TargetLanguageType targetLanguageType);

    boolean makeSubjectDocumentPublic(Long subjectDocumentId);

    byte[] translateSubjectDocumentByFile(MultipartFile document, TargetLanguageType targetLanguageType) throws IOException;

    Blob getSubjectDocumentBlob(Long subjectDocumentId);

    List<Blob> getSubjectDocumentBlobAndAnswers(Long subjectDocumentId, List<Long> answerIds);
}
