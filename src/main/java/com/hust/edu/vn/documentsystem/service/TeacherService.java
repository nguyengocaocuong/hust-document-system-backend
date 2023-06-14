package com.hust.edu.vn.documentsystem.service;

import com.hust.edu.vn.documentsystem.data.dto.TeacherDto;
import com.hust.edu.vn.documentsystem.data.model.CommentReviewTeacherModel;
import com.hust.edu.vn.documentsystem.data.model.TeacherModel;
import com.hust.edu.vn.documentsystem.entity.CommentReviewTeacher;
import com.hust.edu.vn.documentsystem.entity.FavoriteReviewTeacher;
import com.hust.edu.vn.documentsystem.entity.ReviewTeacher;
import com.hust.edu.vn.documentsystem.entity.Teacher;

import java.util.List;

public interface TeacherService {
    List<Teacher> getAllTeachers();

    Teacher getTeacherById(Long id);

    Teacher createTeacher(TeacherModel teacherModel);

    boolean addSubjects(TeacherModel teacherModel);

    List<TeacherDto> getAllTeachersForFilter();

    List<ReviewTeacher> findAllReviewTeacher();

    boolean toggleFavoriteReviewTeacher(Long reviewTeacherId);

    CommentReviewTeacher createCommentForReviewTeacher(Long reviewTeacherId, CommentReviewTeacherModel commentReviewTeacherModel);

    List<CommentReviewTeacher> getAllCommentForReviewTeacher(Long reviewTeacherId);

    boolean deleteCommentReview(Long commentId);

    boolean updateCommentReview(Long commentId, CommentReviewTeacherModel commentReviewTeacherModel);

    List<FavoriteReviewTeacher> getAllFavoriteReviewTeacher(Long reviewTeacherId);

    boolean deleteReviewTeacher(Long reviewTeacherId);

    List<ReviewTeacher> getAllReviewTeacherCreatedByUser();

    List<Object[]> getAllTeacherForAdmin();
}
