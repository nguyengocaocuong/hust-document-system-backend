package com.hust.edu.vn.documentsystem.service;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.common.type.SubjectDocumentType;
import com.hust.edu.vn.documentsystem.data.dto.SubjectDto;
import com.hust.edu.vn.documentsystem.data.model.*;
import com.hust.edu.vn.documentsystem.entity.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface SubjectService {

    List<SubjectDto> getAllSubjects();

    Subject getSubjectById(Long id);

    Subject createSubject(SubjectModel subjectModel);

    boolean updateSubject(SubjectModel subjectModel);

    boolean deleteSubject(Long subjectId);

    List<Subject> getAllSubjectsCreateByUser();

    List<Subject> findSubjectByKeywordAndTeachers(String keyword, Teacher teacher);

    SubjectDocument saveDocumentForSubject(SubjectDocumentModel subjectDocumentModel);

    Object shareDocument(ShareSubjectDocumentModel shareSubjectDocumentModel);

    boolean favoriteSubject(FavoriteSubjectModel favoriteSubjectModel);

    boolean unFavoriteSubject(Long subjectId);

    boolean updateFavoriteSubject(FavoriteSubjectModel favoriteSubjectModel);

    CommentSubjectDocument createCommentForSubjectDocument(CommentSubjectDocumentModel commentSubjectDocumentModel);

    boolean updateCommentForSubjectDocument(CommentSubjectDocumentModel commentSubjectDocumentModel);

    boolean deleteCommentForSubjectDocument(Long id);

    boolean hiddenCommentForSubjectDocument(Long id);

    boolean activeCommentForSubjectDocument(Long id);

    List<FavoriteSubject> getFavoriteSubjects();

    boolean updateDocumentForSubject(SubjectDocumentModel subjectDocumentModel);

    boolean deleteDocumentForSubject(SubjectDocumentModel subjectDocumentModel);

    List<User> getAllUserShareWithMe();

    List<SubjectDto> getAllSubjectsForFilter();

    List<String> findAllSemesterForFilter();

    List<SubjectDocumentType> findAllSubjectDocumentType();

    SubjectDocument getStSubjectDetail(Long subjectId);

    SubjectDocument getSubjectDocumentDetailById(Long subjectDocumentId);

    List<CommentSubjectDocument> getSubjectDocumentCommentBySubjectDocumentId(Long subjectDocumentId);

    boolean favoriteSubjectDocument(Long subjectDocumentId);

    AnswerSubjectDocument saveAnswerForSubjectDocument(Long subjectDocumentId,AnswerSubjectDocumentModel answerSubjectDocumentModel);
}
