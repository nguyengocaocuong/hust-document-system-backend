package com.hust.edu.vn.documentsystem.data.model;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AnswerSubjectDocumentModel {
    private Long id;
    private String content;
    private String type;
    private MultipartFile[] documents;
}
