package com.hust.edu.vn.documentsystem.data.dto;

import com.hust.edu.vn.documentsystem.entity.FavoritePostDto;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A DTO for the {@link com.hust.edu.vn.documentsystem.entity.Post} entity
 */
@Data
@Getter
@Setter
public class PostDto implements Serializable {
    private Long id;
    private UserDto owner;
    private String description;
    private DocumentDto document;
    private Date createdAt;
    private SubjectForPostDto subject;
    private boolean isHidden;
    private List<CommentPostDto> comments = new ArrayList<>();
    private List<AnswerPostDto> answers = new ArrayList<>();
    private List<FavoritePostDto> favorites = new ArrayList<>();
}