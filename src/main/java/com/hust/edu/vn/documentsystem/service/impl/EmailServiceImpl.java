package com.hust.edu.vn.documentsystem.service.impl;

import com.hust.edu.vn.documentsystem.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender emailMailSender;

    @Autowired
    public EmailServiceImpl(JavaMailSender emailMailSender) {
        this.emailMailSender = emailMailSender;
    }

    @Override
    public boolean sendSimpleMessage(String to, String subject, String text){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("cuong.nnc184055@sis.hust.edu.vn");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailMailSender.send(message);
        return true;
    }
}
