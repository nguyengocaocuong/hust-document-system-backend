package com.hust.edu.vn.documentsystem.event.listener;

import com.hust.edu.vn.documentsystem.data.dto.CommentReviewTeacherDto;
import com.hust.edu.vn.documentsystem.entity.CommentReviewTeacher;
import com.hust.edu.vn.documentsystem.event.CommentReviewTeacherEvent;
import com.hust.edu.vn.documentsystem.repository.CommentReviewTeacherRepository;
import com.hust.edu.vn.documentsystem.service.GoogleLanguageService;
import com.hust.edu.vn.documentsystem.service.PusherService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class CommentReviewTeacherEventListener implements ApplicationListener<CommentReviewTeacherEvent> {
    private final CommentReviewTeacherRepository commentReviewTeacherRepository;
    private final GoogleLanguageService googleLanguageService;

    private final PusherService pusherService;

    private final ModelMapperUtils modelMapperUtils;

    public CommentReviewTeacherEventListener(CommentReviewTeacherRepository commentReviewTeacherRepository, GoogleLanguageService googleLanguageService, PusherService pusherService, ModelMapperUtils modelMapperUtils) {
        this.commentReviewTeacherRepository = commentReviewTeacherRepository;
        this.googleLanguageService = googleLanguageService;
        this.pusherService = pusherService;
        this.modelMapperUtils = modelMapperUtils;
    }

    @Override
    public void onApplicationEvent(CommentReviewTeacherEvent event) {
        CommentReviewTeacher commentReviewTeacher = event.getCommentReviewTeacher();
        try {
            float score = googleLanguageService.detectBabComment(commentReviewTeacher.getComment());
            if (score < 0) {
                pusherService.triggerChanel("bad-comment", "new-bad-comment",
                        modelMapperUtils.mapAllProperties(commentReviewTeacher, CommentReviewTeacherDto.class));
            }
            commentReviewTeacher.setScore(score);
            commentReviewTeacherRepository.save(commentReviewTeacher);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
