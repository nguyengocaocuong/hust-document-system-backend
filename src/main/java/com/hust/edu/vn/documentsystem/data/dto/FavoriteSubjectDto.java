package com.hust.edu.vn.documentsystem.data.dto;

import com.hust.edu.vn.documentsystem.common.type.NotificationType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * A DTO for the {@link com.hust.edu.vn.documentsystem.entity.FavoriteSubject} entity
 */
@Data
public class FavoriteSubjectDto implements Serializable {
    private  Long id;
    private  SubjectDto subject;
    private  UserDto user;
    private  Date createAt;
    private  NotificationType notificationType;
}