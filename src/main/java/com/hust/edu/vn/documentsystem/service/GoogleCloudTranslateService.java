package com.hust.edu.vn.documentsystem.service;

import com.hust.edu.vn.documentsystem.common.type.TargetLanguageType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface GoogleCloudTranslateService {
    List<String> translateText(String yourText, TargetLanguageType targetLanguageType);
    List<String> translateTextFromImage(MultipartFile image, TargetLanguageType targetLanguageType);
    String translateDocument(MultipartFile document, TargetLanguageType targetLanguageType) throws IOException;
}
