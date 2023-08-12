package com.hust.edu.vn.documentsystem.controller.user;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.dto.SubjectDto;
import com.hust.edu.vn.documentsystem.data.dto.UserDto;
import com.hust.edu.vn.documentsystem.service.EnrollmentService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users/enrollment")
public class UserEnrollmentController {
    private final ModelMapperUtils modelMapperUtils;
    private final EnrollmentService enrollmentService;

    public UserEnrollmentController(ModelMapperUtils modelMapperUtils, EnrollmentService enrollmentService) {
        this.modelMapperUtils = modelMapperUtils;
        this.enrollmentService = enrollmentService;
    }

    record EnrollmentRequest (List<Long> subjects){
        public List<Long> getSubjects() {
            return subjects;
        }
    }
    @PostMapping()
    public ResponseEntity<CustomResponse> addEnrollmentSubject(@ModelAttribute EnrollmentRequest enrollment){
        boolean status = enrollmentService.addEnrollmentSubject(enrollment.getSubjects());
        return CustomResponse.generateResponse(status);
    }
    @GetMapping()
    public ResponseEntity<CustomResponse> getAllEnrollmentSubjects(@RequestParam(name = "userId", required = false) Long userId){
        return CustomResponse.generateResponse(HttpStatus.OK, enrollmentService.getAllEnrollmentSubjects(userId).stream().map(subject ->{
            subject.setSubjectDocuments(null);
            subject.setFavorites(null);
            subject.setSubjectDocuments(null);
            SubjectDto subjectDto = modelMapperUtils.mapAllProperties(subject, SubjectDto.class);
            subject.getEnrollments().forEach(enrollment -> subjectDto.getEnrollment().add(modelMapperUtils.mapAllProperties(enrollment.getUser(), UserDto.class)));
            return subjectDto;
        }));
    }


    @DeleteMapping
    public ResponseEntity<CustomResponse> deleteEnrollmentSubjects(@ModelAttribute EnrollmentRequest enrollment){
        return CustomResponse.generateResponse(enrollmentService.deleteEnrollmentSubjects(enrollment.getSubjects()));
    }
}
