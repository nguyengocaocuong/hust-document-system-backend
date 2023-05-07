package com.hust.edu.vn.documentsystem.controller.user;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.dto.CommentSubjectDocumentDto;
import com.hust.edu.vn.documentsystem.data.dto.SubjectDto;
import com.hust.edu.vn.documentsystem.data.model.*;
import com.hust.edu.vn.documentsystem.entity.CommentSubjectDocument;
import com.hust.edu.vn.documentsystem.entity.FavoriteSubject;
import com.hust.edu.vn.documentsystem.entity.Subject;
import com.hust.edu.vn.documentsystem.entity.SubjectDocument;
import com.hust.edu.vn.documentsystem.service.SubjectService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users/subjects")
@Tag(name = "Subjects - api")
public class UserSubjectController {
    private final SubjectService subjectService;
    private final ModelMapperUtils modelMapperUtils;

    @Autowired
    public UserSubjectController(SubjectService subjectService, ModelMapperUtils modelMapperUtils) {
        this.modelMapperUtils = modelMapperUtils;
        this.subjectService = subjectService;
    }


    @GetMapping("owner")
    public ResponseEntity<CustomResponse> getAllSubjectsCreateByUser() {
        Object content = subjectService.getAllSubjectsCreateByUser().stream().map(subject -> modelMapperUtils.mapAllProperties(subject, SubjectDto.class)).toList();
        return CustomResponse.generateResponse(HttpStatus.OK, "Danh sách các môn học do bạn tạo ", content);
    }


    @PatchMapping()
    public ResponseEntity<CustomResponse> updateSubject(@ModelAttribute SubjectModel subjectModel) {
        boolean status = subjectService.updateSubject(subjectModel);
        return CustomResponse.generateResponse(status);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<CustomResponse> deleteSubject(@PathVariable("id") Long subjectId) {
        boolean isDeleted = subjectService.deleteSubject(subjectId);
        return CustomResponse.generateResponse(isDeleted);
    }

    @PostMapping("document")
    public ResponseEntity<CustomResponse> uploadDocumentForSubject(@ModelAttribute SubjectDocumentModel subjectDocumentModel) {
        SubjectDocument subjectDocument = subjectService.saveDocumentForSubject(subjectDocumentModel);
        subjectDocument.setSubject(null);
        subjectDocument.setOwner(null);
        return CustomResponse.generateResponse(HttpStatus.OK, subjectDocument);
    }

    @PatchMapping("document")
    public ResponseEntity<CustomResponse> updateDocumentForSubject(@ModelAttribute SubjectDocumentModel subjectDocumentModel) {
        boolean status = subjectService.updateDocumentForSubject(subjectDocumentModel);
        return CustomResponse.generateResponse(status);
    }
    @DeleteMapping("document")
    public ResponseEntity<CustomResponse> deleteDocumentFoSubject(@ModelAttribute SubjectDocumentModel subjectDocumentModel){
        boolean status = subjectService.deleteDocumentForSubject(subjectDocumentModel);
        return CustomResponse.generateResponse(status);
    }

    @PostMapping("sharing")
    public ResponseEntity<CustomResponse> shareDocumentPublic(@ModelAttribute ShareSubjectDocumentModel shareSubjectDocumentModel) {
        Object shareDocument = subjectService.shareDocument(shareSubjectDocumentModel);
        return CustomResponse.generateResponse(shareDocument != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST, shareDocument);
    }

    @PostMapping("favorite")
    public ResponseEntity<CustomResponse> favoriteSubject(@ModelAttribute FavoriteSubjectModel favoriteSubjectModel) {
        boolean status = subjectService.favoriteSubject(favoriteSubjectModel);
        return CustomResponse.generateResponse(status);
    }

    @GetMapping("favorite")
    public ResponseEntity<CustomResponse> getFavoriteSubjects() {
        List<FavoriteSubject> content = subjectService.getFavoriteSubjects();
        List<Subject> subjects = content.stream().map(FavoriteSubject::getSubject).toList();
        return CustomResponse.generateResponse(HttpStatus.OK, "Danh sách các môn học mà bạn yêu thích", subjects.stream().map(subject -> {subject.setOwner(null); return modelMapperUtils.mapAllProperties(subject, SubjectDto.class);}).toList());
    }

    @DeleteMapping("favorite/{id}")
    public ResponseEntity<CustomResponse> unFavoriteSubject(@PathVariable("id") Long subjectId) {
        boolean status = subjectService.unFavoriteSubject(subjectId);
        return CustomResponse.generateResponse(status);
    }

    @PatchMapping("favorite")
    public ResponseEntity<CustomResponse> updateFavoriteSubject(@ModelAttribute FavoriteSubjectModel favoriteSubjectModel) {
        boolean status = subjectService.updateFavoriteSubject(favoriteSubjectModel);
        return CustomResponse.generateResponse(status);
    }

    @PostMapping("document/comment")
    public ResponseEntity<CustomResponse> createComment(@ModelAttribute CommentSubjectDocumentModel commentSubjectDocumentModel)    {
        CommentSubjectDocument comment = subjectService.createCommentForSubjectDocument(commentSubjectDocumentModel);
        return CustomResponse.generateResponse(comment != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST, modelMapperUtils.mapAllProperties(comment, CommentSubjectDocumentDto.class));
    }

    @PatchMapping("document/comment")
    public ResponseEntity<CustomResponse> updateComment(@ModelAttribute CommentSubjectDocumentModel commentSubjectDocumentModel) {
        boolean status = subjectService.updateCommentForSubjectDocument(commentSubjectDocumentModel);
       return CustomResponse.generateResponse(status);
    }

    @DeleteMapping("document/comment/{id}")
    public ResponseEntity<CustomResponse> deleteComment(@PathVariable("id") Long id) {
        boolean status = subjectService.deleteCommentForSubjectDocument(id);
        return CustomResponse.generateResponse(status);
    }

}
