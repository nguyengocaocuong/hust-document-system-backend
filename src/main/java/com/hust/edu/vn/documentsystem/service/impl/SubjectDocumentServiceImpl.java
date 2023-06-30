package com.hust.edu.vn.documentsystem.service.impl;

import com.hust.edu.vn.documentsystem.common.type.SubjectDocumentType;
import com.hust.edu.vn.documentsystem.repository.SubjectDocumentRepository;
import com.hust.edu.vn.documentsystem.service.SubjectDocumentService;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectDocumentServiceImpl implements SubjectDocumentService {
    private final SubjectDocumentRepository subjectDocumentRepository;

    
    public SubjectDocumentServiceImpl(SubjectDocumentRepository subjectDocumentRepository) {
        this.subjectDocumentRepository = subjectDocumentRepository;
    }

    @Override
    public List<SubjectDocumentType> findAllSubjectDocumentType() {
        return subjectDocumentRepository.findAllSubjectDocumentType();
    }

    @Override
    public List<String> findAllSemester() {
        return subjectDocumentRepository.findAllSemester();
    }
}
