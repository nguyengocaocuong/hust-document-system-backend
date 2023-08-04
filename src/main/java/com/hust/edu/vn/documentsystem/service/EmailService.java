package com.hust.edu.vn.documentsystem.service;

import com.hust.edu.vn.documentsystem.entity.User;
import org.springframework.web.multipart.MultipartFile;

public interface EmailService {
    boolean sendSimpleMessage(User user, String subject, String text);
    boolean sendHtmlMessage(String to, String subject, String htmlContent);
    boolean sendEmailWithAttachment(String to, String subject, String text, MultipartFile attachmentFilePath);
}
