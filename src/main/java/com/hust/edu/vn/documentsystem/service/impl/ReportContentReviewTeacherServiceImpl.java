package com.hust.edu.vn.documentsystem.service.impl;

import com.hust.edu.vn.documentsystem.entity.ReportContentReviewTeacher;
import com.hust.edu.vn.documentsystem.repository.ReportContentReviewTeacherRepository;
import com.hust.edu.vn.documentsystem.service.ReportContentReviewTeacherService;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportContentReviewTeacherServiceImpl implements ReportContentReviewTeacherService {
    private final ReportContentReviewTeacherRepository reportContentReviewTeacherRepository;

    
    public ReportContentReviewTeacherServiceImpl(ReportContentReviewTeacherRepository reportContentReviewTeacherRepository) {
        this.reportContentReviewTeacherRepository = reportContentReviewTeacherRepository;
    }

    @Override
    public List<ReportContentReviewTeacher> getAllReportContentForReviewSubject() {
        return reportContentReviewTeacherRepository.findAll();
    }
}
