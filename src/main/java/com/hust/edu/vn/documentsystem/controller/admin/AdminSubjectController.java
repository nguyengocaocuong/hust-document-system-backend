package com.hust.edu.vn.documentsystem.controller.admin;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.dto.SubjectDto;
import com.hust.edu.vn.documentsystem.data.model.SubjectModel;
import com.hust.edu.vn.documentsystem.entity.Subject;
import com.hust.edu.vn.documentsystem.service.SubjectService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import com.spire.ms.System.Collections.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admins/subjects")
public class AdminSubjectController {
    private final SubjectService subjectService;
    private final ModelMapperUtils modelMapperUtils;

    @Autowired
    public AdminSubjectController(SubjectService subjectService, ModelMapperUtils modelMapperUtils) {
        this.modelMapperUtils = modelMapperUtils;
        this.subjectService = subjectService;
    }

    @GetMapping()
    public ResponseEntity<CustomResponse> getAllSubject() {
        List<Object[]> results = subjectService.getAllSubjectForAdmin();
        List<Map<String, Object>> resultMap = new ArrayList();
        results.forEach(result ->{
            Map<String, Object> map = new HashMap<>();
            Subject subject = (Subject)result[0];
            subject.setSubjectDocuments(null);
            map.put("subject", modelMapperUtils.mapAllProperties(subject, SubjectDto.class));
            map.put("subjectDocumentTotal", result[1]);
            map.put("reviewSubjectTotal", result[2]);
            map.put("favoriteSubjectTotal", result[3]);
            resultMap.add(map);
        });
        return CustomResponse.generateResponse(HttpStatus.OK, resultMap);
    }
    @PatchMapping()
    public ResponseEntity<CustomResponse> updateSubject(@ModelAttribute SubjectModel subjectModel){
        boolean status = subjectService.updateSubject(subjectModel);
        return  CustomResponse.generateResponse(HttpStatus.OK, status);
    }
    @DeleteMapping("{subjectId}")
    public ResponseEntity<CustomResponse> deleteSubject(@PathVariable("subjectId") Long subjectId){
        boolean status = subjectService.deleteSubject(subjectId);
        return  CustomResponse.generateResponse(HttpStatus.OK, status);
    }

}
