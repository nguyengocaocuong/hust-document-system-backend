package com.hust.edu.vn.documentsystem.service.impl;

import com.hust.edu.vn.documentsystem.entity.ReportDuplicateSubjectDocument;
import com.hust.edu.vn.documentsystem.repository.ReportDuplicateSubjectDocumentRepository;
import com.hust.edu.vn.documentsystem.service.ReportDuplicateSubjectDocumentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportDuplicateSubjectDocumentServiceImpl implements ReportDuplicateSubjectDocumentService {
    private final ReportDuplicateSubjectDocumentRepository reportDuplicateSubjectDocumentRepository;

    public ReportDuplicateSubjectDocumentServiceImpl(ReportDuplicateSubjectDocumentRepository reportDuplicateSubjectDocumentRepository) {
        this.reportDuplicateSubjectDocumentRepository = reportDuplicateSubjectDocumentRepository;
    }

    @Override
    public List<ReportDuplicateSubjectDocument> getAllReportDuplicateSubjectDocument() {
        return reportDuplicateSubjectDocumentRepository.findAll();
    }
}
