package com.hust.edu.vn.documentsystem.controller.user;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.dto.AnswerSubjectDocumentDto;
import com.hust.edu.vn.documentsystem.data.model.AnswerSubjectDocumentModel;
import com.hust.edu.vn.documentsystem.entity.AnswerSubjectDocument;
import com.hust.edu.vn.documentsystem.entity.Document;
import com.hust.edu.vn.documentsystem.service.AnswerSubjectDocumentService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users/subjects/subjectDocument/{subjectDocumentId}/answerSubjectDocument")
public class UserAnswerSubjectDocumentController {
    private final AnswerSubjectDocumentService  answerSubjectDocumentService;
    private final ModelMapperUtils modelMapperUtils;

    public UserAnswerSubjectDocumentController(AnswerSubjectDocumentService answerSubjectDocumentService, ModelMapperUtils modelMapperUtils) {
        this.answerSubjectDocumentService = answerSubjectDocumentService;
        this.modelMapperUtils = modelMapperUtils;
    }

    @PostMapping()
    public ResponseEntity<CustomResponse> uploadAnswerForSubjectDocument(
            @PathVariable("subjectDocumentId") Long subjectDocumentId,
            @ModelAttribute AnswerSubjectDocumentModel answerSubjectDocumentModel) {
        AnswerSubjectDocument answerSubjectDocument = answerSubjectDocumentService.saveAnswerForSubjectDocument(subjectDocumentId,
                answerSubjectDocumentModel);
        answerSubjectDocument.setSubjectDocument(null);
        return CustomResponse.generateResponse(HttpStatus.OK,
                modelMapperUtils.mapAllProperties(answerSubjectDocument, AnswerSubjectDocumentDto.class));
    }
    @GetMapping()
    public ResponseEntity<CustomResponse> getAllAnswerSubjectDocument(
            @PathVariable("subjectDocumentId") Long subjectDocumentId) {
        List<AnswerSubjectDocument> answerSubjectDocuments = answerSubjectDocumentService
                .getAllAnswerSubjectDocument(subjectDocumentId);
        return CustomResponse.generateResponse(HttpStatus.OK, answerSubjectDocuments.stream()
                .map(answer -> modelMapperUtils.mapAllProperties(answer, AnswerSubjectDocumentDto.class)));
    }

    @GetMapping("{answerSubjectDocumentId}/readFile")
    public ResponseEntity<Resource> readAnswerSubjectDocument(
            @PathVariable("subjectDocumentId") Long subjectDocumentId, @PathVariable("answerSubjectDocumentId") Long answerSubjectDocumentId) {
        List<Object> data = answerSubjectDocumentService.readAnswerSubjectDocument(answerSubjectDocumentId, subjectDocumentId);
        if (data == null || ((byte[]) data.get(1)).length == 0)
            return ResponseEntity.notFound().build();
        Document document = (Document) data.get(0);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(document.getContentType()));
        headers.setContentDisposition(ContentDisposition.attachment().filename(document.getName()).build());
        return ResponseEntity.ok()
                .headers(headers)
                .body(new ByteArrayResource((byte[]) data.get(1)));
    }
    @PostMapping("annotation")
    public ResponseEntity<CustomResponse> createAnnotationForSubjectDocument(@PathVariable("subjectDocumentId") Long subjectDocumentId, @ModelAttribute AnswerSubjectDocumentModel answerSubjectDocumentModel){
        try {
            AnswerSubjectDocument answerSubjectDocument = answerSubjectDocumentService.createAnnotationForSubjectDocument(subjectDocumentId, answerSubjectDocumentModel);
            if(answerSubjectDocument == null) return CustomResponse.generateResponse(HttpStatus.BAD_REQUEST);
            return CustomResponse.generateResponse(HttpStatus.CREATED, modelMapperUtils.mapAllProperties(answerSubjectDocument, AnswerSubjectDocumentDto.class));
        } catch (IOException e) {
            return CustomResponse.generateResponse(HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping("{answerSubjectDocumentId}")
    public ResponseEntity<CustomResponse> deleteAnswerSubjectDocument(@PathVariable("answerSubjectDocumentId") Long answerSubjectDocumentId, @PathVariable("subjectDocumentId") Long subjectDocumentID){
        return CustomResponse.generateResponse(answerSubjectDocumentService.deleteAnswerSubjectDocument(answerSubjectDocumentId, subjectDocumentID));
    }
}
