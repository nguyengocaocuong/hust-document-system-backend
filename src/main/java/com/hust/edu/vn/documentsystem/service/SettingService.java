package com.hust.edu.vn.documentsystem.service;

import com.hust.edu.vn.documentsystem.entity.Setting;

public interface SettingService {
    Setting setNumberOfReportForNotifications(int numberOfReport);

    void resetDefaultSettings();
}
