package com.hust.edu.vn.documentsystem.data.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReportDuplicateModel {
    private Long id;
    private String content;
    private Long documentFirstId;
    private Long documentSecondId;
}
