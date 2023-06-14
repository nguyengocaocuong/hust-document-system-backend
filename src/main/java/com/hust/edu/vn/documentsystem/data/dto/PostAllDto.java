package com.hust.edu.vn.documentsystem.data.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * A DTO for the {@link com.hust.edu.vn.documentsystem.entity.Post} entity
 */
@Data
@Getter
@Setter
public class PostAllDto implements Serializable {
    private  Long id;
    private  UserDto owner;
    private  String content;
    private  DocumentDto document;
    private String description;
    private  SubjectForPostDto subject;
    private  List<FavoritePostDto> favoritePostList;
    private  List<AnswerPostDto> answerPostList;
    private  List<CommentPostDto> commentPosts;
    private Date createdAt;

    /**
     * A DTO for the {@link com.hust.edu.vn.documentsystem.entity.FavoritePost} entity
     */
    @Data
    @Getter
    @Setter
    public static class FavoritePostDto implements Serializable {
        private  Long id;
        private  UserDto user;
        private  Date createAt;
    }
}