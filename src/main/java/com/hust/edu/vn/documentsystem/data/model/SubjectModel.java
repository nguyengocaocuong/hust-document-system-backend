package com.hust.edu.vn.documentsystem.data.model;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubjectModel implements Serializable {
    private Long id;

    @NotBlank
    private String name;

    private String description;

    @NotBlank
    private Long teacherId;

    private String subjectId;

}
