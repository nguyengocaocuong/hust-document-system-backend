package com.hust.edu.vn.documentsystem.service.impl;

import com.google.cloud.spring.vision.CloudVisionTemplate;
import com.google.cloud.storage.Acl;
import com.google.cloud.translate.v3.*;
import com.google.protobuf.ByteString;
import com.hust.edu.vn.documentsystem.common.type.NotificationType;
import com.hust.edu.vn.documentsystem.common.type.TargetLanguageType;
import com.hust.edu.vn.documentsystem.event.NotifyEvent;
import com.hust.edu.vn.documentsystem.service.GoogleCloudStorageService;
import com.hust.edu.vn.documentsystem.service.GoogleCloudTranslateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class GoogleCloudTranslateServiceImpl implements GoogleCloudTranslateService {
    private final TranslationServiceClient translationServiceClient;
    private final CloudVisionTemplate cloudVisionTemplate;
    private final GoogleCloudStorageService cloudStorageService;
    private final ApplicationEventPublisher publisher;

    @Autowired
    public GoogleCloudTranslateServiceImpl(
            TranslationServiceClient translationServiceClient,
            CloudVisionTemplate cloudVisionTemplate,
            GoogleCloudStorageService cloudStorageService, ApplicationEventPublisher publisher) {
        this.translationServiceClient = translationServiceClient;
        this.cloudVisionTemplate = cloudVisionTemplate;
        this.cloudStorageService = cloudStorageService;
        this.publisher = publisher;
    }


    @Override
    public List<String> translateText(String yourText, TargetLanguageType targetLanguageType) {
        LocationName parent = LocationName.of(System.getenv("PROJECT_ID"), "global");
        TranslateTextRequest request = TranslateTextRequest.newBuilder()
                .setParent(parent.toString())
                .setMimeType("text/plain")
                .setTargetLanguageCode(targetLanguageType.getCode())
                .addContents(yourText)
                .build();
        TranslateTextResponse response = translationServiceClient.translateText(request);
        return response.getTranslationsList().stream().map(Translation::getTranslatedText).toList();
    }

    @Override
    public List<String> translateTextFromImage(MultipartFile image, TargetLanguageType targetLanguageType) {
        String textFromImage = cloudVisionTemplate.extractTextFromImage(image.getResource());
        if (textFromImage == null || textFromImage.length() == 0)
            return new ArrayList<>();
        return translateText(textFromImage, targetLanguageType);
    }

    @Override
    public String translateDocument(MultipartFile document, TargetLanguageType targetLanguageType) throws IOException {
        String filename = Objects.requireNonNull(document.getOriginalFilename());
        String folder = UUID.randomUUID().toString();
        String path = "translate/" + folder + "/translated_document_" + targetLanguageType.getCode() + "_translations" + filename.substring(filename.lastIndexOf("."));
        String output = "gs://hust-document-file/translate/" + folder + "/";
        LocationName parent = LocationName.of(System.getenv("PROJECT_ID"), "global");
        DocumentOutputConfig documentOutputConfig = DocumentOutputConfig.newBuilder()
                .setGcsDestination(
                        GcsDestination.newBuilder()
                                .setOutputUriPrefix(output)
                                .build()
                )
                .setMimeType(Objects.requireNonNull(document.getContentType()))
                .build();
        DocumentInputConfig documentInputConfig =  DocumentInputConfig
                .newBuilder()
                .setMimeType(document.getContentType())
                .setContent(ByteString.copyFrom(document.getBytes()))
                .build();
        TranslateDocumentRequest request = TranslateDocumentRequest
                .newBuilder()
                .setParent(parent.toString())
                .setDocumentInputConfig(documentInputConfig)
                .setDocumentOutputConfig(documentOutputConfig)
                .setTargetLanguageCode(targetLanguageType.getCode())
                .build();
        translationServiceClient.translateDocument(request);
        cloudStorageService.setAclForAccessBlob(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER),path);
        publisher.publishEvent(new NotifyEvent(NotificationType.TRANSLATED, path));
        return path;
    }
}

