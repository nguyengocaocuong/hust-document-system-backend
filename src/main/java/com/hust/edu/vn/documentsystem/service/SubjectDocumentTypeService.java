package com.hust.edu.vn.documentsystem.service;

import com.hust.edu.vn.documentsystem.data.model.SubjectDocumentTypeModel;
import com.hust.edu.vn.documentsystem.entity.SubjectDocumentType;

import java.util.Collection;
import java.util.List;

public interface SubjectDocumentTypeService {
    SubjectDocumentType createSubjectDocumentType(SubjectDocumentTypeModel subjectDocumentTypeModel);

    List<SubjectDocumentType> getAllSubjectDocumentTypes();
}
