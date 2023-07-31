package com.hust.edu.vn.documentsystem.service;

import com.hust.edu.vn.documentsystem.data.dto.TeacherDto;
import com.hust.edu.vn.documentsystem.data.model.ReportContentReviewTeacherModel;
import com.hust.edu.vn.documentsystem.data.model.TeacherModel;
import com.hust.edu.vn.documentsystem.entity.*;

import java.io.IOException;
import java.util.List;

public interface TeacherService {

    Teacher createTeacher(TeacherModel teacherModel) throws IOException;

    List<TeacherDto> getAllTeachersForFilter();

    List<ReviewTeacher> findAllReviewTeacher();

    boolean deleteReviewTeacher(Long reviewTeacherId);

    List<ReviewTeacher> getAllReviewTeacherCreatedByUser();

    List<Object[]> getAllTeacherForAdmin();

    boolean updateTeacher(TeacherModel teacherModel);

    boolean deleteTeacher(Long teacherId);

    ReportContentReviewTeacher createReportContentReviewTeacher(Long reviewTeacherId,
                                                                ReportContentReviewTeacherModel reportContentReviewTeacherModel);
}
