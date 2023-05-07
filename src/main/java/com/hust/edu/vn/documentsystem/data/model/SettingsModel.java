package com.hust.edu.vn.documentsystem.data.model;

import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SettingsModel {
    private int numberOfReportForNotifications = 3;
}
