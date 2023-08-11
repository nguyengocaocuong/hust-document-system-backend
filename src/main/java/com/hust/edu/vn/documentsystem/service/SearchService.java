package com.hust.edu.vn.documentsystem.service;

import com.hust.edu.vn.documentsystem.data.dto.SubjectDocumentSearchResult;
import com.hust.edu.vn.documentsystem.entity.SubjectDocument;

import java.util.List;

public interface SearchService {
    List<SubjectDocumentSearchResult> findAllSubjectDocumentBySearchOption(String key, List<String> semester, List<Long> subjectDocumentType, List<Long> subject, List<Long> institute, boolean deepSearch, boolean fuzzySearch);
}
