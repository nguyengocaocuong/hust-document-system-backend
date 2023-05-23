package com.hust.edu.vn.documentsystem.data.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

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
    private  PostDto post;
    private  Date createdAt;
}