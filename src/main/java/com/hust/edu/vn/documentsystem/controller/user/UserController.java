package com.hust.edu.vn.documentsystem.controller.user;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.dto.UserDto;
import com.hust.edu.vn.documentsystem.data.model.UserModel;
import com.hust.edu.vn.documentsystem.entity.User;
import com.hust.edu.vn.documentsystem.service.UserService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.nio.file.Path;

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
    @GetMapping("document")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<Resource> getFile(){
        Path pdf = Path.of("docs/doc.docx");
        try {
            Resource pdfResource = new UrlResource(pdf.toUri());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.valueOf("application/vnd.openxmlformats-officedocument.wordprocessingml.document"));
            headers.setContentDispositionFormData("doc.docx", "doc.docx");
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfResource);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
