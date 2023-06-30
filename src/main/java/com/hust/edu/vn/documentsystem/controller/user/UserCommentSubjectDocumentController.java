package com.hust.edu.vn.documentsystem.controller.user;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.dto.CommentSubjectDocumentDto;
import com.hust.edu.vn.documentsystem.data.model.CommentSubjectDocumentModel;
import com.hust.edu.vn.documentsystem.entity.CommentSubjectDocument;
import com.hust.edu.vn.documentsystem.service.PusherService;
import com.hust.edu.vn.documentsystem.service.SubjectService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users/subjects/subjectDocument/{subjectDocumentId}/comment")
public class UserCommentSubjectDocumentController {
    private final SubjectService subjectService;
    private final ModelMapperUtils modelMapperUtils;
    private final PusherService pusherService;

    public UserCommentSubjectDocumentController(SubjectService subjectService, ModelMapperUtils modelMapperUtils,
            PusherService pusherService) {
        this.subjectService = subjectService;
        this.modelMapperUtils = modelMapperUtils;
        this.pusherService = pusherService;
    }

    @PostMapping()
    public ResponseEntity<CustomResponse> createComment(
            @ModelAttribute CommentSubjectDocumentModel commentSubjectDocumentModel,
            @PathVariable("subjectDocumentId") Long subjectDocumentId) {
        CommentSubjectDocument comment = subjectService.createCommentForSubjectDocument(commentSubjectDocumentModel,
                subjectDocumentId);
        if (comment == null)
            return CustomResponse.generateResponse(HttpStatus.CONFLICT);
        CommentSubjectDocumentDto commentSubjectDocumentDto = modelMapperUtils.mapAllProperties(comment,
                CommentSubjectDocumentDto.class);
        pusherService.triggerChanel("comment-subject-document-" + subjectDocumentId, "new-comment",
                commentSubjectDocumentDto);
        return CustomResponse.generateResponse(HttpStatus.OK, commentSubjectDocumentDto);
    }

    @GetMapping()
    public ResponseEntity<CustomResponse> getSubjectDocumentComment(
            @PathVariable("subjectDocumentId") Long subjectDocumentId) {
        List<CommentSubjectDocument> subjectDocuments = subjectService
                .getSubjectDocumentCommentBySubjectDocumentId(subjectDocumentId);
        return CustomResponse.generateResponse(HttpStatus.OK,
                subjectDocuments.stream().map(subjectDocument -> modelMapperUtils.mapAllProperties(subjectDocument,
                        CommentSubjectDocumentDto.class)));
    }

    @DeleteMapping("{commentId}")
    public ResponseEntity<CustomResponse> deleteCommentSubjectDocument(
            @PathVariable("subjectDocumentId") Long subjectDocumentId, @PathVariable("commentId") Long commentId) {
        boolean status = subjectService.deleteCommentSubjectDocument(subjectDocumentId, commentId);
        if (status)
            pusherService.triggerChanel("comment-subject-document-" + subjectDocumentId, "delete-comment",
                    commentId);
        return CustomResponse.generateResponse(status);
    }

    @PatchMapping("{commentId}")
    public ResponseEntity<CustomResponse> updateCommentSubjectDocument(
            @PathVariable("subjectDocumentId") Long subjectDocumentId, @PathVariable("commentId") Long commentId,
            @ModelAttribute CommentSubjectDocumentModel commentSubjectDocumentModel) {
        CommentSubjectDocument commentSubjectDocument = subjectService
                .updateCommentForSubjectDocument(commentSubjectDocumentModel, subjectDocumentId, commentId);
        if (commentSubjectDocument == null)
            return CustomResponse.generateResponse(HttpStatus.NOT_FOUND);
        CommentSubjectDocumentDto commentSubjectDocumentDto = modelMapperUtils.mapAllProperties(commentSubjectDocument,
                CommentSubjectDocumentDto.class);
        pusherService.triggerChanel("comment-subject-document-" + subjectDocumentId, "edit-comment",
                commentSubjectDocumentDto);
        return CustomResponse.generateResponse(HttpStatus.OK, commentSubjectDocumentDto);
    }

    @PatchMapping("{commentId}/hidden")
    public ResponseEntity<CustomResponse> hiddenCommentSubjectDocument(
            @PathVariable("subjectDocumentId") Long subjectDocumentId, @PathVariable("commentId") Long commentId) {
        boolean status = subjectService.hiddenCommentForSubjectDocument(commentId, subjectDocumentId);
        if (status)
            pusherService.triggerChanel("comment-subject-document-" + subjectDocumentId, "hidden-comment",
                    commentId);
        return CustomResponse.generateResponse(status);
    }
}
