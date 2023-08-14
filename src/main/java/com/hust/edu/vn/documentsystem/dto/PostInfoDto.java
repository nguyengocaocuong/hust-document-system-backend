package com.hust.edu.vn.documentsystem.dto;

import com.hust.edu.vn.documentsystem.data.dto.FavoritePostDto;
import com.hust.edu.vn.documentsystem.entity.Post;
import com.hust.edu.vn.documentsystem.entity.User;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.io.Serializable;
import java.util.Date;

/**
 * A DTO for the {@link com.hust.edu.vn.documentsystem.entity.Post} entity
 */
@Data
public class PostInfoDto implements Serializable {
    private Long id;
    private UserDto owner;
    private String description;
    private DocumentDto document;
    private Date createdAt;
    private SubjectDto subject;
    private Long totalComments;
    private Long totalAnswers;
    private Long totalFavorites;
    private boolean isFavorite;

    public PostInfoDto(Post p,Long totalAnswers, Long totalComments, Long totalFavorites, boolean isFavorite, String subjectName,String documentPath){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setSkipNullEnabled(true)
                .setDeepCopyEnabled(true);

        this.totalAnswers = totalAnswers;
        this.totalComments = totalComments;
        this.totalFavorites = totalFavorites;
        this.isFavorite = isFavorite;
        this.id = p.getId();
        this.description = p.getDescription();
        this.owner = modelMapper.map(p.getOwner(), UserDto.class);
        this.document = new DocumentDto(documentPath);
        this.createdAt = p.getCreatedAt();
        this.subject = new SubjectDto(subjectName);
    }
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
        private String path;
        public DocumentDto(String path){
            this.path = path;
        }
    }

    /**
     * A DTO for the {@link com.hust.edu.vn.documentsystem.entity.Subject} entity
     */
    @Data
    private static class SubjectDto implements  Serializable{
        private Long id;
        private String name;
        public SubjectDto(String name){
            this.name = name;
        }
    }


}