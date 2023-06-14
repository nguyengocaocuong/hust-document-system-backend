package com.hust.edu.vn.documentsystem.data.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * A DTO for the {@link com.hust.edu.vn.documentsystem.entity.CommentPost} entity
 */
@Data
@Getter
@Setter
public class CommentPostDto implements Serializable {
    private  Long id;
    private  String comment;
    private  UserDto owner;
    private  Date createdAt;
    private List<CommentPostDto> childComment;
}