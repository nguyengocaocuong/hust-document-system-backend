package com.hust.edu.vn.documentsystem.controller;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.dto.UserDto;
import com.hust.edu.vn.documentsystem.entity.User;
import com.hust.edu.vn.documentsystem.service.UserService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/public/users")
public class PublicUserController {
    private final UserService userService;
    private final ModelMapperUtils modelMapperUtils;

    @Autowired
    public PublicUserController(UserService userService, ModelMapperUtils modelMapperUtils) {
        this.userService = userService;
        this.modelMapperUtils = modelMapperUtils;
    }

    @GetMapping("{id}")
    public ResponseEntity<CustomResponse> getProfileById(@PathVariable("id") Long userId){
        User user = userService.getProfileUserById(userId);
        return CustomResponse.generateResponse(user == null ? HttpStatus.NOT_FOUND : HttpStatus.OK, modelMapperUtils.mapAllProperties(user, UserDto.class));
    }

    @GetMapping()
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<CustomResponse> getAllUser(){
        List<User> users = userService.getAllUser();
        return CustomResponse.generateResponse(HttpStatus.OK, users.stream().map(user -> modelMapperUtils.mapAllProperties(user, UserDto.class)));
    }
}
