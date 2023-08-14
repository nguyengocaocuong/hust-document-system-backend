package com.hust.edu.vn.documentsystem.controller.user;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.dto.SubjectDocumentDto;
import com.hust.edu.vn.documentsystem.data.dto.SubjectDocumentSearchResult;
import com.hust.edu.vn.documentsystem.entity.SubjectDocument;
import com.hust.edu.vn.documentsystem.service.SearchService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users/search")
@Slf4j
public class UserSearchController {
    private final SearchService searchService;

    public UserSearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("search-subject-document")
    public ResponseEntity<CustomResponse> searchSubjectDocument(@RequestParam("key") String key, @RequestParam("institute") List<Long> institute, @RequestParam("subject") List<Long> subject, @RequestParam("semester") List<String> semester, @RequestParam("subjectDocumentType") List<Long> subjectDocumentType, @RequestParam("deepSearch") boolean deepSearch, @RequestParam("fuzzySearch") boolean fuzzySearch) {
        List<SubjectDocumentSearchResult> subjectDocuments = searchService.findAllSubjectDocumentBySearchOption(key,semester,subjectDocumentType,subject,institute, deepSearch, fuzzySearch);
        return CustomResponse.generateResponse(HttpStatus.OK, subjectDocuments);
    }
}
