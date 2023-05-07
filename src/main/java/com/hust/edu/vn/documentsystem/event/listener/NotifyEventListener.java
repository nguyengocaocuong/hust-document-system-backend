package com.hust.edu.vn.documentsystem.event.listener;

import com.hust.edu.vn.documentsystem.event.NotifyEvent;
import com.hust.edu.vn.documentsystem.service.FirebaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotifyEventListener implements ApplicationListener<NotifyEvent> {
    private final FirebaseService firebaseService;

    @Autowired
    public NotifyEventListener(FirebaseService firebaseService) {
        this.firebaseService = firebaseService;
    }
    @Override
    public void onApplicationEvent(NotifyEvent event) {
        switch (event.getNotificationType()){
            case NEW_DOCUMENT -> firebaseService.notifyForEventNewDocument(event.getData());
            case NEW_POST -> firebaseService.notifyForEventNewPost(event.getData());
            case DELETE_POST -> firebaseService.notifyForEventDeletePost(event.getData());
            case EDIT_POST -> firebaseService.notifyForEventEditPost(event.getData());
            case DELETE_DOCUMENT -> firebaseService.notifyForEventDeleteDocument(event.getData());
            case EDIT_DOCUMENT -> firebaseService.notifyForEventEditDocument(event.getData());
            case NEW_ANSWER -> firebaseService.notifyForEventNewAnswer(event.getData());
            case DELETE_ANSWER -> firebaseService.notifyForEventDeleteAnswer(event.getData());
            case EDIT_ANSWER -> firebaseService.notifyForEventEditAnswer(event.getData());
            case NEW_REPORT -> firebaseService.notifyForEventNewReport(event.getData());
            case DELETE_REPORT -> firebaseService.notifyForEventDeleteReport(event.getData());
            case EDIT_REPORT -> firebaseService.notifyForEventEditReport(event.getData());
            case NEW_REVIEW_TEACHER -> firebaseService.notifyForEventNewReviewTeacher(event.getData());
            case DELETE_REVIEW_TEACHER -> firebaseService.notifyForEventDeleteReviewTeacher(event.getData());
            case EDIT_REVIEW_TEACHER -> firebaseService.notifyForEventEditReviewTeacher(event.getData());
            case NEW_REVIEW_SUBJECT -> firebaseService.notifyForEventNewReviewSubject(event.getData());
            case DELETE_REVIEW_SUBJECT -> firebaseService.notifyForEventDeleteReviewSubject(event.getData());
            case EDIT_REVIEW_SUBJECT -> firebaseService.notifyForEventEditReviewSubject(event.getData());
            case NEW_USER -> firebaseService.notifyForEventNewUser(event.getData());
            case DELETE_USER -> firebaseService.notifyForEventDeleteUser(event.getData());
            case EDIT_USER -> firebaseService.notifyForEventEditUser(event.getData());
            case NEW_COMMENT_POST -> firebaseService.notifyForEventNewCommentPort(event.getData());
            case DELETE_COMMENT_POST -> firebaseService.notifyForEventDeleteCommentPort(event.getData());
            case EDIT_COMMENT_POST -> firebaseService.notifyForEventEditCommentPort(event.getData());
            case NEW_COMMENT_SUBJECT_DOCUMENT -> firebaseService.notifyForEventNewCommentSubjectDocument(event.getData());
            case DELETE_COMMENT_SUBJECT_DOCUMENT -> firebaseService.notifyForEventDeleteCommentSubjectDocument(event.getData());
            case EDIT_COMMENT_SUBJECT_DOCUMENT -> firebaseService.notifyForEventEditCommentSubjectDocument(event.getData());
            case NEW_COMMENT_REVIEW_TEACHER -> firebaseService.notifyForEventNewCommentReviewTeacher(event.getData());
            case DELETE_COMMENT_REVIEW_TEACHER -> firebaseService.notifyForEventDeleteCommentReviewTeacher(event.getData());
            case EDIT_COMMENT_REVIEW_TEACHER -> firebaseService.notifyForEventEditCommentReviewTeacher(event.getData());
            case NEW_COMMENT_REVIEW_SUBJECT -> firebaseService.notifyForEventNewCommentReviewSubject(event.getData());
            case DELETE_COMMENT_REVIEW_SUBJECT -> firebaseService.notifyForEventDeleteCommentReviewSubject(event.getData());
            case EDIT_COMMENT_REVIEW_SUBJECT -> firebaseService.notifyForEventEditCommentReviewSubject(event.getData());
            case TRANSLATED -> firebaseService.notifyForEventTranslated(event.getData());
            case HIDDEN_COMMENT_PORT -> firebaseService.notifyForEventHiddenCommentPort(event.getData());
            case ACTIVE_COMMENT_PORT -> firebaseService.notifyForEventActiveCommentPort(event.getData());
            case HIDDEN_ANSWER_PORT -> firebaseService.notifyForEventHiddenAnswerPort(event.getData());
            case ACTIVE_ANSWER_PORT -> firebaseService.notifyForEventActiveAnswerPort(event.getData());
            case HIDDEN_PORT -> firebaseService.notifyForEventHiddenPort(event.getData());
            case ACTIVE_PORT -> firebaseService.notifyForEventActivePort(event.getData());
            default -> log.info("Unknown event");
        }
    }
}
