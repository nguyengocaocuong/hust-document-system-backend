package com.hust.edu.vn.documentsystem.controller.user;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.dto.HistoryDto;
import com.hust.edu.vn.documentsystem.service.HistoryService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users/history")
public class UserHistoryController {
    private final HistoryService historyService;
    private final ModelMapperUtils modelMapperUtils;

    public UserHistoryController(HistoryService historyService, ModelMapperUtils modelMapperUtils) {
        this.historyService = historyService;
        this.modelMapperUtils = modelMapperUtils;
    }

    @GetMapping
    public ResponseEntity<CustomResponse> getHistory(){
        return CustomResponse.generateResponse(HttpStatus.OK, historyService.getAllHistory().stream().map(history -> modelMapperUtils.mapAllProperties(history, HistoryDto.class)));
    }
}
