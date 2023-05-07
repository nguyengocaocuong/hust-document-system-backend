package com.hust.edu.vn.documentsystem.service;

public interface EmailService {
    public boolean sendSimpleMessage(String to, String subject, String text);
}
