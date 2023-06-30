package com.hust.edu.vn.documentsystem.controller.notifications;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.service.NotificationTokenService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {
    private final NotificationTokenService notificationTokenService;

    
    public NotificationController(NotificationTokenService notificationTokenService) {
        this.notificationTokenService = notificationTokenService;
    }

    @GetMapping()
    public ResponseEntity<CustomResponse> registerForNotifications(@RequestParam("token") String token ){
        notificationTokenService.registerForNotifications(token);
        return CustomResponse.generateResponse(true);
    }
    @DeleteMapping()
    public ResponseEntity<CustomResponse> unregisterForNotifications(@RequestParam("token") String token ){
        return CustomResponse.generateResponse(notificationTokenService.unRegisterForNotifications(token));
    }
}
