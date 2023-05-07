package com.hust.edu.vn.documentsystem.event.listener;

import com.hust.edu.vn.documentsystem.entity.User;
import com.hust.edu.vn.documentsystem.event.RegistrationCompleteEvent;
import com.hust.edu.vn.documentsystem.repository.UserRepository;
import com.hust.edu.vn.documentsystem.service.EmailService;
import com.hust.edu.vn.documentsystem.service.GoogleCloudStorageService;
import com.hust.edu.vn.documentsystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {
    private final GoogleCloudStorageService googleCloudStorageService;
    private final UserRepository userRepository;
    private final UserService userService;

    private final EmailService emailService;


    @Autowired
    public RegistrationCompleteEventListener(
            GoogleCloudStorageService googleCloudStorageService,
            UserRepository userRepository, UserService userService,
            EmailService emailService
    ) {
        this.googleCloudStorageService = googleCloudStorageService;
        this.userRepository = userRepository;
        this.userService = userService;
        this.emailService = emailService;
    }

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();


        boolean isCreatedRootPath = googleCloudStorageService.createFolderForUser(token);
        if (!isCreatedRootPath) {
            log.error("Failed to create root path for user: {}", user.getEmail());
            return;
        }
        user.setRootPath(token);
        userRepository.save(user);


        userService.saveVerificationTokenForUser(user, token);
        String url = event.getApplicationUrl()
                + "/auth/verificationAccountToken?token="
                + token;
        emailService.sendSimpleMessage(user.getEmail(), "Kích hoạt tài khoản", "Link: " + url);
    }
}
