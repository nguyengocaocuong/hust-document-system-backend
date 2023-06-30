package com.hust.edu.vn.documentsystem.controller.admin;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.service.PostService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/admins/posts")
public class AdminPostController {
    private final PostService postService;

    
    public AdminPostController(PostService postService) {
        this.postService = postService;
    }



    @PatchMapping("hidden/{id}")
    public ResponseEntity<CustomResponse> hiddenPost(@PathVariable("id") Long id) {
        boolean status = postService.hiddenPost(id);
        return CustomResponse.generateResponse(status);
    }

    @PatchMapping("active/{id}")
    public ResponseEntity<CustomResponse> activePost(@PathVariable("id") Long id) {
        boolean status = postService.activePost(id);
        return CustomResponse.generateResponse(status ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }


}
