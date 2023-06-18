package com.hust.edu.vn.documentsystem.service;

import com.hust.edu.vn.documentsystem.common.type.SubjectDocumentType;
import com.hust.edu.vn.documentsystem.common.type.TargetLanguageType;
import com.hust.edu.vn.documentsystem.data.dto.SubjectDto;
import com.hust.edu.vn.documentsystem.data.model.*;
import com.hust.edu.vn.documentsystem.entity.*;

import java.util.List;

public interface SubjectService {

    List<SubjectDto> getAllSubjects();

    Subject getSubjectById(Long id);

    Subject createSubject(SubjectModel subjectModel);

    boolean updateSubject(SubjectModel subjectModel);

    boolean deleteSubject(Long subjectId);

    List<Subject> getAllSubjectsCreateByUser();


    SubjectDocument saveDocumentForSubject(SubjectDocumentModel subjectDocumentModel, Long subjectId);

    Object shareDocument(Long subjectDocumentId,ShareSubjectDocumentModel shareSubjectDocumentModel);

    boolean favoriteSubject(Long subjectId, FavoriteSubjectModel favoriteSubjectModel);

    boolean unFavoriteSubject(Long subjectId);

    boolean updateFavoriteSubject(FavoriteSubjectModel favoriteSubjectModel);

    CommentSubjectDocument createCommentForSubjectDocument(CommentSubjectDocumentModel commentSubjectDocumentModel);

    boolean updateCommentForSubjectDocument(CommentSubjectDocumentModel commentSubjectDocumentModel);

    boolean hiddenCommentForSubjectDocument(Long id);

    boolean activeCommentForSubjectDocument(Long id);

    List<FavoriteSubject> getFavoriteSubjects();

    List<User> getAllUserShareWithMe();

    List<SubjectDto> getAllSubjectsForFilter();

    List<String> findAllSemesterForFilter();

    List<SubjectDocumentType> findAllSubjectDocumentType();


    SubjectDocument getSubjectDocumentDetailById(Long subjectDocumentId);

    List<CommentSubjectDocument> getSubjectDocumentCommentBySubjectDocumentId(Long subjectDocumentId);

    boolean favoriteSubjectDocument(Long subjectDocumentId);

    AnswerSubjectDocument saveAnswerForSubjectDocument(Long subjectDocumentId,AnswerSubjectDocumentModel answerSubjectDocumentModel);

    List<Object> readSubjectDocumentFile(Long subjectDocumentId, String token);

    boolean favoriteAnswerSubjectDocument(Long answerSubjectDocumentID);

    List<FavoriteAnswerSubjectDocument> getAllFavoriteAnswerSubjectDocument(Long answerSubjectDocumentId);

    List<AnswerSubjectDocument> getAllAnswerSubjectDocument(Long subjectDocumentId);

    List<CommentReviewSubject> getAllCommentForReviewSubject(Long reviewSubjectId);

    List<User> getAllUserShared(Long subjectDocumentId);

    List<SubjectDocument> getAllSubjectDocumentCreateByUser();

    String generatePublicOnInternetUrlForSubjectDocument(Long subjectDocumentId);

    List<SharePrivate> getAllSubjectDocumentShared();

    String generatePublicOnWebsiteUrlForSubjectDocument(Long subjectDocumentId);

    boolean deleteSubjectDocumentForever(Long subjectDocumentId);

    boolean moveSubjectDocumentToTrash(Long subjectDocumentId);

    boolean restoreSubjectDocument(Long subjectDocumentId);

    boolean makeSubjectDocumentPublic(Long subjectDocumentId);

    boolean makeSubjectDocumentPrivate(Long subjectDocumentId);

    boolean clearSharedPrivateSubjectDocument(Long sharedId);

    List<Object> translateSubjectDocument(Long subjectDocumentId, TargetLanguageType targetLanguageType);

    boolean deleteReviewSubject(Long reviewSubjectId);

    List<ReviewSubject> getAllReviewSubjectCreatedByUser();

    List<Object[]> getAllSubjectForAdmin();
}
