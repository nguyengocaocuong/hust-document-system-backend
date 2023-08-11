package com.hust.edu.vn.documentsystem.data.model;

import lombok.*;

import java.io.Serializable;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubjectModel implements Serializable {
    private Long id;
    private String name;
    private Long instituteId;
    private String description;
    private String subjectCode;
    private String enName;
}
