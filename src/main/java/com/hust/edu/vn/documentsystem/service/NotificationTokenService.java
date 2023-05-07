package com.hust.edu.vn.documentsystem.service;

public interface NotificationTokenService {
    void registerForNotifications(String token);

    boolean unRegisterForNotifications(String token);
}
