package com.hust.edu.vn.documentsystem.event;

import com.hust.edu.vn.documentsystem.entity.CommentReviewTeacher;
import com.hust.edu.vn.documentsystem.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class CommentReviewTeacherEvent extends ApplicationEvent {
    private CommentReviewTeacher commentReviewTeacher;

    public CommentReviewTeacherEvent(CommentReviewTeacher commentReviewTeacher) {
        super(commentReviewTeacher);
        this.commentReviewTeacher = commentReviewTeacher;
    }
}
