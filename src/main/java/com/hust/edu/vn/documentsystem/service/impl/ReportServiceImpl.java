package com.hust.edu.vn.documentsystem.service.impl;

import com.hust.edu.vn.documentsystem.data.model.ProcessReportContentModel;
import com.hust.edu.vn.documentsystem.data.model.ReportContentModel;
import com.hust.edu.vn.documentsystem.data.model.ReportDuplicateModel;
import com.hust.edu.vn.documentsystem.entity.*;
import com.hust.edu.vn.documentsystem.repository.*;
import com.hust.edu.vn.documentsystem.service.GoogleCloudStorageService;
import com.hust.edu.vn.documentsystem.service.ReportService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {
    private final ReportContentRepository reportContentRepository;
    private final UserRepository userRepository;
    private final DocumentRepository documentRepository;
    private final ModelMapperUtils modelMapperUtils;
    private final ReportDuplicateDocumentRepository reportDuplicateDocumentRepository;
    private final GoogleCloudStorageService googleCloudStorageService;
    private final SubjectDocumentRepository subjectDocumentRepository;

    @Autowired
    public ReportServiceImpl(ReportContentRepository reportContentRepository,
                             UserRepository userRepository,
                             DocumentRepository documentRepository, ModelMapperUtils modelMapperUtils,
                             ReportDuplicateDocumentRepository reportDuplicateDocumentRepository, GoogleCloudStorageService googleCloudStorageService,
                             SubjectDocumentRepository subjectDocumentRepository) {
        this.reportContentRepository = reportContentRepository;
        this.userRepository = userRepository;
        this.documentRepository = documentRepository;
        this.modelMapperUtils = modelMapperUtils;
        this.reportDuplicateDocumentRepository = reportDuplicateDocumentRepository;
        this.googleCloudStorageService = googleCloudStorageService;
        this.subjectDocumentRepository = subjectDocumentRepository;
    }

    @Override
    public ReportContent reportContent(ReportContentModel reportContentModel) {
        SubjectDocument subjectDocument = subjectDocumentRepository.findById(reportContentModel.getSubjectDocumentId()).orElse(null);
        if (subjectDocument == null) return null;
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        ReportContent reportContent = reportContentRepository.findBySubjectDocumentAndOwner(subjectDocument, user);
        if (reportContent != null) return null;
        reportContent = modelMapperUtils.mapAllProperties(reportContentModel, ReportContent.class);
        reportContent.setSubjectDocument(subjectDocument);
        reportContent.setOwner(user);
        return reportContentRepository.save(reportContent);
    }

    @Override
    public ReportDuplicateDocument reportDuplicate(ReportDuplicateModel reportDuplicateModel) {
        SubjectDocument documentFirst = subjectDocumentRepository.findById(reportDuplicateModel.getDocumentFirstId()).orElse(null);
        SubjectDocument documentSecond = subjectDocumentRepository.findById(reportDuplicateModel.getDocumentSecondId()).orElse(null);
        if (documentFirst == null || documentSecond == null) return null;
        ReportDuplicateDocument reportDuplicateDocument = modelMapperUtils.mapAllProperties(reportDuplicateModel, ReportDuplicateDocument.class);
        reportDuplicateDocument.setSubjectDocumentFirst(documentFirst);
        reportDuplicateDocument.setSubjectDocumentSecond(documentSecond);
        reportDuplicateDocument.setOwner(userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()));
        return reportDuplicateDocumentRepository.save(reportDuplicateDocument);
    }

    @Override
    public boolean processReportContent(ProcessReportContentModel processReportContentModel) {
        ReportContent reportContent = reportContentRepository.findById(processReportContentModel.getId()).orElse(null);
        if (reportContent == null) return false;
        reportContent.setMessage(processReportContentModel.getMessage());
        reportContent.setStatus(processReportContentModel.getReportStatus());
        reportContentRepository.save(reportContent);
        return true;
    }


    @Override
    public List<ReportContent> getAllReportContents() {
        return reportContentRepository.findAllReports();
    }

    @Override
    public List<ReportDuplicateDocument> getReportDuplicateDocuments() {
        return reportDuplicateDocumentRepository.findAllReportDuplicateDocuments();
    }

}
