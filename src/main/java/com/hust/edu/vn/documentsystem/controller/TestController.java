package com.hust.edu.vn.documentsystem.controller;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.service.FirebaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

record Message(String token, String data) {
    public String getToken() {
        return token;
    }

    public String getData() {
        return data;
    }
}

@RestController()
@RequestMapping("/api/v1/public")
public class TestController {
    private final FirebaseService firebaseService;

    private final ResourceLoader resourceLoader;

    @Autowired
    public TestController(FirebaseService firebaseService, ResourceLoader resourceLoader) {
        this.firebaseService = firebaseService;
        this.resourceLoader = resourceLoader;
    }

    @PostMapping("test/sendMessage")
    public ResponseEntity<CustomResponse> sendMessage(@ModelAttribute Message message) {
        try {
            firebaseService.sendMessage(message.getToken(), "hello", "hello", Map.of("user", "nguyen ngo cao cuong"));
            return CustomResponse.generateResponse(true);

        } catch (FirebaseMessagingException e) {
            return CustomResponse.generateResponse(false);
        }
    }

    @GetMapping()
    public String test() {
        return "Rea";
    }

    @GetMapping("{type}")
    public ResponseEntity<Resource> getFileHead(@PathVariable(name = "type") String type) {
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
        Resource resource = resourceLoader.getResource("classpath:doc." + type);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(CONTENT_TYPES.get(type)));
        headers.setContentDisposition(ContentDisposition.attachment().filename("doc." + type).build());
        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);

    }
}
