package com.hust.edu.vn.documentsystem.controller.user;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/public/filter")
public class PublicFilterController {
    @GetMapping()
    public ResponseEntity<CustomResponse> getAllFilter(){
        return null;
    }
}
