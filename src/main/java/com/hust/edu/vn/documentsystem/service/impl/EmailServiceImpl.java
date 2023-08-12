package com.hust.edu.vn.documentsystem.service.impl;

import com.hust.edu.vn.documentsystem.entity.User;
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
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;

@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender emailMailSender;
    private final TemplateEngine templateEngine;

    public EmailServiceImpl(JavaMailSender emailMailSender, TemplateEngine templateEngine) {
        this.emailMailSender = emailMailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public boolean sendSimpleMessage(User user, String subject, String text) {
        MimeMessage message = emailMailSender.createMimeMessage();
        MimeMessageHelper helper;
        String username = user.getFirstName() + " " + user.getLastName();
        String activeUrl = text;
        String frontendUrl = System.getenv("FRONTEND_URL");
        Context context = new Context();
        context.setVariable("username", username);
        context.setVariable("activeUrl", activeUrl);
        context.setVariable("frontendUrl", frontendUrl);
        String emailContent = templateEngine.process("active-account-template",context);

        try {
            helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom("cuong.nnc184055@sis.hust.edu.vn");
            helper.setTo(user.getEmail());
            helper.setSubject(subject);
            helper.setText(emailContent, true);
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
            helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom("cuong.nnc184055@sis.hust.edu.vn");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
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
