package com.hust.edu.vn.documentsystem.service.impl;

import com.hust.edu.vn.documentsystem.common.type.DocumentType;
import com.hust.edu.vn.documentsystem.common.type.TargetLanguageType;
import com.hust.edu.vn.documentsystem.data.dto.SubjectDocumentSearchResult;
import com.hust.edu.vn.documentsystem.entity.*;
import com.hust.edu.vn.documentsystem.repository.*;
import com.hust.edu.vn.documentsystem.service.GoogleCloudTranslateService;
import com.hust.edu.vn.documentsystem.service.SearchService;
import com.hust.edu.vn.documentsystem.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class SearchServiceImpl implements SearchService {
    private final DocumentRepository documentRepository;
    private final SubjectRepository subjectRepository;
    private final SubjectDocumentTypeRepository subjectDocumentTypeRepository;
    private final SubjectDocumentRepository subjectDocumentRepository;
    private final UserRepository userRepository;
    private final GoogleCloudTranslateService googleCloudTranslateService;

    public SearchServiceImpl(
            SubjectDocumentRepository subjectDocumentRepository,
            UserRepository userRepository, GoogleCloudTranslateService googleCloudTranslateService,
            SubjectDocumentTypeRepository subjectDocumentTypeRepository,
            SubjectRepository subjectRepository,
            DocumentRepository documentRepository) {
        this.subjectDocumentRepository = subjectDocumentRepository;
        this.userRepository = userRepository;
        this.googleCloudTranslateService = googleCloudTranslateService;
        this.subjectDocumentTypeRepository = subjectDocumentTypeRepository;
        this.subjectRepository = subjectRepository;
        this.documentRepository = documentRepository;
    }

    @Override
    public List<SubjectDocumentSearchResult> findAllSubjectDocumentBySearchOption(String key, List<String> semester, List<Long> subjectDocumentType, List<Long> subject, List<Long> institute, boolean deepSearch, boolean fuzzySearch) {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

        if(key == null || key.length() == 0){
            key = null;
        }
        else if(deepSearch){
            key = googleCloudTranslateService.translateText(key.trim(), TargetLanguageType.ENGLISH).get(0);
        }
        List<Object[]> results = subjectDocumentRepository.findAllSubjectDocumentBySearchOption(StringUtils.removeDiacritics(key), semester, semester.size(), subjectDocumentType, subjectDocumentType.size(), subject,subject.size(), institute, institute.size(), user.getId(), deepSearch, fuzzySearch);
        List<SubjectDocumentSearchResult> subjectDocuments = new ArrayList<>();
        results.forEach( objects -> {
            SubjectDocumentSearchResult subjectDocument = new SubjectDocumentSearchResult();
            subjectDocument.setId((Long) objects[0]);
            Document document = documentRepository.findById((Long) objects[10]).orElse(null);
            subjectDocument.setType(DocumentType.getDocumentTypeFromContentType(document.getContentType()));
            subjectDocument.setDescription((String) objects[2]);
            subjectDocument.setCreatedAt((Date) objects[3]);
            subjectDocument.setSemester((String) objects[4]);
            subjectDocument.setTotalDownload((Long) objects[5]);
            subjectDocument.setTotalView((Long) objects[6]);
            SubjectDocumentType sdt = subjectDocumentTypeRepository.findById((Long) objects[7]).orElse(null);
            subjectDocument.setSubjectDocumentTypeId(sdt.getId());
            subjectDocument.setSubjectDocumentTypeName(sdt.getName());
            subjectDocument.setSubjectDocumentType(sdt.getType().toString());
            Subject s = subjectRepository.findById((Long) objects[8]).orElse(null);
            subjectDocument.setSubjectId(s.getId());
            subjectDocument.setSubjectName(s.getName());
            subjectDocument.setSubjectCode(s.getSubjectCode());
            subjectDocument.setInstitute(s.getInstitute().getInstitute());
            User owner = userRepository.findById((Long) objects[9]).orElse(null);
            subjectDocument.setOwnerId(owner.getId());
            subjectDocument.setOwnerFirstName(owner.getFirstName());
            subjectDocument.setOwnerLastName(owner.getLastName());
            subjectDocument.setThumbnail(document.getThumbnail());
            subjectDocument.setTotalComments((Long) objects[11]);
            subjectDocument.setTotalAnswers((Long) objects[12]);
            subjectDocument.setTotalFavorites((Long) objects[13]);

            subjectDocuments.add(subjectDocument);
        });

        return subjectDocuments;
    }
}
