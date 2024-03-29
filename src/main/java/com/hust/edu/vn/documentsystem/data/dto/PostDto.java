package com.hust.edu.vn.documentsystem.data.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A DTO for the {@link com.hust.edu.vn.documentsystem.entity.Post} entity
 */
@Data
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



    /**
     * A DTO for the {@link com.hust.edu.vn.documentsystem.entity.User} entity
     */
    @Data
    public static class UserDto implements Serializable{
        private Long id;
        private String firstName;
        private String lastName;
        private String avatar;
    }


    /**
     * A DTO for the {@link com.hust.edu.vn.documentsystem.entity.Document} entity
     */
    @Data
    public static class DocumentDto implements Serializable{
        private Long id;
        private String path;
    }

    /**
     * A DTO for the {@link com.hust.edu.vn.documentsystem.entity.Subject} entity
     */
    private static class SubjectDto implements  Serializable{
        private Long id;
        private String name;
    }

}