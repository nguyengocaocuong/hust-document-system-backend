package com.hust.edu.vn.documentsystem.controller.user;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.dto.SubjectDocumentDto;
import com.hust.edu.vn.documentsystem.data.dto.UserDto;
import com.hust.edu.vn.documentsystem.data.model.UserModel;
import com.hust.edu.vn.documentsystem.entity.SubjectDocument;
import com.hust.edu.vn.documentsystem.entity.User;
import com.hust.edu.vn.documentsystem.service.UserService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    private final ModelMapperUtils modelMapperUtils;


    public UserController(UserService userService, ModelMapperUtils modelMapperUtils) {
        this.userService = userService;
        this.modelMapperUtils = modelMapperUtils;
    }

    @GetMapping()
    public ResponseEntity<CustomResponse> getProfile() {
        User user = userService.getProfile();
        return CustomResponse.generateResponse(user == null ? HttpStatus.NOT_FOUND : HttpStatus.OK, "Dưới đây là thông tin cá nhân của bạn", modelMapperUtils.mapAllProperties(user, UserDto.class));
    }

    @PatchMapping()
    public ResponseEntity<CustomResponse> updateProfile(@ModelAttribute UserModel userModel) {
        User status = userService.updateProfile(userModel);
        return CustomResponse.generateResponse(HttpStatus.OK, modelMapperUtils.mapAllProperties(status, UserDto.class));
    }

    @GetMapping("allUserForFilter")
    public ResponseEntity<CustomResponse> getAllUserForFilter() {
        List<User> users = userService.getAllUser();
        return CustomResponse.generateResponse(HttpStatus.OK, users.stream().map(user -> modelMapperUtils.mapAllProperties(user, UserDto.class)));
    }

    @GetMapping("trash")
    public ResponseEntity<CustomResponse> getAllTrash() {
        List<SubjectDocument> subjectDocuments = userService.getAllSubjectDocumentTrash();
        return CustomResponse.generateResponse(HttpStatus.OK, subjectDocuments.stream().map(subjectDocument -> modelMapperUtils.mapAllProperties(subjectDocument, SubjectDocumentDto.class)));
    }

    @PatchMapping("update-avatar")
    public ResponseEntity<CustomResponse> updateAvatar(@ModelAttribute UserModel userModel) {
        String avatarUrl = userService.updateAvatar(userModel.getAvatarFile());
        if (avatarUrl == null) return CustomResponse.generateResponse(HttpStatus.BAD_GATEWAY);
        Map<String, String> result = new HashMap<>();
        result.put("avatarUrl", avatarUrl);
        return CustomResponse.generateResponse(HttpStatus.OK, result);
    }

    @PatchMapping("update-user-info")
    public ResponseEntity<CustomResponse> updateUserInfo(@ModelAttribute UserModel userModel) {
        User user = userService.updateUserInfo(userModel);
        if(user == null) return CustomResponse.generateResponse(HttpStatus.CONFLICT);
        return CustomResponse.generateResponse(HttpStatus.OK,modelMapperUtils.mapAllProperties(user, UserDto.class));
    }

    @PatchMapping("update-account-info")
    public ResponseEntity<CustomResponse> updateAccountInfo(@ModelAttribute UserModel userModel) {
        User user = userService.updateAccountInfo(userModel);
        if(user == null) return CustomResponse.generateResponse(HttpStatus.CONFLICT);
        return CustomResponse.generateResponse(HttpStatus.OK,modelMapperUtils.mapAllProperties(user, UserDto.class));

    }
    @GetMapping("profile")
    public ResponseEntity<CustomResponse> getUserProfile(@RequestParam("userId") Long userId){
        User user = userService.getUserProfile(userId);
        if(user == null)
                return  CustomResponse.generateResponse(HttpStatus.NOT_FOUND);
        return CustomResponse.generateResponse(HttpStatus.OK, modelMapperUtils.mapAllProperties(user, UserDto.class));
    }

    @GetMapping("wrote")
    public ResponseEntity<CustomResponse> getAllWroteByUserId(@RequestParam("userId") Long userId){
        Map<String, Object> result = userService.getAllWroteByUserId(userId);
        return CustomResponse.generateResponse(HttpStatus.OK, result);
    }

}
