package com.hust.edu.vn.documentsystem.service.impl;

import com.google.cloud.spring.vision.CloudVisionTemplate;
import com.google.cloud.storage.Acl;
import com.google.cloud.translate.v3.*;
import com.google.protobuf.ByteString;
import com.hust.edu.vn.documentsystem.common.type.TargetLanguageType;
import com.hust.edu.vn.documentsystem.entity.Document;
import com.hust.edu.vn.documentsystem.service.GoogleCloudStorageService;
import com.hust.edu.vn.documentsystem.service.GoogleCloudTranslateService;

import org.springframework.http.MediaType;
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

    
    public GoogleCloudTranslateServiceImpl(
            TranslationServiceClient translationServiceClient,
            CloudVisionTemplate cloudVisionTemplate,
            GoogleCloudStorageService cloudStorageService) {
        this.translationServiceClient = translationServiceClient;
        this.cloudVisionTemplate = cloudVisionTemplate;
        this.cloudStorageService = cloudStorageService;
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
        return path;
    }

    @Override
    public byte[] translateSubjectDocument(Document document, TargetLanguageType targetLanguageType) {
        String output = getOutputTranslatePath(document.getPath());
        LocationName parent = LocationName.of(System.getenv("PROJECT_ID"), "global");
        byte[] inputs = cloudStorageService.readBlobByPath(document.getPath());
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
                .setContent(ByteString.copyFrom(inputs))
                .build();
        TranslateDocumentRequest request = TranslateDocumentRequest
                .newBuilder()
                .setParent(parent.toString())
                .setDocumentInputConfig(documentInputConfig)
                .setDocumentOutputConfig(documentOutputConfig)
                .setTargetLanguageCode(targetLanguageType.getCode())
                .build();
        TranslateDocumentResponse translateDocumentResponse = translationServiceClient.translateDocument(request);
        return  translateDocumentResponse.getDocumentTranslation().getByteStreamOutputs(0).toByteArray();
    }

    @Override
    public String translatePost(Document document, TargetLanguageType targetLanguageType) {
        String output = getOutputTranslatePath(document.getPath());
        LocationName parent = LocationName.of(System.getenv("PROJECT_ID"), "global");
        String paths[] = document.getPath().split("/");
        String filename = document.getName();
        String path =paths[paths.length - 2] +  "/translated_document_" + targetLanguageType.getCode() + "_translations" + filename.substring(filename.lastIndexOf("."));
        byte[] inputs = cloudStorageService.readBlobByPath(paths[paths.length - 2] + "/" + document.getName());
        DocumentOutputConfig documentOutputConfig = DocumentOutputConfig.newBuilder()
                .setGcsDestination(
                        GcsDestination.newBuilder()
                                .setOutputUriPrefix(output)
                                .build()
                )
                .setMimeType(Objects.requireNonNull(MediaType.APPLICATION_PDF.toString()))
                .build();
        DocumentInputConfig documentInputConfig =  DocumentInputConfig
                .newBuilder()
                .setMimeType(document.getContentType())
                .setContent(ByteString.copyFrom(inputs))
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
        return  cloudStorageService.generateUriFromPath(path);
    }

    @Override
    public byte[] translateSubjectDocumentByFile(MultipartFile file, TargetLanguageType targetLanguageType) throws IOException {
        String output = getOutputTranslatePath(null);
        LocationName parent = LocationName.of(System.getenv("PROJECT_ID"), "global");
        byte[] inputs = file.getBytes();
        DocumentOutputConfig documentOutputConfig = DocumentOutputConfig.newBuilder()
                .setGcsDestination(
                        GcsDestination.newBuilder()
                                .setOutputUriPrefix(output)
                                .build()
                )
                .setMimeType(Objects.requireNonNull(file.getContentType()))
                .build();
        DocumentInputConfig documentInputConfig =  DocumentInputConfig
                .newBuilder()
                .setMimeType(file.getContentType())
                .setContent(ByteString.copyFrom(inputs))
                .build();
        TranslateDocumentRequest request = TranslateDocumentRequest
                .newBuilder()
                .setParent(parent.toString())
                .setDocumentInputConfig(documentInputConfig)
                .setDocumentOutputConfig(documentOutputConfig)
                .setTargetLanguageCode(targetLanguageType.getCode())
                .build();
        TranslateDocumentResponse translateDocumentResponse = translationServiceClient.translateDocument(request);
        return  translateDocumentResponse.getDocumentTranslation().getByteStreamOutputs(0).toByteArray();
    }

    private String getOutputTranslatePath(String path){
        return "gs://" + System.getenv("BUCKET_NAME") + "/" + UUID.randomUUID()  + "/";
    }
}

