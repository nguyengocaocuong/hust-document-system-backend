package com.hust.edu.vn.documentsystem.controller.user;

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

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    private final ModelMapperUtils modelMapperUtils;

    @Autowired
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
        boolean status = userService.updateProfile(userModel);
        return CustomResponse.generateResponse(status ? HttpStatus.OK : HttpStatus.CONFLICT);
    }
}
