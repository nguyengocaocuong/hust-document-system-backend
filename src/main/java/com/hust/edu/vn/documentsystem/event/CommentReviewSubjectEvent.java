package com.hust.edu.vn.documentsystem.event;

import com.hust.edu.vn.documentsystem.entity.CommentReviewSubject;
import com.hust.edu.vn.documentsystem.entity.CommentReviewTeacher;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class CommentReviewSubjectEvent extends ApplicationEvent {
    private CommentReviewSubject commentReviewSubject;

    public CommentReviewSubjectEvent(CommentReviewSubject commentReviewSubject) {
        super(commentReviewSubject);
        this.commentReviewSubject = commentReviewSubject;
    }
}
