package com.hust.edu.vn.documentsystem.controller.user;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.dto.CommentSubjectDocumentDto;
import com.hust.edu.vn.documentsystem.data.dto.SubjectDto;
import com.hust.edu.vn.documentsystem.data.dto.UserDto;
import com.hust.edu.vn.documentsystem.data.model.*;
import com.hust.edu.vn.documentsystem.entity.*;
import com.hust.edu.vn.documentsystem.repository.DocumentRepository;
import com.hust.edu.vn.documentsystem.service.SubjectService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users/subjects")
@Tag(name = "Subjects - api")
public class UserSubjectController {
    private final SubjectService subjectService;
    private final ModelMapperUtils modelMapperUtils;
    private final ResourceLoader resourceLoader;
    private final DocumentRepository documentRepository;

    @Autowired
    public UserSubjectController(SubjectService subjectService, ModelMapperUtils modelMapperUtils, ResourceLoader resourceLoader,
                                 DocumentRepository documentRepository) {
        this.modelMapperUtils = modelMapperUtils;
        this.subjectService = subjectService;
        this.resourceLoader = resourceLoader;
        this.documentRepository = documentRepository;
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
    public ResponseEntity<CustomResponse> deleteDocumentFoSubject(@ModelAttribute SubjectDocumentModel subjectDocumentModel) {
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
        return CustomResponse.generateResponse(HttpStatus.OK, "Danh sách các môn học mà bạn yêu thích", subjects.stream().map(subject -> {
            subject.setOwner(null);
            return modelMapperUtils.mapAllProperties(subject, SubjectDto.class);
        }).toList());
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
    public ResponseEntity<CustomResponse> createComment(@ModelAttribute CommentSubjectDocumentModel commentSubjectDocumentModel) {
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

    @GetMapping("allUserShareWithMe")
    public ResponseEntity<CustomResponse> getAllUserShareWithMe() {
        List< User> users = subjectService.getAllUserShareWithMe();
        return CustomResponse.generateResponse(HttpStatus.OK, users.stream().map(user -> modelMapperUtils.mapAllProperties(user, UserDto.class)).toList());
    }

    @GetMapping("allSubjects")
    public ResponseEntity<CustomResponse> getAllSubjects(){
        return CustomResponse.generateResponse(HttpStatus.OK,subjectService.getAllSubjects() );
    }
    @GetMapping("subjectDetail/{subjectId}")
    public ResponseEntity<CustomResponse> getSubjectDetail(@PathVariable Long subjectId){
        Subject subject = subjectService.getSubjectById(subjectId);
        return CustomResponse.generateResponse(HttpStatus.OK, "Thông tin chi tiết môn học", modelMapperUtils.mapAllProperties(subject, SubjectDto.class));
    }

    @GetMapping("allSubjectForFilter")
    public  ResponseEntity<CustomResponse> getAllSubjectForFilter(){
        return CustomResponse.generateResponse(HttpStatus.OK, subjectService.getAllSubjectsForFilter());
    }
    @GetMapping("allSemesterForFilter")
    public ResponseEntity<CustomResponse> getAllSemesterForFilter(){
        return CustomResponse.generateResponse(HttpStatus.OK,subjectService.findAllSemesterForFilter() );
    }

    @GetMapping("subjectDocumentTypeForFilter")
    public ResponseEntity<CustomResponse> getAllSubjectDocumentType(){
        return CustomResponse.generateResponse(HttpStatus.OK,subjectService.findAllSubjectDocumentType() );
    }
    @GetMapping("subjectDocuments/readFile/{subjectDocumentId}")
    public ResponseEntity<byte[]> readSubjectDocument(@PathVariable("subjectDocumentId") Long id){
//        byte[] bytes = subjectService.readSubjectDocument(subjectDocumentId);
        Document document = documentRepository.findById(id).orElse(null);
        Map<String, String> CONTENT_TYPES = new HashMap<>();

        CONTENT_TYPES.put("bmp", "image/bmp");
        CONTENT_TYPES.put("csv", "text/csv");
        CONTENT_TYPES.put("odt", "application/vnd.oasis.opendocument.text");
        CONTENT_TYPES.put("doc", "application/msword");
        CONTENT_TYPES.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        CONTENT_TYPES.put("gif", "image/gif");
        CONTENT_TYPES.put("htm", "text/htm");
        CONTENT_TYPES.put("html", "text/html");
        CONTENT_TYPES.put("jpg", "image/jpg");
        CONTENT_TYPES.put("jpeg", "image/jpeg");
        CONTENT_TYPES.put("pdf", "application/pdf");
        CONTENT_TYPES.put("png", "image/png");
        CONTENT_TYPES.put("ppt", "application/vnd.ms-powerpoint");
        CONTENT_TYPES.put("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");
        CONTENT_TYPES.put("tiff", "image/tiff");
        CONTENT_TYPES.put("txt", "text/plain");
        CONTENT_TYPES.put("xls", "application/vnd.ms-excel");
        CONTENT_TYPES.put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        Resource resource = resourceLoader.getResource(document.getPath());
        try {
            byte[] bytes = resource.getContentAsByteArray();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.valueOf(CONTENT_TYPES.get(document.getContentType())));
            headers.setContentDisposition(ContentDisposition.attachment().filename(document.getName()).build());
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(bytes);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(null);
        }


    }
}
