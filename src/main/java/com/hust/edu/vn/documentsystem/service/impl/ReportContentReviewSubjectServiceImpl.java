package com.hust.edu.vn.documentsystem.service.impl;

import com.hust.edu.vn.documentsystem.entity.ReportContentReviewSubject;
import com.hust.edu.vn.documentsystem.repository.ReportContentReviewSubjectRepository;
import com.hust.edu.vn.documentsystem.service.ReportContentReviewSubjectService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportContentReviewSubjectServiceImpl implements ReportContentReviewSubjectService {
    private final ReportContentReviewSubjectRepository reportContentReviewSubjectRepository;

    public ReportContentReviewSubjectServiceImpl(ReportContentReviewSubjectRepository reportContentReviewSubjectRepository) {
        this.reportContentReviewSubjectRepository = reportContentReviewSubjectRepository;
    }

    @Override
    public List<ReportContentReviewSubject> getAllReportContentForReviewSubject() {
        return reportContentReviewSubjectRepository.findAll();
    }
}
