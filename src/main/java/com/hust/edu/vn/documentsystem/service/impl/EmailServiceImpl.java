package com.hust.edu.vn.documentsystem.service.impl;

import com.hust.edu.vn.documentsystem.service.EmailService;
import jakarta.activation.DataHandler;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender emailMailSender;

    
    public EmailServiceImpl(JavaMailSender emailMailSender) {
        this.emailMailSender = emailMailSender;
    }

    @Override
    public boolean sendSimpleMessage(String to, String subject, String text){
        MimeMessage message = emailMailSender.createMimeMessage();
        MimeMessageHelper helper;
        StringBuilder htmlContent = new StringBuilder();
        htmlContent.append("<h3>Chào mừng bạn đến với <strong>Hust Document System</strong></h3>");
        htmlContent.append("<h6>Click vào");
        htmlContent.append("<a href='" + text +"'>đây</a> để kích hoạt tài khoản của bạn</h6>");
        try {
            helper = new MimeMessageHelper(message,true, "UTF-8");
            helper.setFrom("cuong.nnc184055@sis.hust.edu.vn");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent.toString(),true);
            emailMailSender.send(message);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean sendHtmlMessage(String to, String subject, String htmlContent) {
        MimeMessage message = emailMailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(message,true, "UTF-8");
            helper.setFrom("cuong.nnc184055@sis.hust.edu.vn");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent,true);
            emailMailSender.send(message);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean sendEmailWithAttachment(String to, String subject, String text, MultipartFile attachmentFilePath) {
        MimeMessage message = emailMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("cuong.nnc184055@sis.hust.edu.vn");
            helper.setTo(to);
            helper.setSubject(subject);

            MimeMultipart multipart = new MimeMultipart();
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(text);
            multipart.addBodyPart(textPart);
            MimeBodyPart attachmentPart = new MimeBodyPart();
            ByteArrayDataSource attachmentDataSource = new ByteArrayDataSource(attachmentFilePath.getBytes(), attachmentFilePath.getContentType());
            attachmentPart.setDataHandler(new DataHandler(attachmentDataSource));
            attachmentPart.setFileName(attachmentFilePath.getOriginalFilename());
            multipart.addBodyPart(attachmentPart);

            message.setContent(multipart);

            emailMailSender.send(message);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
