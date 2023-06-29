package com.hust.edu.vn.documentsystem.controller.user;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.dto.AnswerSubjectDocumentDto;
import com.hust.edu.vn.documentsystem.data.dto.SubjectDocumentDto;
import com.hust.edu.vn.documentsystem.entity.AnswerSubjectDocument;
import com.hust.edu.vn.documentsystem.entity.SubjectDocument;
import com.hust.edu.vn.documentsystem.service.UserService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users/recommend")
public class UserRecommendController {
    private final UserService userService;
    private final ModelMapperUtils modelMapperUtils;

    public UserRecommendController(UserService userService, ModelMapperUtils modelMapperUtils) {
        this.userService = userService;
        this.modelMapperUtils = modelMapperUtils;
    }

    @GetMapping()
    public ResponseEntity<CustomResponse> getRecommendFavorite(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "5") int size) {
        Object[] recommend = userService.getObjectForRecommend(page, size);
        @SuppressWarnings("unchecked")
        List<SubjectDocument> subjectDocuments = (List<SubjectDocument>) recommend[0];
        @SuppressWarnings("unchecked")
        List<AnswerSubjectDocument> answerSubjectDocuments = (List<AnswerSubjectDocument>) recommend[1];
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("subjectDocuments", subjectDocuments.stream()
                .map(subjectDocument -> modelMapperUtils.mapAllProperties(subjectDocument, SubjectDocumentDto.class)));
        resultMap.put("answerSubjectDocuments",
                answerSubjectDocuments.stream().map(answerSubjectDocument -> modelMapperUtils
                        .mapAllProperties(answerSubjectDocument, AnswerSubjectDocumentDto.class)));
        return CustomResponse.generateResponse(HttpStatus.OK, resultMap);
    }
}
