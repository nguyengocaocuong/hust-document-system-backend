package com.hust.edu.vn.documentsystem.data.model;

import com.hust.edu.vn.documentsystem.common.type.TargetLanguageType;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TranslateTextModel {
    @NotBlank
    private String text;
    @NotBlank
    private TargetLanguageType targetLanguage;
}
