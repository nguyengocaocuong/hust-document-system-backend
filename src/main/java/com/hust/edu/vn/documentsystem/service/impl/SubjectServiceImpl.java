package com.hust.edu.vn.documentsystem.service.impl;

import co.elastic.thumbnails4j.core.ThumbnailingException;
import com.google.cloud.storage.Acl;
import com.hust.edu.vn.documentsystem.common.type.*;
import com.hust.edu.vn.documentsystem.data.dto.SubjectDto;
import com.hust.edu.vn.documentsystem.data.model.*;
import com.hust.edu.vn.documentsystem.entity.*;
import com.hust.edu.vn.documentsystem.repository.*;
import com.hust.edu.vn.documentsystem.service.*;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class SubjectServiceImpl implements SubjectService {
    private final ReportDuplicateSubjectDocumentRepository reportDuplicateSubjectDocumentRepository;
    private final ReportContentSubjectDocumentRepository reportContentSubjectDocumentRepository;
    private final ReportContentReviewSubjectRepository reportContentReviewSubjectRepository;
    private final ReviewSubjectRepository reviewSubjectRepository;
    private final ShareByLinkRepository shareByLinkRepository;
    private final AnswerSubjectDocumentRepository answerSubjectDocumentRepository;
    private final AnswerSubjectRepository answerSubjectRepository;
    private final CommentSubjectDocumentRepository commentSubjectDocumentRepository;
    private final SharePrivateRepository sharePrivateRepository;
    private final SubjectDocumentRepository subjectDocumentRepository;
    private final DocumentRepository documentRepository;
    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;
    private final ModelMapperUtils modelMapperUtils;
    private final GoogleCloudStorageService googleCloudStorageService;
    private final DocumentService documentService;

    private final GoogleCloudTranslateService googleCloudTranslateService;

    @Autowired
    public SubjectServiceImpl(
            SubjectRepository subjectRepository,
            UserRepository userRepository,
            TeacherRepository teacherRepository,
            ModelMapperUtils modelMapperUtils,
            GoogleCloudStorageService googleCloudStorageService,
            DocumentRepository documentRepository,
            SubjectDocumentRepository subjectDocumentRepository,
            SharePrivateRepository sharePrivateRepository,
            CommentSubjectDocumentRepository commentSubjectDocumentRepository,
            AnswerSubjectRepository answerSubjectRepository, DocumentService documentService,
            AnswerSubjectDocumentRepository answerSubjectDocumentRepository,
            ShareByLinkRepository shareByLinkRepository, GoogleCloudTranslateService googleCloudTranslateService,
            ReviewSubjectRepository reviewSubjectRepository,
            ReportContentReviewSubjectRepository reportContentReviewSubjectRepository,
            ReportContentSubjectDocumentRepository reportContentSubjectDocumentRepository,
            ReportDuplicateSubjectDocumentRepository reportDuplicateSubjectDocumentRepository) {
        this.subjectRepository = subjectRepository;
        this.modelMapperUtils = modelMapperUtils;
        this.userRepository = userRepository;
        this.teacherRepository = teacherRepository;
        this.googleCloudStorageService = googleCloudStorageService;
        this.documentRepository = documentRepository;
        this.subjectDocumentRepository = subjectDocumentRepository;
        this.sharePrivateRepository = sharePrivateRepository;
        this.commentSubjectDocumentRepository = commentSubjectDocumentRepository;
        this.answerSubjectRepository = answerSubjectRepository;
        this.documentService = documentService;
        this.answerSubjectDocumentRepository = answerSubjectDocumentRepository;
        this.shareByLinkRepository = shareByLinkRepository;
        this.googleCloudTranslateService = googleCloudTranslateService;
        this.reviewSubjectRepository = reviewSubjectRepository;
        this.reportContentReviewSubjectRepository = reportContentReviewSubjectRepository;
        this.reportContentSubjectDocumentRepository = reportContentSubjectDocumentRepository;
        this.reportDuplicateSubjectDocumentRepository = reportDuplicateSubjectDocumentRepository;
    }

    @Override
    public List<SubjectDto> getAllSubjectsForFilter() {
        return subjectRepository.findAllSubjectName();
    }

    @Override
    public List<String> findAllSemesterForFilter() {
        return subjectDocumentRepository.findAllSemester();
    }

    @Override
    public List<SubjectDocumentType> findAllSubjectDocumentType() {
        return subjectDocumentRepository.findAllSubjectDocumentType();
    }

    @Override
    public SubjectDocument getSubjectDocumentDetailById(Long subjectDocumentId) {
        return subjectDocumentRepository.findById(subjectDocumentId).orElse(null);
    }

    @Override
    public List<CommentSubjectDocument> getSubjectDocumentCommentBySubjectDocumentId(Long subjectDocumentId) {
        return commentSubjectDocumentRepository.findAllCommentById(subjectDocumentId);
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
                document = documentService.savePublicDocumentToGoogleCloud(answerSubjectDocumentModel.getDocuments());
            }
            answerSubjectDocument.setDescription(answerSubjectDocumentModel.getDescription());
            answerSubjectDocument.setDocument(document);
            answerSubjectDocument.setSubjectDocument(subjectDocument);
            answerSubjectDocument.setOwner(user);
            answerSubjectDocument.setType(answerSubjectDocumentModel.getType());
            answerSubjectRepository.save(answerSubjectDocument);
            return answerSubjectDocument;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Object> readSubjectDocumentFile(Long subjectDocumentId, String token) {
        SubjectDocument subjectDocument = subjectDocumentRepository.findById(subjectDocumentId).orElse(null);
        SharePrivate sharePrivate = sharePrivateRepository.findBySubjectDocumentAndUser(subjectDocument,
                userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()));
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
        return List.of(subjectDocument.getDocument(), data);
    }

    @Override
    public List<AnswerSubjectDocument> getAllAnswerSubjectDocument(Long subjectDocumentId) {
        return answerSubjectDocumentRepository.findAllBySubjectDocumentId(subjectDocumentId);
    }

    @Override
    public List<User> getAllUserShared(Long subjectDocumentId) {
        return sharePrivateRepository.findBySubjectDocumentId(subjectDocumentId);
    }

    @Override
    public List<SubjectDocument> getAllSubjectDocumentCreateByUser() {

        return subjectDocumentRepository
                .findAllByUserEmailAndIsDelete(SecurityContextHolder.getContext().getAuthentication().getName());
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
    public List<SharePrivate> getAllSubjectDocumentShared() {
        return sharePrivateRepository
                .findAllByUserEmail(SecurityContextHolder.getContext().getAuthentication().getName());
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
        subjectDocument.setLastEditedAt(new Date());
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
    public boolean clearSharedPrivateSubjectDocument(Long sharedId, Long subjectDocumentId) {
        SharePrivate sharePrivate = sharePrivateRepository.findByIdAndSubjectDocumentIdAndUserEmail(sharedId,
                subjectDocumentId,
                SecurityContextHolder.getContext().getAuthentication().getName());
        if (sharePrivate == null)
            return false;
        sharePrivateRepository.delete(sharePrivate);
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
    public List<ReviewSubject> getAllReviewSubjectCreatedByUser() {
        return reviewSubjectRepository
                .findAllReviewSubjectCreatedByUser(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Override
    public List<Object[]> getAllSubjectForAdmin() {
        return subjectRepository.getAllSubjectForAdmin();
    }

    @Override
    public boolean deleteCommentSubjectDocument(Long subjectDocumentId, Long commentId) {
        CommentSubjectDocument commentSubjectDocument = commentSubjectDocumentRepository
                .findByIdAndSubjectDocumentIdAndOwnerEmail(commentId, subjectDocumentId,
                        SecurityContextHolder.getContext().getAuthentication().getName());
        if (commentSubjectDocument == null)
            return false;
        commentSubjectDocumentRepository.delete(commentSubjectDocument);
        return true;
    }

    @Override
    public ReportContentReviewSubject createReportContentReviewSubject(Long reviewSubjectId,
            ReportContentReviewSubjectModel reportContentReviewSubjectModel) {
        ReviewSubject reviewSubject = reviewSubjectRepository.findById(reviewSubjectId).orElse(null);
        if (reviewSubject == null)
            return null;
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        ReportContentReviewSubject reportContentReviewSubject = new ReportContentReviewSubject();
        reportContentReviewSubject.setReviewSubject(reviewSubject);
        reportContentReviewSubject.setOwner(user);
        reportContentReviewSubject.setMessage(reportContentReviewSubjectModel.getMessage());
        reportContentReviewSubject.setStatus(ReportStatus.NEW_REPORT);
        return reportContentReviewSubjectRepository.save(reportContentReviewSubject);
    }

    @Override
    public ReportContentSubjectDocument createReportContentSubjectDocument(Long subjectDocumentId,
            ReportContentSubjectDocumentModel reportContentSubjectDocumentModel) {
        SubjectDocument subjectDocument = subjectDocumentRepository.findById(subjectDocumentId).orElse(null);
        if (subjectDocument == null)
            return null;
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        ReportContentSubjectDocument reportContentSubjectDocument = new ReportContentSubjectDocument();
        reportContentSubjectDocument.setOwner(user);
        reportContentSubjectDocument.setSubjectDocument(subjectDocument);
        reportContentSubjectDocument.setStatus(ReportStatus.NEW_REPORT);
        reportContentSubjectDocument.setMessage(reportContentSubjectDocumentModel.getMessage());

        return reportContentSubjectDocumentRepository.save(reportContentSubjectDocument);
    }

    @Override
    public ReportDuplicateSubjectDocument createReportDuplicateSubjectDocument(Long subjectDocumentId,
            ReportDuplicateSubjectDocumentModel reportContentSubjectDocumentModel) {
        SubjectDocument subjectDocumentFirst = subjectDocumentRepository.findById(subjectDocumentId).orElse(null);
        if (subjectDocumentFirst == null)
            return null;
        SubjectDocument subjectDocumentSecond = subjectDocumentRepository
                .findById(reportContentSubjectDocumentModel.getSubjectDocumentId()).orElse(null);
        if (subjectDocumentSecond == null)
            return null;
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        ReportDuplicateSubjectDocument reportDuplicateSubjectDocument = new ReportDuplicateSubjectDocument();
        reportDuplicateSubjectDocument.setOwner(user);
        reportDuplicateSubjectDocument.setSubjectDocumentFirst(subjectDocumentFirst);
        reportDuplicateSubjectDocument.setSubjectDocumentSecond(subjectDocumentSecond);

        return reportDuplicateSubjectDocumentRepository.save(reportDuplicateSubjectDocument);
    }

    @Override
    public Subject getSubjectById(Long id) {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        Subject subject = subjectRepository.findById(id).orElse(null);
        List<SubjectDocument> subjectDocuments = subjectDocumentRepository
                .findAllSubjectDocumentCanAccessByUserEmail(user.getId(), id);
        subject.setSubjectDocuments(subjectDocuments);
        return subject;
    }

    @Override
    public Subject createSubject(SubjectModel subjectModel) {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        Subject subject = modelMapperUtils.mapAllProperties(subjectModel, Subject.class);

        if (subjectModel.getTeacherId() != null) {
            Teacher teacher = teacherRepository.findById(subjectModel.getTeacherId().get(0)).orElse(null);
            if (teacher != null)
                subject.getTeachers().add(teacher);
        }

        subject.setOwner(user);
        subjectRepository.save(subject);
        return subject;
    }

    @Override
    public boolean updateSubject(SubjectModel subjectModel) {
        Subject subject = subjectRepository.findById(subjectModel.getId()).orElse(null);
        subject.setName(subjectModel.getName());
        subject.setSubjectCode(subjectModel.getSubjectCode());
        subject.setDescription(subjectModel.getDescription());
        subjectRepository.save(subject);
        return true;
    }

    @Override
    public boolean deleteSubject(Long subjectId) {
        Subject subject = subjectRepository.findById(subjectId).orElse(null);
        if (subject == null)
            return false;
        subjectRepository.deleteById(subjectId);
        return true;
    }

    @Override
    public List<Subject> getAllSubjectsCreateByUser() {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        return subjectRepository.findAllByOwner(user);
    }

    @Override
    public SubjectDocument saveDocumentForSubject(SubjectDocumentModel subjectDocumentModel, Long subjectId) {
        int length = subjectDocumentModel.getDocuments().length;
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        Subject subject = subjectRepository.findById(subjectId).orElse(null);
        if (length <= 0 || subject == null)
            return null;
        try {
            SubjectDocument subjectDocument = new SubjectDocument();
            Document document;

            if (subjectDocument.getType() != DocumentType.LINK) {
                document = documentService.savePrivateDocumentToGoogleCloud(subjectDocumentModel.getDocuments());
            } else {
                document = new Document();
                document.setUrl(subjectDocumentModel.getUrl());
                subjectDocument.setType(DocumentType.LINK);
                document.setContentType(MediaType.ALL.getType());
                document = documentRepository.save(document);
            }
            subjectDocument.setDocument(document);
            subjectDocument.setOwner(user);
            subjectDocument.setSubject(subject);
            subjectDocument.setSubjectDocumentType(subjectDocumentModel.getSubjectDocumentType());
            subjectDocument.setDescription(subjectDocumentModel.getDescription());
            subjectDocument.setSemester(subjectDocument.getSemester());
            subjectDocument.setPublic(subjectDocumentModel.getIsPublic() == 1);
            subjectDocument.setType(subjectDocumentModel.getType());

            subjectDocument = subjectDocumentRepository.save(subjectDocument);
            return subjectDocument;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ThumbnailingException e) {
            throw new RuntimeException(e);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object shareDocument(Long subjectDocumentId, ShareSubjectDocumentModel shareSubjectDocumentModel) {
        SubjectDocument subjectDocument = subjectDocumentRepository.findById(subjectDocumentId).orElse(null);
        if (subjectDocument == null || !subjectDocument.getOwner().getEmail()
                .equals(SecurityContextHolder.getContext().getAuthentication().getName()))
            return null;
        switch (shareSubjectDocumentModel.getShareType()) {
            case PUBLIC -> {
                subjectDocument.setPublic(true);
                return "OK";
            }
            case PRIVATE -> {
                if (shareSubjectDocumentModel.getUserIds() == null || shareSubjectDocumentModel.getUserIds().isEmpty())
                    return null;
                shareSubjectDocumentModel.getUserIds().forEach(userId -> {
                    User user = userRepository.findById(userId).orElse(null);
                    if (user != null) {
                        SharePrivate sharePrivate = new SharePrivate();
                        sharePrivate.setSubjectDocument(subjectDocument);
                        sharePrivate.setUser(user);
                        sharePrivateRepository.save(sharePrivate);
                    }
                });
                return "Ok";
            }
            case ANYONE_HAS_LINK -> {
                String token = UUID.randomUUID().toString();
                ShareByLink shareByLink = new ShareByLink();
                shareByLink.setSubjectDocument(subjectDocument);
                shareByLink.setToken(token);
                googleCloudStorageService.setAclForAccessBlob(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER),
                        subjectDocument.getDocument().getPath() + subjectDocument.getDocument().getName());
                return "https://storage.googleapis.com/" + "hust-document-file" + "/"
                        + subjectDocument.getDocument().getPath() + subjectDocument.getDocument().getName();
            }

        }
        return false;
    }

    @Override
    public CommentSubjectDocument createCommentForSubjectDocument(
            CommentSubjectDocumentModel commentSubjectDocumentModel, Long subjectDocumentId) {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        SubjectDocument subjectDocument = subjectDocumentRepository.findById(subjectDocumentId).orElse(null);
        if (subjectDocument == null)
            return null;
        SharePrivate sharePrivate = sharePrivateRepository.findBySubjectDocumentAndUser(subjectDocument, user);
        if (!subjectDocument.isPublic() && sharePrivate == null
                && !subjectDocument.getOwner().getId().equals(user.getId()))
            return null;
        CommentSubjectDocument comment = modelMapperUtils.mapAllProperties(commentSubjectDocumentModel,
                CommentSubjectDocument.class);
        if (commentSubjectDocumentModel.getParentCommentId() != null) {
            CommentSubjectDocument tmp = commentSubjectDocumentRepository
                    .findById(commentSubjectDocumentModel.getParentCommentId()).orElse(null);
            if (tmp != null)
                comment.setParentComment(tmp);
        }
        comment.setSubjectDocument(subjectDocument);
        comment.setOwner(user);
        comment = commentSubjectDocumentRepository.save(comment);
        return comment;
    }

    @Override
    public CommentSubjectDocument updateCommentForSubjectDocument(
            CommentSubjectDocumentModel commentSubjectDocumentModel, Long subjectDocumentId, Long commentId) {
        CommentSubjectDocument comment = commentSubjectDocumentRepository.findByIdAndSubjectDocumentIdAndOwnerEmail(
                commentId, subjectDocumentId, SecurityContextHolder.getContext().getAuthentication().getName());
        if (comment == null)
            return null;
        comment.setComment(commentSubjectDocumentModel.getComment());
        return commentSubjectDocumentRepository.save(comment);
    }

    @Override
    public boolean hiddenCommentForSubjectDocument(Long commentId, Long subjectDocumentId) {
        CommentSubjectDocument commentSubjectDocument = commentSubjectDocumentRepository
                .findByIdAndSubjectDocumentIdAndSubjectDocumentOwnerEmailAndIsHidden(commentId, subjectDocumentId,
                        SecurityContextHolder.getContext().getAuthentication().getName(), false);
        if (commentSubjectDocument == null)
            return false;
        commentSubjectDocument.setHidden(true);
        commentSubjectDocumentRepository.save(commentSubjectDocument);
        return true;
    }

    @Override
    public List<User> getAllUserShareWithMe() {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        return sharePrivateRepository.findByUser(user);
    }

    @Override
    public List<SubjectDto> getAllSubjects() {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        return subjectRepository.findAllSubjects(user.getId());
    }
}
