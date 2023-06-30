package com.hust.edu.vn.documentsystem.event.listener;

import com.hust.edu.vn.documentsystem.entity.User;
import com.hust.edu.vn.documentsystem.event.RegistrationCompleteEvent;
import com.hust.edu.vn.documentsystem.service.EmailService;
import com.hust.edu.vn.documentsystem.service.UserService;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {
    private final UserService userService;

    private final EmailService emailService;


    
    public RegistrationCompleteEventListener(
            UserService userService,
            EmailService emailService
    ) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        User user = event.getUser();
        String tokenForEnableAccount = UUID.randomUUID().toString();
        userService.saveVerificationTokenForUser(user, tokenForEnableAccount);
        String url = event.getApplicationUrl()
                + "/api/v1/authentication/verificationAccountToken?token="
                + tokenForEnableAccount;
        emailService.sendSimpleMessage(user.getEmail(), "Kích hoạt tài khoản", "Link: " + url);
    }
}
