package com.hust.edu.vn.documentsystem.service.impl;

import com.hust.edu.vn.documentsystem.data.model.SubjectDocumentTypeModel;
import com.hust.edu.vn.documentsystem.entity.SubjectDocumentType;
import com.hust.edu.vn.documentsystem.repository.SubjectDocumentTypeRepository;
import com.hust.edu.vn.documentsystem.service.SubjectDocumentTypeService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectDocumentTypeServiceImpl implements SubjectDocumentTypeService {
    private final SubjectDocumentTypeRepository subjectDocumentTypeRepository;
    private final ModelMapperUtils modelMapperUtils;

    public SubjectDocumentTypeServiceImpl(SubjectDocumentTypeRepository subjectDocumentTypeRepository, ModelMapperUtils modelMapperUtils) {
        this.subjectDocumentTypeRepository = subjectDocumentTypeRepository;
        this.modelMapperUtils = modelMapperUtils;
    }

    @Override
    public SubjectDocumentType createSubjectDocumentType(SubjectDocumentTypeModel subjectDocumentTypeModel) {
        boolean exists = subjectDocumentTypeRepository.existsByType(subjectDocumentTypeModel.getType());
        if (exists) return null;
        SubjectDocumentType subjectDocumentType = modelMapperUtils.mapAllProperties(subjectDocumentTypeModel, SubjectDocumentType.class);
        return subjectDocumentTypeRepository.save(subjectDocumentType);
    }

    @Override
    public List<SubjectDocumentType> getAllSubjectDocumentTypes() {
        return subjectDocumentTypeRepository.findAll();
    }
}
