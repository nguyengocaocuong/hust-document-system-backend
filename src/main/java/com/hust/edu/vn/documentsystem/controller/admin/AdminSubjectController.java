package com.hust.edu.vn.documentsystem.controller.admin;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.dto.SubjectDto;
import com.hust.edu.vn.documentsystem.data.model.SubjectModel;
import com.hust.edu.vn.documentsystem.entity.Document;
import com.hust.edu.vn.documentsystem.entity.Subject;
import com.hust.edu.vn.documentsystem.service.SubjectDocumentService;
import com.hust.edu.vn.documentsystem.service.SubjectService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import com.spire.ms.System.Collections.ArrayList;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admins/subjects")
public class AdminSubjectController {
    private final SubjectService subjectService;
    private final ModelMapperUtils modelMapperUtils;

    private final SubjectDocumentService subjectDocumentService;
    
    public AdminSubjectController(SubjectService subjectService, ModelMapperUtils modelMapperUtils, SubjectDocumentService subjectDocumentService) {
        this.modelMapperUtils = modelMapperUtils;
        this.subjectService = subjectService;
        this.subjectDocumentService = subjectDocumentService;
    }

    @GetMapping()
    public ResponseEntity<CustomResponse> getAllSubject() {
        List<Object[]> results = subjectService.getAllSubjectForAdmin();
        @SuppressWarnings("unchecked")
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

    @GetMapping("subjectDocuments/{subjectDocumentId}/readFile")
    public ResponseEntity<Resource> readSubjectDocument(@PathVariable("subjectDocumentId") Long id) {
        List<Object> data = subjectDocumentService.readSubjectDocumentFile(id, null);
        if (data == null || ((byte[]) data.get(1)).length == 0) return ResponseEntity.notFound().build();
        Document document = (Document) data.get(0);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(document.getContentType()));
        headers.setContentDisposition(ContentDisposition.attachment().filename(document.getName()).build());
        return ResponseEntity.ok()
                .headers(headers)
                .body(new ByteArrayResource((byte[]) data.get(1)));
    }
}
