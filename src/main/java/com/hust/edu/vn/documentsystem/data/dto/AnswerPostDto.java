package com.hust.edu.vn.documentsystem.data.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * A DTO for the {@link com.hust.edu.vn.documentsystem.entity.AnswerPost} entity
 */
@Data
@Setter
@Getter
public class AnswerPostDto implements Serializable {
    private  Long id;
    private  String content;
    private  String type;
    private  DocumentDto document;
    private  String name;
    private  Date createdAt;
    private  PostDto post;
    private  UserDto owner;
}