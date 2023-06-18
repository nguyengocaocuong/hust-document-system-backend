package com.hust.edu.vn.documentsystem.service.impl;

import com.hust.edu.vn.documentsystem.entity.ReportContentSubjectDocument;
import com.hust.edu.vn.documentsystem.repository.ReportContentSubjectDocumentRepository;
import com.hust.edu.vn.documentsystem.service.ReportContentSubjectDocumentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportContentSubjectDocumentServiceImpl implements ReportContentSubjectDocumentService {
    private final ReportContentSubjectDocumentRepository reportContentSubjectDocumentRepository;

    public ReportContentSubjectDocumentServiceImpl(ReportContentSubjectDocumentRepository reportContentSubjectDocumentRepository) {
        this.reportContentSubjectDocumentRepository = reportContentSubjectDocumentRepository;
    }

    @Override
    public List<ReportContentSubjectDocument> getAllReportContentSubjectDocument() {
        return reportContentSubjectDocumentRepository.findAll();
    }
}
