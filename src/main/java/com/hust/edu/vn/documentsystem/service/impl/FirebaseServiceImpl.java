package com.hust.edu.vn.documentsystem.service.impl;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.gson.Gson;
import com.hust.edu.vn.documentsystem.common.type.NotificationType;
import com.hust.edu.vn.documentsystem.entity.NotificationToken;
import com.hust.edu.vn.documentsystem.entity.User;
import com.hust.edu.vn.documentsystem.repository.NotificationTokenRepository;
import com.hust.edu.vn.documentsystem.repository.UserRepository;
import com.hust.edu.vn.documentsystem.service.FirebaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class FirebaseServiceImpl implements FirebaseService {
    private final UserRepository userRepository;
    private final NotificationTokenRepository notificationTokenRepository;
    private final FirebaseApp firebaseApp;

    @Autowired
    public FirebaseServiceImpl(FirebaseApp firebaseApp,
                               NotificationTokenRepository notificationTokenRepository,
                               UserRepository userRepository) {
        this.firebaseApp = firebaseApp;
        this.notificationTokenRepository = notificationTokenRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void sendMessage(String token, String titleNotification, String dataNotification, Map<String, String> data) throws FirebaseMessagingException {
        FirebaseMessaging messaging = FirebaseMessaging.getInstance(firebaseApp);
        Message message = Message.builder()
                .setNotification(
                        Notification.builder()
                                .setTitle(titleNotification)
                                .setBody(dataNotification)
                                .build()
                )
                .putAllData(data)
                .setToken(token)
                .build();
        messaging.send(message);
    }

    @Override
    public void notifyForEventEditPost(Object data) {
        Gson gson = new Gson();
        List<NotificationToken> notificationTokenList = notificationTokenRepository.findAll();
        for (NotificationToken notificationToken : notificationTokenList) {
            try {
                this.sendMessage(notificationToken.getToken(),"Chỉnh sưa bài viết","Bài viết vừa được chỉnh sửa, bạn có muốn xem không", Map.of("type", NotificationType.EDIT_POST.name(), "post", gson.toJson(data)));
            } catch (FirebaseMessagingException e) {
                log.error(e.getMessage(), e);
            }
        }
    }


    @Override
    public void notifyForEventNewUser(Object data) {
        Gson gson = new Gson();
        User user = userRepository.findByRoleType("ADMIN");
        NotificationToken notificationToken = notificationTokenRepository.findByUser(user);
        if(notificationToken == null) return;
        try {
            this.sendMessage(
                    notificationToken.getToken(),
                    "Có thêm người dùng mới",
                    "Hệ thống vừa ghi nhân thêm một nguời dùng mới",
                    Map.of("type", NotificationType.NEW_USER.name(), "data", gson.toJson(data))
            );
        } catch (FirebaseMessagingException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void notifyForEventNewReport(Object data) {
        log.info("notifyForEventNewReport");
    }

    @Override
    public void notifyForEventEditReport(Object data) {
        log.info("notifyForEventEditReport");
    }

    @Override
    public void notifyForEventEditDocument(Object data) {
       log.info("notifyForEventEditDocument");
    }


    @Override
    public void notifyForEventDeletePost(Object data) {
        log.info("notifyForEventDeletePost");
    }

    @Override
    public void notifyForEventEditUser(Object data) {
        log.info("notifyForEventEditUser");
    }

    @Override
    public void notifyForEventNewCommentPort(Object data) {
        log.info("notifyForEventNewCommentPort");
    }

    @Override
    public void notifyForEventDeleteCommentPort(Object data) {
        log.info("notifyForEventDeleteCommentPort");
    }

    @Override
    public void notifyForEventEditCommentPort(Object data) {
        log.info("notifyForEventEditCommentPort");
    }

    @Override
    public void notifyForEventNewCommentSubjectDocument(Object data) {
        log.info("notifyForEventNewCommentSubjectDocument");
    }

    @Override
    public void notifyForEventDeleteCommentSubjectDocument(Object data) {
        log.info("notifyForEventDeleteCommentSubjectDocument");
    }

    @Override
    public void notifyForEventEditCommentSubjectDocument(Object data) {
        log.info("notifyForEventEditCommentSubjectDocument");
    }

    @Override
    public void notifyForEventNewCommentReviewTeacher(Object data) {
        log.info("notifyForEventNewCommentReviewTeacher");
    }

    @Override
    public void notifyForEventDeleteCommentReviewTeacher(Object data) {
        log.info("notifyForEventDeleteCommentReviewTeacher");
    }

    @Override
    public void notifyForEventEditCommentReviewTeacher(Object data) {
        log.info("notifyForEventEditCommentReviewTeacher");
    }

    @Override
    public void notifyForEventNewCommentReviewSubject(Object data) {
        log.info("notifyForEventNewCommentReviewSubject");
    }

    @Override
    public void notifyForEventDeleteCommentReviewSubject(Object data) {
        log.info("notifyForEventDeleteCommentReviewSubject");
    }

    @Override
    public void notifyForEventEditCommentReviewSubject(Object data) {
        log.info("notifyForEventEditCommentReviewSubject");
    }

    @Override
    public void notifyForEventTranslated(Object data) {
        log.info("notifyForEventTranslated");
    }

    @Override
    public void notifyForEventHiddenCommentPort(Object data) {
        log.info("notifyForEventHiddenCommentPort");
    }

    @Override
    public void notifyForEventActiveCommentPort(Object data) {
        log.info("notifyForEventActiveCommentPort");
    }

    @Override
    public void notifyForEventHiddenAnswerPort(Object data) {
        log.info("notifyForEventHiddenAnswerPort");
    }

    @Override
    public void notifyForEventActiveAnswerPort(Object data) {
        log.info("notifyForEventActiveAnswerPort");
    }

    @Override
    public void notifyForEventHiddenPort(Object data) {
        log.info("notifyForEventHiddenPort");
    }

    @Override
    public void notifyForEventActivePort(Object data) {
        log.info("notifyForEventActivePort");
    }

    @Override
    public void notifyForEventDeleteReport(Object data) {
        log.info("notifyForEventDeleteReport");
    }

    @Override
    public void notifyForEventDeleteDocument(Object data) {
        log.info("notifyForEventDeleteDocument");
    }

    @Override
    public void notifyForEventDeleteUser(Object data) {
        log.info("notifyForEventDeleteUser");
    }

    @Override
    public void notifyForEventNewDocument(Object data) {
        log.info("notifyForEventNewDocument");
    }

    @Override
    public void notifyForEventNewPost(Object data) {
        log.info("notifyForEventNewPost");
    }


    @Override
    public void notifyForEventNewAnswer(Object data) {
        log.info("notifyForEventNewAnswer");
    }

    @Override
    public void notifyForEventDeleteAnswer(Object data) {
        log.info("notifyForEventDeleteAnswer");
    }

    @Override
    public void notifyForEventEditAnswer(Object data) {
        log.info("notifyForEventEditAnswer");
    }

    @Override
    public void notifyForEventNewReviewTeacher(Object data) {
        log.info("notifyForEventNewReviewTeacher");
    }

    @Override
    public void notifyForEventDeleteReviewTeacher(Object data) {
        log.info("notifyForEventDeleteReviewTeacher");
    }

    @Override
    public void notifyForEventEditReviewTeacher(Object data) {
        log.info("notifyForEventEditReviewTeacher");
    }

    @Override
    public void notifyForEventNewReviewSubject(Object data) {
        log.info("notifyForEventNewReviewSubject");
    }

    @Override
    public void notifyForEventDeleteReviewSubject(Object data) {
        log.info("notifyForEventDeleteReviewSubject");
    }

    @Override
    public void notifyForEventEditReviewSubject(Object data) {
        log.info("notifyForEventEditReviewSubject");
    }
}
