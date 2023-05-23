package com.hust.edu.vn.documentsystem.data.model;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewSubjectModel {
    private Long id;

    private String review;

    private Long done;

    private Long subjectId;
}
