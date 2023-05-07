package com.hust.edu.vn.documentsystem.service;
import com.google.firebase.messaging.FirebaseMessagingException;

import java.util.Map;
public interface FirebaseService {
    void sendMessage(String token, String titleNotification, String dataNotification, Map<String, String> data) throws FirebaseMessagingException;

    void notifyForEventEditPost(Object data);

    void notifyForEventNewUser(Object data);

    void notifyForEventNewReport(Object data);

    void notifyForEventEditReport(Object data);

    void notifyForEventEditDocument(Object data);

    void notifyForEventDeleteReport(Object data);

    void notifyForEventDeleteDocument(Object data);

    void notifyForEventDeleteUser(Object data);

    void notifyForEventNewDocument(Object data);

    void notifyForEventNewPost(Object data);

    void notifyForEventNewAnswer(Object data);

    void notifyForEventDeleteAnswer(Object data);

    void notifyForEventEditAnswer(Object data);

    void notifyForEventNewReviewTeacher(Object data);

    void notifyForEventDeleteReviewTeacher(Object data);

    void notifyForEventEditReviewTeacher(Object data);

    void notifyForEventNewReviewSubject(Object data);

    void notifyForEventDeleteReviewSubject(Object data);

    void notifyForEventEditReviewSubject(Object data);

    void notifyForEventDeletePost(Object data);

    void notifyForEventEditUser(Object data);

    void notifyForEventNewCommentPort(Object data);

    void notifyForEventDeleteCommentPort(Object data);

    void notifyForEventEditCommentPort(Object data);

    void notifyForEventNewCommentSubjectDocument(Object data);

    void notifyForEventDeleteCommentSubjectDocument(Object data);

    void notifyForEventEditCommentSubjectDocument(Object data);

    void notifyForEventNewCommentReviewTeacher(Object data);

    void notifyForEventDeleteCommentReviewTeacher(Object data);

    void notifyForEventEditCommentReviewTeacher(Object data);

    void notifyForEventNewCommentReviewSubject(Object data);

    void notifyForEventDeleteCommentReviewSubject(Object data);

    void notifyForEventEditCommentReviewSubject(Object data);

    void notifyForEventTranslated(Object data);

    void notifyForEventHiddenCommentPort(Object data);

    void notifyForEventActiveCommentPort(Object data);

    void notifyForEventHiddenAnswerPort(Object data);

    void notifyForEventActiveAnswerPort(Object data);

    void notifyForEventHiddenPort(Object data);

    void notifyForEventActivePort(Object data);
}
