package com.hust.edu.vn.documentsystem.data.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

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
    private  Long id;
    private  String name;
    private  String email;
    private  String phoneNumber;
    private  Date createdAt;
    private  String avatar;
    private  Date dob;
    private  List<SubjectDto> subjects;
    private  String description;
    private  UserDto owner;

    public TeacherDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}