package com.hust.edu.vn.documentsystem.service.impl;

import com.hust.edu.vn.documentsystem.common.type.ReportStatus;
import com.hust.edu.vn.documentsystem.data.model.ProcessDuplicateModel;
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
import java.util.Objects;

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
        Document document = documentRepository.findById(reportContentModel.getDocumentId()).orElse(null);
        if (document == null) return null;
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        ReportContent reportContent = reportContentRepository.findByDocumentAndOwner(document, user);
        if (reportContent != null) return null;
        reportContent = modelMapperUtils.mapAllProperties(reportContentModel, ReportContent.class);
        reportContent.setDocument(document);
        reportContent.setOwner(user);
        return reportContentRepository.save(reportContent);
    }

    @Override
    public ReportDuplicateDocument reportDuplicate(ReportDuplicateModel reportDuplicateModel) {
        Document documentFirst = documentRepository.findById(reportDuplicateModel.getDocumentFirstId()).orElse(null);
        Document documentSecond = documentRepository.findById(reportDuplicateModel.getDocumentSecondId()).orElse(null);
        if (documentFirst == null || documentSecond == null) return null;
        ReportDuplicateDocument reportDuplicateDocument = modelMapperUtils.mapAllProperties(reportDuplicateModel, ReportDuplicateDocument.class);
        reportDuplicateDocument.setDocumentFirst(documentFirst);
        reportDuplicateDocument.setDocumentSecond(documentSecond);
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
    public boolean processReportDuplicate(ProcessDuplicateModel processDuplicateModel) {
        ReportDuplicateDocument reportDuplicateDocument = reportDuplicateDocumentRepository.findById(processDuplicateModel.getId()).orElse(null);
        if (reportDuplicateDocument == null) return false;
        reportDuplicateDocument.setMessage(processDuplicateModel.getMessage());
        reportDuplicateDocument.setStatus(processDuplicateModel.getReportStatus());
        reportDuplicateDocument.setStatus(processDuplicateModel.getReportStatus());
        if (processDuplicateModel.getReportStatus() == ReportStatus.ACCESS) {
            Document documentDelete = reportDuplicateDocument.getDocumentFirst().getId().intValue() == processDuplicateModel.getRemoveDocumentId().intValue() ? reportDuplicateDocument.getDocumentFirst() : reportDuplicateDocument.getDocumentSecond();
            Document documentHolder = reportDuplicateDocument.getDocumentFirst().getId().intValue() == processDuplicateModel.getRemoveDocumentId().intValue() ? reportDuplicateDocument.getDocumentFirst() : reportDuplicateDocument.getDocumentSecond();
            documentDelete.setPath(documentHolder.getPath());
            User user = subjectDocumentRepository.findByDocument(documentDelete).getOwner();
            if (user == null)
                return false;
            googleCloudStorageService.deleteDocumentByRootPath(Objects.requireNonNull(user).getRootPath() + "documents/" + documentDelete.getPath());
            documentRepository.save(documentDelete);
        }

        reportDuplicateDocumentRepository.save(reportDuplicateDocument);
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
