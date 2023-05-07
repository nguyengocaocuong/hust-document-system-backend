package com.hust.edu.vn.documentsystem.controller;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.dto.ReviewTeacherDto;
import com.hust.edu.vn.documentsystem.data.dto.SubjectDto;
import com.hust.edu.vn.documentsystem.data.dto.TeacherDto;
import com.hust.edu.vn.documentsystem.entity.ReviewTeacher;
import com.hust.edu.vn.documentsystem.entity.Subject;
import com.hust.edu.vn.documentsystem.entity.Teacher;
import com.hust.edu.vn.documentsystem.service.ReviewTeacherService;
import com.hust.edu.vn.documentsystem.service.SubjectService;
import com.hust.edu.vn.documentsystem.service.TeacherService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/public/search")
@Tag(name = "Search - api")
public class PublicSearchController {
    private final TeacherService teacherService;
    private final ReviewTeacherService reviewTeacherRepository;

    private final SubjectService subjectService;

    private final ModelMapperUtils modelMapperUtils;



    @Autowired
    public PublicSearchController(
            TeacherService teacherService,
            ReviewTeacherService reviewTeacherRepository,
            SubjectService subjectService,
            ModelMapperUtils modelMapperUtils
           ) {
        this.teacherService = teacherService;
        this.reviewTeacherRepository = reviewTeacherRepository;
        this.subjectService = subjectService;
        this.modelMapperUtils = modelMapperUtils;
    }

    @GetMapping("teachers")
    public ResponseEntity<CustomResponse> searchTeacherByKeyword(@RequestParam(name = "key") String keyword) {
        List<Teacher> teachers = teacherService.findTeacherByKeyword(keyword);
        PropertyMap<Teacher, TeacherDto> propertyMap = new PropertyMap<>() {
            @Override
            protected void configure() {
                skip(destination.getOwner());
                skip(destination.getSubjects());
            }
        };
        return CustomResponse.generateResponse(
                HttpStatus.OK,
                "Dưới đây là danh sách giáo viên",
                teachers.stream().map(
                        teacher -> modelMapperUtils.mapSelectedProperties(teacher, TeacherDto.class, propertyMap)
                )
        );
    }

    @GetMapping("reviewsTeacher")
    public ResponseEntity<CustomResponse> searchReviewsTeacher(@RequestParam("teacherId") Long teacherId, @RequestParam("subject_id") Long subjectId) {
        List<ReviewTeacher> reviewTeachers = reviewTeacherRepository.findReviewByTeacherAndSubject(teacherId, subjectId);
        return CustomResponse.generateResponse(
                HttpStatus.OK,
                "Dưới đây là danh sách reviewTeacher",
                reviewTeachers.stream().map(reviewTeacher -> modelMapperUtils.mapAllProperties(reviewTeacher, ReviewTeacherDto.class)).toList()
        );
    }

    @GetMapping("subjects")
    public ResponseEntity<CustomResponse> searchSubjectByKeyword(@RequestParam(name = "key") String keyword, @RequestParam(value = "teacherId", required = false) Long teacherId) {
        Teacher teacher = null;
        if (teacherId != null)
            teacher = teacherService.getTeacherById(teacherId);
        List<Subject> subjects = subjectService.findSubjectByKeywordAndTeachers(keyword, teacher);
        return ResponseEntity.ok(
                CustomResponse.builder()
                        .responseCode(HttpStatus.OK.value())
                        .description(HttpStatus.OK.getReasonPhrase())
                        .content(subjects.stream().map(subject -> modelMapperUtils.mapAllProperties(subject, SubjectDto.class)).toList())
                        .build()
        );
    }

}
