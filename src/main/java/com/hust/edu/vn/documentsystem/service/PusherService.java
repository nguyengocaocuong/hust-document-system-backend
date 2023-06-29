package com.hust.edu.vn.documentsystem.service;

public interface PusherService {
    void triggerChanel(String channel, String event, Object data);
}
