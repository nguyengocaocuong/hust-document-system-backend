package com.hust.edu.vn.documentsystem.dto;


import com.hust.edu.vn.documentsystem.data.dto.SubjectDocumentTypeDto;
import com.hust.edu.vn.documentsystem.entity.Institute;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * A DTO for the {@link com.hust.edu.vn.documentsystem.entity.SubjectDocument} entity
 */
@Data
public class SearchResultDto {
    private Long id;
    private SubjectDocumentTypeDto subjectDocumentType;
    private SubjectDocumentInfoDto.DocumentDto document;
    private String description;
    private SubjectDocumentInfoDto.UserDto owner;
    private Date createdAt;
    private SubjectDto subject;
    private Long totalDownload;
    private Long totalView;
    private Long totalFavorite;
    private Long totalAnswer;
    private Long totalComment;

    /**
     * A DTO for the {@link com.hust.edu.vn.documentsystem.entity.User} entity
     */
    @Data
    public static class UserDto implements Serializable {
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
        private InstituteDto institute;

        /**
         * A DTO for the {@link com.hust.edu.vn.documentsystem.entity.Institute} entity
         */
        private static class InstituteDto implements  Serializable{
            private String institute;
        }
    }
}
