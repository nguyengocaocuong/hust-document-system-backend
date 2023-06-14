package com.hust.edu.vn.documentsystem.data.dto;

import com.hust.edu.vn.documentsystem.common.type.DocumentType;
import com.hust.edu.vn.documentsystem.entity.FavoriteAnswerPost;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * A DTO for the {@link com.hust.edu.vn.documentsystem.entity.AnswerPost} entity
 */
@Data
@Setter
@Getter
public class AnswerPostDto implements Serializable {
    private  Long id;
    private  String description;
    private  DocumentDto document;
    private DocumentType type;
    private  Date createdAt;
    private  UserDto owner;
    private List<FavoriteAnswerPostDto> favorites;
}