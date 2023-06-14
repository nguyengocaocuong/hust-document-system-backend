package com.hust.edu.vn.documentsystem.data.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * A DTO for the {@link com.hust.edu.vn.documentsystem.entity.FavoriteAnswerSubjectDocument} entity
 */
@Data
@Getter
@Setter
public class FavoriteAnswerSubjectDocumentDto implements Serializable {
    private  Long id;
    private  UserDto user;
    private  Date createAt;
}