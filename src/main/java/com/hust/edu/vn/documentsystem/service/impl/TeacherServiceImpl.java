package com.hust.edu.vn.documentsystem.service.impl;

import com.hust.edu.vn.documentsystem.common.type.ApproveType;
import com.hust.edu.vn.documentsystem.common.type.ReportStatus;
import com.hust.edu.vn.documentsystem.data.dto.TeacherDto;
import com.hust.edu.vn.documentsystem.data.model.ReportContentReviewTeacherModel;
import com.hust.edu.vn.documentsystem.data.model.TeacherModel;
import com.hust.edu.vn.documentsystem.entity.*;
import com.hust.edu.vn.documentsystem.repository.*;
import com.hust.edu.vn.documentsystem.service.GoogleCloudStorageService;
import com.hust.edu.vn.documentsystem.service.TeacherService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class TeacherServiceImpl implements TeacherService {
    private final ReportContentReviewTeacherRepository reportContentReviewTeacherRepository;
    private final ReviewTeacherRepository reviewTeacherRepository;
    private final TeacherRepository teacherRepository;

    private final ModelMapperUtils modelMapperUtils;
    private final UserRepository userRepository;

    private final GoogleCloudStorageService googleCloudStorageService;

    public TeacherServiceImpl(
            TeacherRepository teacherRepository,
            ModelMapperUtils modelMapperUtils,
            UserRepository userRepository,
            GoogleCloudStorageService googleCloudStorageService,
            ReviewTeacherRepository reviewTeacherRepository,
            ReportContentReviewTeacherRepository reportContentReviewTeacherRepository) {
        this.teacherRepository = teacherRepository;
        this.modelMapperUtils = modelMapperUtils;
        this.userRepository = userRepository;
        this.googleCloudStorageService = googleCloudStorageService;
        this.reviewTeacherRepository = reviewTeacherRepository;
        this.reportContentReviewTeacherRepository = reportContentReviewTeacherRepository;
    }

    @Override
    public Teacher createTeacher(TeacherModel teacherModel) throws IOException {
        if (teacherRepository.findByEmailHust(teacherModel.getEmailHust()) != null)
            return null;
        Teacher teacher = modelMapperUtils.mapAllProperties(teacherModel, Teacher.class);
        if(teacherModel.getAvatarFile() != null){
            String url = googleCloudStorageService.uploadAvatarToGCP(teacherModel.getAvatarFile());
            teacher.setAvatar(url);
        }
        return teacherRepository.save(teacher);
    }

    @Override
    public List<TeacherDto> getAllTeachersForFilter() {
        return teacherRepository.findAllTeacherForFilter();
    }

    @Override
    public List<ReviewTeacher> findAllReviewTeacher() {
        return reviewTeacherRepository.findAllByDoneAndApproved(true, ApproveType.APPROVED);
    }

    @Override
    public List<ReviewTeacher> getAllReviewTeacherCreatedByUser() {
        return reviewTeacherRepository
                .findAllReviewTeacherCreateByUser(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Override
    public List<Object[]> getAllTeacherForAdmin() {
        return teacherRepository.getAllTeacherForAdmin();
    }

    @Override
    public boolean updateTeacher(TeacherModel teacherModel) {
        Teacher teacher = teacherRepository.findById(teacherModel.getId()).orElse(null);
        if (teacher == null)
            return false;
        teacher.setAvatar(teacherModel.getAvatar());
        if (teacherModel.getAvatarFile() != null) {
            try {
                String url = googleCloudStorageService.uploadAvatarToGCP(teacherModel.getAvatarFile());
                teacher.setAvatar(url);
                teacherModel.setAvatar(url);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        teacher.setName(teacherModel.getName());
        teacher.setDescription(teacherModel.getDescription());
        teacher.setEmailHust(teacherModel.getEmailHust());
        teacher.setPhoneNumber(teacherModel.getPhoneNumber());
        teacher.setEmailPrivate(teacherModel.getEmailPrivate());
        teacherRepository.save(teacher);
        return true;
    }

    @Override
    public boolean deleteTeacher(Long teacherId) {
        if (!teacherRepository.existsById(teacherId))
            return false;
        teacherRepository.deleteById(teacherId);
        return true;
    }


    @Override
    public ReportContentReviewTeacher createReportContentReviewTeacher(Long reviewTeacherId,
                                                                       ReportContentReviewTeacherModel reportContentReviewTeacherModel) {
        ReviewTeacher reviewTeacher = reviewTeacherRepository.findById(reviewTeacherId).orElse(null);
        if (reviewTeacher == null)
            return null;
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        ReportContentReviewTeacher reportContentReviewTeacher = new ReportContentReviewTeacher();
        reportContentReviewTeacher.setReviewTeacher(reviewTeacher);
        reportContentReviewTeacher.setOwner(user);
        reportContentReviewTeacher.setMessage(reportContentReviewTeacherModel.getMessage());
        reportContentReviewTeacher.setStatus(ReportStatus.NEW_REPORT);
        return reportContentReviewTeacherRepository.save(reportContentReviewTeacher);
    }

    @Override
    public boolean updateReportContentReviewTeacher(Long reviewTeacherId, Long reportContentReviewTeacherId,ReportContentReviewTeacherModel reportContentReviewTeacherModel) {
        ReportContentReviewTeacher reportContentReviewTeacher = reportContentReviewTeacherRepository.findByTeacherIdAndIdAndOwnerEmail(reportContentReviewTeacherId,reviewTeacherId, SecurityContextHolder.getContext().getAuthentication().getName());
        if(reportContentReviewTeacher == null) return false;
        reportContentReviewTeacher.setMessage(reportContentReviewTeacherModel.getMessage());
        reportContentReviewTeacherRepository.save(reportContentReviewTeacher);
        return true;
    }

    @Override
    public boolean deleteReportContentReviewTeacher(Long reviewTeacherId, Long reportContentReviewTeacherId) {
        ReportContentReviewTeacher reportContentReviewTeacher = reportContentReviewTeacherRepository.findByTeacherIdAndIdAndOwnerEmail(reportContentReviewTeacherId,reviewTeacherId, SecurityContextHolder.getContext().getAuthentication().getName());
        if(reportContentReviewTeacher == null) return false;
        reportContentReviewTeacherRepository.delete(reportContentReviewTeacher);
        return true;
    }
}
