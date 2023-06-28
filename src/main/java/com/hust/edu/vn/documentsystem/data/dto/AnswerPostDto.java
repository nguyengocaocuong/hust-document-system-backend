package com.hust.edu.vn.documentsystem.data.dto;

import com.hust.edu.vn.documentsystem.common.type.DocumentType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * A DTO for the {@link com.hust.edu.vn.documentsystem.entity.AnswerPost} entity
 */
@Data
public class AnswerPostDto implements Serializable {
    private  Long id;
    private  String description;
    private  DocumentDto document;
    private DocumentType type;
    private  Date createdAt;
    private  UserDto owner;
    private List<FavoriteAnswerPostDto> favorites;
}