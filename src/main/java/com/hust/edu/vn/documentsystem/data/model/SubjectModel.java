package com.hust.edu.vn.documentsystem.data.model;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SubjectModel implements Serializable {
    private Long id;

    @NotBlank
    private String name;

    private String description;

    private List<Long> teacherId;

    private String subjectCode;

}
