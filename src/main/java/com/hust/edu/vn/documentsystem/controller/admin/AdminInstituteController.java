package com.hust.edu.vn.documentsystem.controller.admin;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.dto.InstituteDto;
import com.hust.edu.vn.documentsystem.data.model.InstituteModel;
import com.hust.edu.vn.documentsystem.entity.Institute;
import com.hust.edu.vn.documentsystem.service.InstituteService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admins/institute")
public class AdminInstituteController {
    private final InstituteService instituteService;
    private final ModelMapperUtils modelMapperUtils;

    public AdminInstituteController(InstituteService instituteService, ModelMapperUtils modelMapperUtils) {
        this.instituteService = instituteService;
        this.modelMapperUtils = modelMapperUtils;
    }

    @PostMapping()
    public ResponseEntity<CustomResponse> createInstitute(@ModelAttribute InstituteModel instituteModel) {
        Institute institute = instituteService.createInstitute(instituteModel);
        if (institute == null)
            return CustomResponse.generateResponse(HttpStatus.CONFLICT);
        return CustomResponse.generateResponse(HttpStatus.CREATED, modelMapperUtils.mapAllProperties(institute, InstituteDto.class));
    }
}
