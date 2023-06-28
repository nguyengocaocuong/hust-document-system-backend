package com.hust.edu.vn.documentsystem.data.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;


@Data
public class ReportContentModel {

    private Long id;

    @NotNull
    private String content;

    private String message;

    @NotNull
    private Long subjectDocumentId;
}
