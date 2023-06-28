package com.hust.edu.vn.documentsystem.controller.admin;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.dto.UserDto;
import com.hust.edu.vn.documentsystem.data.model.UserModel;
import com.hust.edu.vn.documentsystem.entity.User;
import com.hust.edu.vn.documentsystem.service.UserService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admins/users")
public class AdminUserController {
    private final UserService userService;
    private final ModelMapperUtils modelMapperUtils;

    @Autowired
    public AdminUserController(UserService userService, ModelMapperUtils modelMapperUtils) {
        this.userService = userService;
        this.modelMapperUtils = modelMapperUtils;
    }

    @GetMapping("new")
    public ResponseEntity<CustomResponse> getAllNewUser(){
        List<User> users = userService.getAllNewUser();
        return CustomResponse.generateResponse(HttpStatus.OK, users.stream().map(user -> modelMapperUtils.mapAllProperties(user, UserDto.class)));
    }
    @GetMapping("")
    public ResponseEntity<CustomResponse> getAllUser(){
        List<User> users = userService.getAllForAdminUser();
        return CustomResponse.generateResponse(HttpStatus.OK, users.stream().map(user -> modelMapperUtils.mapAllProperties(user, UserDto.class)));
    }
    @GetMapping("{userId}/profile")
    public ResponseEntity<CustomResponse> getProfileForUser(@PathVariable("userId") Long userId){
        Object[] result = (Object[]) userService.getProfileForUser(userId)[0];
        Object[] subjectDocumentResult = userService.getSubjectDocumentForProfile(userId);
        Map<String, Object> subjectDocumentResultMap = new HashMap<>();
        Long subjectDocumentTotal = 0L;
        for (Object o : subjectDocumentResult) {
            Object[] t = (Object[]) o;
            subjectDocumentTotal += (Long)t[1];
            subjectDocumentResultMap.put(t[0].toString(), t[1]);
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("user", modelMapperUtils.mapAllProperties((User)result[0], UserDto.class));
        resultMap.put("postTotal", result[1]);
        resultMap.put("subjectDocument", subjectDocumentResultMap);
        resultMap.put("subjectDocumentTotal", subjectDocumentTotal);
        resultMap.put("reviewTotal", result[2]);
        return CustomResponse.generateResponse(HttpStatus.OK, resultMap);
    }

    @PostMapping()
    public ResponseEntity<CustomResponse> createUser(@ModelAttribute UserModel userModel){
        boolean status = userService.createUser(userModel);
        return CustomResponse.generateResponse(status);
    }
 }
