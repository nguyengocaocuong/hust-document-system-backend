package com.hust.edu.vn.documentsystem.controller.admin;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.entity.Setting;
import com.hust.edu.vn.documentsystem.service.SettingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v2/settings")
@Tag(name="Settings - api")
public class AdminSettingController {
    private final SettingService settingService;

    public AdminSettingController(SettingService settingService) {
        this.settingService = settingService;
    }

    @GetMapping("numberOfReportForNotifications/{numberOfReport}")
    public ResponseEntity<CustomResponse> settingNumberOfReportForNotifications(@PathVariable("numberOfReport") int numberOfReport){
        Setting setting = settingService.setNumberOfReportForNotifications(numberOfReport);
        return CustomResponse.generateResponse(setting != null ? HttpStatus.OK: HttpStatus.BAD_REQUEST, setting);
    }
    @GetMapping("resetDefaultSettings")
    public ResponseEntity<CustomResponse> resetDefaultSettings(){
        settingService.resetDefaultSettings();
        return CustomResponse.generateResponse(true);
    }
}
