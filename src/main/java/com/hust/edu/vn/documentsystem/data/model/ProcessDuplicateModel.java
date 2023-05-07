package com.hust.edu.vn.documentsystem.data.model;

import com.hust.edu.vn.documentsystem.common.type.ReportStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProcessDuplicateModel {
    @NotNull
    private Long id;
    @NotNull
    private String message;
    @NotNull
    private Long removeDocumentId;
    @NotNull
    private ReportStatus reportStatus;
}
