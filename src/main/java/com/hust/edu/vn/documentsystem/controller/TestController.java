package com.hust.edu.vn.documentsystem.controller;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.service.FirebaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

record Message(String token, String data){
    public String getToken( ){ return token; }
    public String getData( ){ return data; }
}

@RestController()
@RequestMapping("/api/v1/public")
public class TestController {
    private final FirebaseService firebaseService;

    @Autowired
    public TestController(FirebaseService firebaseService) {
        this.firebaseService = firebaseService;
    }

    @PostMapping("test/sendMessage")
    public ResponseEntity<CustomResponse> sendMessage(@ModelAttribute Message message) {
        try {
            firebaseService.sendMessage(message.getToken(),"hello", "hello", Map.of("user","nguyen ngo cao cuong"));
            return CustomResponse.generateResponse(true);

        } catch (FirebaseMessagingException e) {
            return CustomResponse.generateResponse(false);
        }
    }
    @GetMapping()
    public String test(){
        return "Rea";
    }

    @GetMapping("test")
    public String tes1t(){
        return "Rea";
    }
}
