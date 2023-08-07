package com.hust.edu.vn.documentsystem.service.impl;

import co.elastic.thumbnails4j.core.ThumbnailingException;
import com.hust.edu.vn.documentsystem.common.type.*;
import com.hust.edu.vn.documentsystem.data.dto.*;
import com.hust.edu.vn.documentsystem.data.model.*;
import com.hust.edu.vn.documentsystem.entity.*;
import com.hust.edu.vn.documentsystem.repository.*;
import com.hust.edu.vn.documentsystem.service.*;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import com.itextpdf.text.DocumentException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class SubjectServiceImpl implements SubjectService {
    private final ReportDuplicateSubjectDocumentRepository reportDuplicateSubjectDocumentRepository;
    private final ReportContentSubjectDocumentRepository reportContentSubjectDocumentRepository;
    private final ReportContentReviewSubjectRepository reportContentReviewSubjectRepository;
    private final ReviewSubjectRepository reviewSubjectRepository;
    private final CommentSubjectDocumentRepository commentSubjectDocumentRepository;
    private final SharePrivateRepository sharePrivateRepository;
    private final SubjectDocumentRepository subjectDocumentRepository;
    private final DocumentRepository documentRepository;
    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;
    private final ModelMapperUtils modelMapperUtils;
    private final DocumentService documentService;

    private final PusherService pusherService;
    private final ReviewTeacherRepository reviewTeacherRepository;
    private final ReportContentReviewTeacherRepository reportContentReviewTeacherRepository;


    public SubjectServiceImpl(
            SubjectRepository subjectRepository,
            UserRepository userRepository,
            ModelMapperUtils modelMapperUtils,
            DocumentRepository documentRepository,
            SubjectDocumentRepository subjectDocumentRepository,
            SharePrivateRepository sharePrivateRepository,
            CommentSubjectDocumentRepository commentSubjectDocumentRepository,
            DocumentService documentService,
            ReviewSubjectRepository reviewSubjectRepository,
            ReportContentReviewSubjectRepository reportContentReviewSubjectRepository,
            ReportContentSubjectDocumentRepository reportContentSubjectDocumentRepository,
            ReportDuplicateSubjectDocumentRepository reportDuplicateSubjectDocumentRepository, PusherService pusherService,
            ReviewTeacherRepository reviewTeacherRepository,
            ReportContentReviewTeacherRepository reportContentReviewTeacherRepository) {
        this.subjectRepository = subjectRepository;
        this.modelMapperUtils = modelMapperUtils;
        this.userRepository = userRepository;
        this.documentRepository = documentRepository;
        this.subjectDocumentRepository = subjectDocumentRepository;
        this.sharePrivateRepository = sharePrivateRepository;
        this.commentSubjectDocumentRepository = commentSubjectDocumentRepository;
        this.documentService = documentService;
        this.reviewSubjectRepository = reviewSubjectRepository;
        this.reportContentReviewSubjectRepository = reportContentReviewSubjectRepository;
        this.reportContentSubjectDocumentRepository = reportContentSubjectDocumentRepository;
        this.reportDuplicateSubjectDocumentRepository = reportDuplicateSubjectDocumentRepository;
        this.pusherService = pusherService;
        this.reviewTeacherRepository = reviewTeacherRepository;
        this.reportContentReviewTeacherRepository = reportContentReviewTeacherRepository;
    }

    @Override
    public List<SubjectDto> getAllSubjectsForFilter() {
        return subjectRepository.findAllSubjectName();
    }

    @Override
    public List<SubjectDocumentType> findAllSubjectDocumentType() {
        return subjectDocumentRepository.findAllSubjectDocumentType();
    }


    @Override
    public List<CommentSubjectDocument> getSubjectDocumentCommentBySubjectDocumentId(Long subjectDocumentId) {
        return commentSubjectDocumentRepository.findAllCommentById(subjectDocumentId);
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
    public List<SharePrivate> getAllSubjectDocumentShared() {
        return sharePrivateRepository
                .findAllByUserEmail(SecurityContextHolder.getContext().getAuthentication().getName());
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
    public List<ReviewSubject> getAllReviewSubjects() {
        return reviewSubjectRepository.findAllByDone(true);
    }

    @Override
    public List<Subject> getAllSubjectByInstitute(String institute) {
        return subjectRepository.findAllByInstitute(institute);
    }

    @Override
    public List<String> getAllInstitute() {
        return subjectRepository.getAllInstitute();
    }

    @Override
    public List<SubjectDocument> getAllSharedByUser(Long userId) {
        return subjectDocumentRepository.getAllSharedByUser(userId, SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Override
    public List<Object> getAllReported() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<ReportContentReviewSubject> reportContentReviewSubjects = reportContentReviewSubjectRepository.getAllReported(email);
        List<ReportContentReviewTeacher> reportContentReviewTeachers = reportContentReviewTeacherRepository.getAllReported(email);
        List<ReportContentSubjectDocument> reportContentSubjectDocuments = reportContentSubjectDocumentRepository.getAllReported(email);
        List<ReportDuplicateSubjectDocument> reportDuplicateSubjectDocuments = reportDuplicateSubjectDocumentRepository.getAllReported(email);

        return Arrays.asList(
                reportContentReviewSubjects,
                reportContentReviewTeachers,
                reportContentSubjectDocuments,
                reportDuplicateSubjectDocuments
        );
    }

    @Override
    public boolean updateReportContentReviewSubject(Long reviewSubjectId, Long reportContentReviewSubjectId, ReportContentReviewSubjectModel reportContentReviewSubjectModel) {
        ReportContentReviewSubject reportContentReviewSubject = reportContentReviewSubjectRepository.findByTeacherIdAndIdAndOwnerEmail(reportContentReviewSubjectId,reviewSubjectId, SecurityContextHolder.getContext().getAuthentication().getName());
        if(reportContentReviewSubject == null) return false;
        reportContentReviewSubject.setMessage(reportContentReviewSubjectModel.getMessage());
        reportContentReviewSubjectRepository.save(reportContentReviewSubject);
        return true;
    }

    @Override
    public boolean deleteReportContentReviewSubject(Long reviewSubjectId, Long reportContentReviewSubjectId) {
        ReportContentReviewSubject reportContentReviewSubject = reportContentReviewSubjectRepository.findByTeacherIdAndIdAndOwnerEmail(reportContentReviewSubjectId,reviewSubjectId, SecurityContextHolder.getContext().getAuthentication().getName());
        if(reportContentReviewSubject == null) return false;
        reportContentReviewSubjectRepository.delete(reportContentReviewSubject);
        return true;
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
        if (shareSubjectDocumentModel.getShareUserId().isEmpty() && shareSubjectDocumentModel.getDeleteUserId().isEmpty())
            return null;
        shareSubjectDocumentModel.getShareUserId().forEach(userId -> {
            User user = userRepository.findById(userId).orElse(null);
            if (user != null) {
                SharePrivate sharePrivate = sharePrivateRepository.findBySubjectDocumentAndUser(subjectDocument, user);
                if (sharePrivate == null) {
                    sharePrivate = new SharePrivate();
                    sharePrivate.setSubjectDocument(subjectDocument);
                    sharePrivate.setUser(user);
                    sharePrivate = sharePrivateRepository.save(sharePrivate);
                    Map<String, Object> notification = new HashMap();
                    SubjectDocumentDto subjectDocumentDto = modelMapperUtils.mapAllProperties(subjectDocument, SubjectDocumentDto.class);
                    UserDto owner = modelMapperUtils.mapAllProperties(userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()), UserDto.class);
                    notification.put("subjectDocument", subjectDocumentDto);
                    notification.put("id", UUID.randomUUID().toString());
                    notification.put("user", owner);
                    notification.put("sharedAt", sharePrivate.getSharedAt());
                    notification.put("type", "SHARED");
                    pusherService.triggerChanel("notification", "share-subject-document-to-" + userId, notification);
                }
            }
        });
        shareSubjectDocumentModel.getDeleteUserId().forEach(userId -> {
            User user = userRepository.findById(userId).orElse(null);
            if (user != null) {
                SharePrivate sharePrivate = sharePrivateRepository.findBySubjectDocumentAndUser(subjectDocument, user);
                if (sharePrivate != null) {
                    sharePrivateRepository.delete(sharePrivate);
                }
            }
        });
        return true;
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
    public PageDto<SubjectDto> getAllSubjects(int page, int size) {
        Sort sort = Sort.by("id").descending();
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        Page<Subject> pages = subjectRepository.findAll(pageRequest);
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        List<SubjectDto> subjects = subjectRepository.findAllSubjects(user.getId(),pageRequest);
        PageDto<SubjectDto> pageResult = new PageDto<>();
        pageResult.setTotalPages(pages.getTotalPages());
        pageResult.setTotalItems(pages.getTotalElements());
        pageResult.setItems(subjects);

        return pageResult;
    }
}
