package com.hust.edu.vn.documentsystem.service;

import com.hust.edu.vn.documentsystem.data.dto.PageDto;
import com.hust.edu.vn.documentsystem.data.dto.SubjectDto;
import com.hust.edu.vn.documentsystem.data.model.*;
import com.hust.edu.vn.documentsystem.entity.*;

import java.util.List;

public interface SubjectService {

    PageDto<SubjectDto> getAllSubjects(int page, int size);

    Subject getSubjectById(Long id);

    Subject createSubject(SubjectModel subjectModel);

    boolean updateSubject(SubjectModel subjectModel);

    boolean deleteSubject(Long subjectId);

    SubjectDocument saveDocumentForSubject(SubjectDocumentModel subjectDocumentModel, Long subjectId);

    Object shareDocument(Long subjectDocumentId, ShareSubjectDocumentModel shareSubjectDocumentModel);

    CommentSubjectDocument createCommentForSubjectDocument(CommentSubjectDocumentModel commentSubjectDocumentModel,
                                                           Long subjectDocumentId);

    CommentSubjectDocument updateCommentForSubjectDocument(CommentSubjectDocumentModel commentSubjectDocumentModel,
                                                           Long subjectDocumentId, Long commentId);

    boolean hiddenCommentForSubjectDocument(Long commentId, Long subjectDocumentId);

    List<User> getAllUserShareWithMe();

    List<SubjectDto> getAllSubjectsForFilter();

    List<SubjectDocumentType> findAllSubjectDocumentType();

    List<CommentSubjectDocument> getSubjectDocumentCommentBySubjectDocumentId(Long subjectDocumentId);

    List<User> getAllUserShared(Long subjectDocumentId);

    List<SubjectDocument> getAllSubjectDocumentCreateByUser();

    List<SharePrivate> getAllSubjectDocumentShared();

    boolean clearSharedPrivateSubjectDocument(Long sharedId, Long subjectDocumentId);


    List<ReviewSubject> getAllReviewSubjectCreatedByUser();

    List<Object[]> getAllSubjectForAdmin();

    boolean deleteCommentSubjectDocument(Long subjectDocumentId, Long commentId);

    ReportContentReviewSubject createReportContentReviewSubject(Long reviewSubjectId,
                                                                ReportContentReviewSubjectModel reportContentReviewSubjectModel);

    ReportContentSubjectDocument createReportContentSubjectDocument(Long subjectDocumentId,
                                                                    ReportContentSubjectDocumentModel reportContentSubjectDocumentModel);

    ReportDuplicateSubjectDocument createReportDuplicateSubjectDocument(Long subjectDocumentId,
                                                                        ReportDuplicateSubjectDocumentModel reportContentSubjectDocumentModel);

    List<ReviewSubject> getAllReviewSubjects();

    List<Subject> getAllSubjectByInstitute(Long instituteId);

    List<String> getAllInstitute();

    List<SubjectDocument> getAllSharedByUser(Long userId);

    List<Object> getAllReported();

    boolean updateReportContentReviewSubject(Long reviewSubjectId, Long reportContentReviewSubjectId, ReportContentReviewSubjectModel reportContentReviewSubjectModel);

    boolean deleteReportContentReviewSubject(Long reviewSubjectId, Long reportContentReviewSubjectId);

    boolean updateReportContentSubjectDocument(Long subjectDocumentId, Long reportContentSubjectDocumentId, ReportContentSubjectDocumentModel reportContentSubjectDocumentModel);

    boolean deleteReportContentSubjectDocument(Long subjectDocumentId, Long reportContentSubjectDocumentId);
}
