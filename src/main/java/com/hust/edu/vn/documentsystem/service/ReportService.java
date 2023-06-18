package com.hust.edu.vn.documentsystem.service;

import com.hust.edu.vn.documentsystem.data.model.ProcessReportContentModel;
import com.hust.edu.vn.documentsystem.data.model.ReportContentModel;
import com.hust.edu.vn.documentsystem.data.model.ReportDuplicateModel;
import com.hust.edu.vn.documentsystem.entity.ReportContentSubjectDocument;
import com.hust.edu.vn.documentsystem.entity.ReportDuplicateSubjectDocument;

import java.util.List;

public interface ReportService {
    ReportContentSubjectDocument reportContent(ReportContentModel reportContentModel);

    ReportDuplicateSubjectDocument reportDuplicate(ReportDuplicateModel reportDuplicateModel);

    boolean processReportContent(ProcessReportContentModel processReportContentModel);


    List<ReportContentSubjectDocument> getAllReportContents();

    List<ReportDuplicateSubjectDocument> getReportDuplicateDocuments();
}
