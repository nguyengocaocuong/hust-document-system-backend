package com.hust.edu.vn.documentsystem.data.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * A DTO for the {@link com.hust.edu.vn.documentsystem.entity.Setting} entity
 */
@Data
@Getter
@Setter
public class SettingDto implements Serializable {
    private  Long id;
    private  int numberOfReportForNotifications;
}