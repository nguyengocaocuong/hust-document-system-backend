package com.hust.edu.vn.documentsystem.data.model;

import com.hust.edu.vn.documentsystem.common.type.TargetLanguageType;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TranslateDocumentModel {
    @NotNull
    private MultipartFile document;

    @NotNull
    private TargetLanguageType targetLanguage;
}
