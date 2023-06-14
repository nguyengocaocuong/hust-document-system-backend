package com.hust.edu.vn.documentsystem.data.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * A DTO for the {@link com.hust.edu.vn.documentsystem.entity.Teacher} entity
 */
@Data
@Getter
@Setter
public class TeacherDto implements Serializable {
    public Long id;
    public String name;
    public String emailHust;
    public String emailPrivate;
    private String description;
    public String phoneNumber;
    public String avatar;
    public Date dob;
    public List<SubjectDto> subjects;

    public TeacherDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public TeacherDto() {
    }
}