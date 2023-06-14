package com.hust.edu.vn.documentsystem.controller.admin;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.entity.Post;
import com.hust.edu.vn.documentsystem.entity.ReviewTeacher;
import com.hust.edu.vn.documentsystem.entity.User;
import com.hust.edu.vn.documentsystem.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/v1/admins")
public class AdminController {
    private final UserService userService;
    private final PostService postService;
    private final SubjectDocumentService subjectDocumentService;
    private final ReviewTeacherService reviewTeacherService;
    private final ReviewSubjectService reviewSubjectService;

    @Autowired
    public AdminController(UserService userService, PostService postService, SubjectDocumentService subjectDocumentService, ReviewTeacherService reviewTeacherService, ReviewSubjectService reviewSubjectService) {
        this.userService = userService;
        this.postService = postService;
        this.subjectDocumentService = subjectDocumentService;
        this.reviewTeacherService = reviewTeacherService;
        this.reviewSubjectService = reviewSubjectService;
    }

    @GetMapping("dashboard")
    public ResponseEntity<CustomResponse> getDashboardOnWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        Date sevenDaysAgo = calendar.getTime();
        DateFormat formatter = new SimpleDateFormat("dd/MM");
        List<Object[]> users = userService.getUserForDashboard(sevenDaysAgo);
        Map<String, Object> userMap = new HashMap<>();
        users.forEach( user -> {
            userMap.put(formatter.format((Date)user[0]), user[1]);
        } );
        List<Object[]> posts = postService.getPostForDashboard(sevenDaysAgo);
        Map<String, Object> postMap = new HashMap<>();
        posts.forEach( user -> {
            postMap.put(formatter.format((Date)user[0]), user[1]);
        } );
        List<Object[]> reviewSubjects = reviewSubjectService.getReviewForDashboard(sevenDaysAgo);
        Map<String, Long> reviewSubjectMap = new HashMap<>();
        reviewSubjects.forEach( user -> {
            reviewSubjectMap.put(formatter.format((Date)user[0]), (Long) user[1]);
        } );
        List<Object[]> reviewTeachers = reviewTeacherService.getReviewForDashboard(sevenDaysAgo);
        reviewTeachers.forEach( user -> {
            if(reviewSubjectMap.containsKey(formatter.format((Date)user[0])))
                reviewSubjectMap.replace(formatter.format((Date)user[0]), (Long)user[1] + reviewSubjectMap.get(formatter.format((Date)user[0])));
            else
                reviewSubjectMap.put(formatter.format((Date)user[0]), (Long) user[1]);
        } );
        return CustomResponse.generateResponse(HttpStatus.OK, List.of(userMap, postMap, reviewSubjectMap));
    }
}
