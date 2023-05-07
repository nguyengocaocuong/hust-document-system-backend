package com.hust.edu.vn.documentsystem.service.impl;

import com.hust.edu.vn.documentsystem.entity.NotificationToken;
import com.hust.edu.vn.documentsystem.entity.User;
import com.hust.edu.vn.documentsystem.repository.NotificationTokenRepository;
import com.hust.edu.vn.documentsystem.repository.UserRepository;
import com.hust.edu.vn.documentsystem.service.NotificationTokenService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class NotificationTokenServiceImpl implements NotificationTokenService {
    private final UserRepository userRepository;
    private final NotificationTokenRepository notificationTokenRepository;

    public NotificationTokenServiceImpl(UserRepository userRepository,
                                        NotificationTokenRepository notificationTokenRepository) {
        this.userRepository = userRepository;
        this.notificationTokenRepository = notificationTokenRepository;
    }

    @Override
    public void registerForNotifications(String token) {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        NotificationToken notificationToken = new NotificationToken();
        notificationToken.setToken(token);
        notificationToken.setUser(user);
        notificationTokenRepository.save(notificationToken);
    }

    @Override
    public boolean unRegisterForNotifications(String token) {
        NotificationToken notificationToken = notificationTokenRepository.findByToken(token);
        if(notificationToken == null || notificationToken.getUser().getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName()))
            return false;
        notificationTokenRepository.delete(notificationToken);
        return true;
    }
}
