package com.hust.edu.vn.documentsystem.service;

import com.hust.edu.vn.documentsystem.common.type.SubjectDocumentType;

import java.util.List;

public interface SubjectDocumentService {
    List<SubjectDocumentType> findAllSubjectDocumentType();

    List<String> findAllSemester();
}
