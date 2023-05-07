package com.hust.edu.vn.documentsystem.service.impl;

import com.hust.edu.vn.documentsystem.entity.Setting;
import com.hust.edu.vn.documentsystem.repository.SettingRepository;
import com.hust.edu.vn.documentsystem.service.SettingService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SettingServiceImpl implements SettingService {
    private final SettingRepository settingRepository;

    public SettingServiceImpl(SettingRepository settingRepository) {
        this.settingRepository = settingRepository;
    }

    @Override
    public Setting setNumberOfReportForNotifications(int numberOfReport) {
        List<Setting> settingList = settingRepository.findAll();
        Setting setting = null;
        if (settingList.size() == 0)
            setting = new Setting();
        else
            setting = settingList.get(0);
        setting.setNumberOfReportForNotifications(numberOfReport);
        return settingRepository.save(setting);
    }

    @Override
    public void resetDefaultSettings() {
        List<Setting> settingList = settingRepository.findAll();
        Setting setting = null;
        if (settingList.size() == 0)
            setting = new Setting();
        else
            setting = settingList.get(0);
        setting.setNumberOfReportForNotifications(3);

        settingRepository.save(setting);
    }
}
