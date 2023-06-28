package com.hust.edu.vn.documentsystem.controller.user;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.dto.CommentSubjectDocumentDto;
import com.hust.edu.vn.documentsystem.data.model.CommentSubjectDocumentModel;
import com.hust.edu.vn.documentsystem.entity.CommentSubjectDocument;
import com.hust.edu.vn.documentsystem.service.SubjectService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users/subjects/subjectDocument/{subjectDocumentId}/comment")
@Slf4j
public class UserCommentSubjectDocumentController {
    private final SubjectService subjectService;
    private final ModelMapperUtils modelMapperUtils;

    public UserCommentSubjectDocumentController(SubjectService subjectService, ModelMapperUtils modelMapperUtils) {
        this.subjectService = subjectService;
        this.modelMapperUtils = modelMapperUtils;
    }

    @PostMapping()
    public ResponseEntity<CustomResponse> createComment(@ModelAttribute CommentSubjectDocumentModel commentSubjectDocumentModel, @PathVariable("subjectDocumentId") Long subjectDocumentId) {
        log.info(commentSubjectDocumentModel.toString());
        CommentSubjectDocument comment = subjectService.createCommentForSubjectDocument(commentSubjectDocumentModel, subjectDocumentId);
        return CustomResponse.generateResponse(comment != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST, comment != null ? modelMapperUtils.mapAllProperties(comment, CommentSubjectDocumentDto.class) : null);
    }
    @GetMapping()
    public ResponseEntity<CustomResponse> getSubjectDocumentComment(@PathVariable("subjectDocumentId") Long subjectDocumentId) {
        List<CommentSubjectDocument> subjectDocuments = subjectService.getSubjectDocumentCommentBySubjectDocumentId(subjectDocumentId);
        return CustomResponse.generateResponse(HttpStatus.OK, subjectDocuments.stream().map(subjectDocument -> modelMapperUtils.mapAllProperties(subjectDocument, CommentSubjectDocumentDto.class)));
    }
    @DeleteMapping("{commentId}")
    public ResponseEntity<CustomResponse> deleteCommentSubjectDocument(@PathVariable("subjectDocumentId") Long subjectDocumentId, @PathVariable("commentId") Long commentId){
        boolean status = subjectService.deleteCommentSubjectDocument(subjectDocumentId, commentId);
        return CustomResponse.generateResponse(status);
    }
    @PatchMapping("{commentId}")
    public ResponseEntity<CustomResponse> updateCommentSubjectDocument(@PathVariable("subjectDocumentId") Long subjectDocumentId, @PathVariable("commentId") Long commentId, @ModelAttribute CommentSubjectDocumentModel commentSubjectDocumentModel){
        CommentSubjectDocument commentSubjectDocument = subjectService.updateCommentForSubjectDocument(commentSubjectDocumentModel, subjectDocumentId, commentId);
        if (commentSubjectDocument == null) return CustomResponse.generateResponse(HttpStatus.NOT_FOUND);
        return CustomResponse.generateResponse(HttpStatus.OK, modelMapperUtils.mapAllProperties(commentSubjectDocument, CommentSubjectDocumentDto.class));
    }
    @PatchMapping("{commentId}/hidden")
    public ResponseEntity<CustomResponse> hiddenCommentSubjectDocument(@PathVariable("subjectDocumentId") Long subjectDocumentId, @PathVariable("commentId") Long commentId){
        boolean status = subjectService.hiddenCommentForSubjectDocument(commentId,subjectDocumentId);
        return CustomResponse.generateResponse(status);
    }
}
