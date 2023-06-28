package com.hust.edu.vn.documentsystem.data.model;


import com.hust.edu.vn.documentsystem.common.type.ReportStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
public class ProcessReportContentModel {
    @NotNull
    private Long id;
    @NotNull
    private String message;
    @NotNull
    private ReportStatus reportStatus;
}
