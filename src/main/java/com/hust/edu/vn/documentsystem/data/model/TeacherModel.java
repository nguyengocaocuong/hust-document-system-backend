package com.hust.edu.vn.documentsystem.data.model;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TeacherModel {
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String emailHust;
    private String emailPrivate;
    private String phoneNumber;
    private String description;
    private String avatar;
    private MultipartFile avatarFile;
}
