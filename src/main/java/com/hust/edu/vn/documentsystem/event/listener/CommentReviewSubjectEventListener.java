package com.hust.edu.vn.documentsystem.event.listener;

import com.hust.edu.vn.documentsystem.data.dto.CommentReviewSubjectDto;
import com.hust.edu.vn.documentsystem.entity.CommentReviewSubject;
import com.hust.edu.vn.documentsystem.event.CommentReviewSubjectEvent;
import com.hust.edu.vn.documentsystem.repository.CommentReviewSubjectRepository;
import com.hust.edu.vn.documentsystem.service.GoogleLanguageService;
import com.hust.edu.vn.documentsystem.service.PusherService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class CommentReviewSubjectEventListener implements ApplicationListener<CommentReviewSubjectEvent> {
    private final GoogleLanguageService googleLanguageService;

    private final CommentReviewSubjectRepository commentReviewSubjectRepository;

    private final PusherService pusherService;
    private final ModelMapperUtils modelMapperUtils;

    public CommentReviewSubjectEventListener(GoogleLanguageService googleLanguageService, PusherService pusherService,
                                             CommentReviewSubjectRepository commentReviewSubjectRepository, ModelMapperUtils modelMapperUtils) {
        this.googleLanguageService = googleLanguageService;
        this.pusherService = pusherService;
        this.commentReviewSubjectRepository = commentReviewSubjectRepository;
        this.modelMapperUtils = modelMapperUtils;
    }

    @Override
    public void onApplicationEvent(CommentReviewSubjectEvent event) {
        CommentReviewSubject commentReviewSubject = event.getCommentReviewSubject();
        try {
            float score = googleLanguageService.detectBabComment(commentReviewSubject.getComment());
            if (score < 0) {
                pusherService.triggerChanel("bad-comment", "new-bad-comment",
                        modelMapperUtils.mapAllProperties(commentReviewSubject, CommentReviewSubjectDto.class));
            }
            commentReviewSubject.setScore(score);
            commentReviewSubjectRepository.save(commentReviewSubject);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
