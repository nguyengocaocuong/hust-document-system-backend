package com.hust.edu.vn.documentsystem.controller.user;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.dto.InstituteDto;
import com.hust.edu.vn.documentsystem.service.InstituteService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/v1/users/institute")
public class UserInstituteController {
    private final InstituteService instituteService;
    private final ModelMapperUtils modelMapperUtils;

    public UserInstituteController(InstituteService instituteService, ModelMapperUtils modelMapperUtils) {
        this.instituteService = instituteService;
        this.modelMapperUtils = modelMapperUtils;
    }

    @GetMapping
    public ResponseEntity<CustomResponse> getAllInstitutes(){
        return CustomResponse.generateResponse(HttpStatus.OK, instituteService.getAllInstitutes().stream().map(institute -> modelMapperUtils.mapAllProperties(institute, InstituteDto.class)));
    }
}
