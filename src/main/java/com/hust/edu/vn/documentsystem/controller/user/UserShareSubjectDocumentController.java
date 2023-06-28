package com.hust.edu.vn.documentsystem.controller.user;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.dto.UserDto;
import com.hust.edu.vn.documentsystem.data.model.ShareSubjectDocumentModel;
import com.hust.edu.vn.documentsystem.entity.User;
import com.hust.edu.vn.documentsystem.service.SubjectService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;

@RestController
@RequestMapping("/api/v1/users/subjects/subjectDocument/{subjectDocumentId}")
public class UserShareSubjectDocumentController {
    private final SubjectService subjectService;
    private final ModelMapperUtils modelMapperUtils;

    public UserShareSubjectDocumentController(SubjectService subjectService, ModelMapperUtils modelMapperUtils) {
		this.subjectService = subjectService;
		this.modelMapperUtils = modelMapperUtils;
	}

	@GetMapping("shared")
    public ResponseEntity<CustomResponse> getSharedUser(@PathVariable("subjectDocumentId") Long subjectDocumentId) {
        List<User> users = subjectService.getAllUserShared(subjectDocumentId);
        return CustomResponse.generateResponse(HttpStatus.OK,
                users.stream().map(user -> modelMapperUtils.mapAllProperties(user, UserDto.class)));
    }
    @PostMapping("share")
    public ResponseEntity<CustomResponse> shareSubjectDocument(
            @PathVariable("subjectDocumentId") Long subjectDocumentId,
            @ModelAttribute ShareSubjectDocumentModel shareSubjectDocumentModel) {
        Object object = subjectService.shareDocument(subjectDocumentId, shareSubjectDocumentModel);
        if (object == null)
            return CustomResponse.generateResponse(HttpStatus.CONFLICT);
        return CustomResponse.generateResponse(HttpStatus.OK, object);
    }
        @DeleteMapping("shared/{sharedId}")
    public ResponseEntity<CustomResponse> clearSharedSubjectDocument(@PathVariable("sharedId") Long sharedId, @PathVariable("subjectDocumentId") Long subjectDocumentId) {
        boolean status = subjectService.clearSharedPrivateSubjectDocument(sharedId, subjectDocumentId);
        return CustomResponse.generateResponse(status);
    }
}
