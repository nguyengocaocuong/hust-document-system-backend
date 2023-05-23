package com.hust.edu.vn.documentsystem.data.model;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class DocumentModel {

    private Long id;

    private MultipartFile[] documents;

    private String[] path;
}
