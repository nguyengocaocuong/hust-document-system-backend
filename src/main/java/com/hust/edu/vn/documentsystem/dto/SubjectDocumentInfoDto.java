package com.hust.edu.vn.documentsystem.dto;

import com.hust.edu.vn.documentsystem.data.dto.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * A DTO for the {@link com.hust.edu.vn.documentsystem.entity.SubjectDocument} entity
 */
@Data
public class SubjectDocumentInfoDto implements Serializable {
    private Long id;
    private SubjectDocumentTypeDto subjectDocumentType;
    private DocumentDto document;
    private String description;
    private UserDto owner;
    private Date createdAt;
    private Date deletedAt;
    private SubjectDto subject;
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
        private String thumbnail;
    }

    /**
     * A DTO for the {@link com.hust.edu.vn.documentsystem.entity.Subject} entity
     */
    @Data
    private static class SubjectDto implements  Serializable{
        private Long id;
        private String name;
    }

}
