package com.hust.edu.vn.documentsystem.data.model;


import com.hust.edu.vn.documentsystem.common.type.ApproveType;
import jakarta.validation.constraints.NotBlank;
import lombok.*;



@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewTeacherModel {
    private Long id;

    @NotBlank
    private String review;

    private Long done;

    private ApproveType approved = ApproveType.NEW;
}
