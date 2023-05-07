package com.hust.edu.vn.documentsystem.controller.general;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.model.*;
import com.hust.edu.vn.documentsystem.entity.Subject;
import com.hust.edu.vn.documentsystem.service.SubjectService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/generals/subjects")
@Tag(name = "Subjects - api")
public class GeneralSubjectController {
    private final SubjectService subjectService;

    @Autowired
    public GeneralSubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }



    @GetMapping("{id}")
    public ResponseEntity<CustomResponse> getSubject(@PathVariable("id") Long id) {
        Subject subject = subjectService.getSubjectById(id);
        return CustomResponse.generateResponse(subject != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST, subject);
    }

    @PostMapping()
    public ResponseEntity<CustomResponse> createSubject(@ModelAttribute SubjectModel subjectModel) {
        Subject subject = subjectService.createSubject(subjectModel);
        return CustomResponse.generateResponse(subject != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST, subject);
    }

    @PatchMapping("document/comment/hidden/{id}")
    public ResponseEntity<CustomResponse> hiddenComment(@PathVariable("id") Long id) {
        boolean status = subjectService.hiddenCommentForSubjectDocument(id);
        return CustomResponse.generateResponse(status);
    }
    @PatchMapping("document/comment/active/{id}")
    public ResponseEntity<CustomResponse> activeComment(@PathVariable("id") Long id) {
        boolean status = subjectService.activeCommentForSubjectDocument(id);
        return CustomResponse.generateResponse(status);
    }

}
