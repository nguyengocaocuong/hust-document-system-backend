package com.hust.edu.vn.documentsystem.data.model;

import com.hust.edu.vn.documentsystem.common.type.DocumentType;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class DocumentModel {

    private Long id;

    private String title;

    private String description;

    private DocumentType type;

    private MultipartFile[] documents;

    private String[] path;
}
