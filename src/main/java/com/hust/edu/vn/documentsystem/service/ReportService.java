package com.hust.edu.vn.documentsystem.service;

import com.hust.edu.vn.documentsystem.data.model.ProcessDuplicateModel;
import com.hust.edu.vn.documentsystem.data.model.ProcessReportContentModel;
import com.hust.edu.vn.documentsystem.data.model.ReportContentModel;
import com.hust.edu.vn.documentsystem.data.model.ReportDuplicateModel;
import com.hust.edu.vn.documentsystem.entity.ReportContent;
import com.hust.edu.vn.documentsystem.entity.ReportDuplicateDocument;

import java.util.List;

public interface ReportService {
    ReportContent reportContent(ReportContentModel reportContentModel);

    ReportDuplicateDocument reportDuplicate(ReportDuplicateModel reportDuplicateModel);

    boolean processReportContent(ProcessReportContentModel processReportContentModel);


    List<ReportContent> getAllReportContents();

    List<ReportDuplicateDocument> getReportDuplicateDocuments();
}
