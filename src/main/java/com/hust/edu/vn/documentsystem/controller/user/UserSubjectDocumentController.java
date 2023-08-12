package com.hust.edu.vn.documentsystem.controller.user;

import com.google.cloud.storage.Blob;
import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.common.type.TargetLanguageType;
import com.hust.edu.vn.documentsystem.data.dto.SubjectDocumentDto;
import com.hust.edu.vn.documentsystem.entity.Document;
import com.hust.edu.vn.documentsystem.entity.SubjectDocument;
import com.hust.edu.vn.documentsystem.service.PusherService;
import com.hust.edu.vn.documentsystem.service.SubjectDocumentService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("/api/v1/users/subjects/subjectDocument/{subjectDocumentId}")
@Slf4j
public class UserSubjectDocumentController {
    private final SubjectDocumentService subjectDocumentService;
    private final ModelMapperUtils modelMapperUtils;

    private final PusherService pusherService;

    public UserSubjectDocumentController(SubjectDocumentService subjectDocumentService, ModelMapperUtils modelMapperUtils, PusherService pusherService) {
        this.subjectDocumentService = subjectDocumentService;
        this.modelMapperUtils = modelMapperUtils;
        this.pusherService = pusherService;
    }

    @GetMapping("readFile")
    public ResponseEntity<Resource> readSubjectDocumentFile(@PathVariable("subjectDocumentId") Long subjectDocumentId,
                                                            @RequestParam(required = false) String token) throws UnsupportedEncodingException {
        List<Object> data = subjectDocumentService.readSubjectDocumentFile(subjectDocumentId, token);
        log.info("test");
        if (data == null || ((byte[]) data.get(1)).length == 0)
            return ResponseEntity.notFound().build();
        Document document = (Document) data.get(0);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(document.getContentType()));
        headers.setContentDisposition(ContentDisposition.attachment().filename(URLEncoder.encode(document.getName(), StandardCharsets.UTF_8.toString())).build());
        return ResponseEntity.ok()
                .headers(headers)
                .body(new ByteArrayResource((byte[]) data.get(1)));
    }

    @GetMapping()
    public ResponseEntity<CustomResponse> getSubjectDocumentDetail(
            @PathVariable("subjectDocumentId") Long subjectDocumentId) {
        SubjectDocument subjectDocument = subjectDocumentService.getSubjectDocumentDetailById(subjectDocumentId);
        subjectDocument.getSubject().setSubjectDocuments(null);
        return CustomResponse.generateResponse(subjectDocument != null ? HttpStatus.OK : HttpStatus.NOT_FOUND,
                subjectDocument != null ? modelMapperUtils.mapAllProperties(subjectDocument, SubjectDocumentDto.class)
                        : null);
    }

    @GetMapping("generatePublicOnInternetUrl")
    public ResponseEntity<CustomResponse> generatePublicOnInternetUrlForSubjectDocument(
            @PathVariable("subjectDocumentId") Long subjectDocumentId) {
        String url = subjectDocumentService.generatePublicOnInternetUrlForSubjectDocument(subjectDocumentId);
        if (url == null)
            return CustomResponse.generateResponse(HttpStatus.UNAUTHORIZED);
        return CustomResponse.generateResponse(HttpStatus.OK, url);
    }

    @GetMapping("generatePublicOnWebsiteUrl")
    public ResponseEntity<CustomResponse> generatePublicOnWebsiteUrlForSubjectDocument(
            @PathVariable("subjectDocumentId") Long subjectDocumentId) {
        String url = subjectDocumentService.generatePublicOnWebsiteUrlForSubjectDocument(subjectDocumentId);
        if (url == null)
            return CustomResponse.generateResponse(HttpStatus.UNAUTHORIZED);
        return CustomResponse.generateResponse(HttpStatus.OK, url);
    }

    @DeleteMapping("forever")
    public ResponseEntity<CustomResponse> deleteSubjectDocumentForever(
            @PathVariable("subjectDocumentId") Long subjectDocumentId) {
        boolean status = subjectDocumentService.deleteSubjectDocumentForever(subjectDocumentId);
        return CustomResponse.generateResponse(status);
    }

    @DeleteMapping()
    public ResponseEntity<CustomResponse> moveSubjectDocumentToTrash(
            @PathVariable("subjectDocumentId") Long subjectDocumentId) {
        boolean status = subjectDocumentService.moveSubjectDocumentToTrash(subjectDocumentId);
        return CustomResponse.generateResponse(status);
    }

    @PatchMapping("restore")
    public ResponseEntity<CustomResponse> restoreSubjectDocument(
            @PathVariable("subjectDocumentId") Long subjectDocumentId) {
        boolean status = subjectDocumentService.restoreSubjectDocument(subjectDocumentId);
        return CustomResponse.generateResponse(status);
    }

    @PatchMapping("private")
    public ResponseEntity<CustomResponse> makeSubjectDocumentPrivate(
            @PathVariable("subjectDocumentId") Long subjectDocumentId) {
        boolean status = subjectDocumentService.makeSubjectDocumentPrivate(subjectDocumentId);
        return CustomResponse.generateResponse(status);
    }

    @GetMapping("translate")
    public ResponseEntity<Resource> translateDocument(@PathVariable("subjectDocumentId") Long subjectDocumentId,
                                                      @RequestParam("targetLanguage") TargetLanguageType targetLanguageType) {
        List<Object> data = subjectDocumentService.translateSubjectDocument(subjectDocumentId, targetLanguageType);
        if (data == null || ((byte[]) data.get(1)).length == 0)
            return ResponseEntity.notFound().build();
        Document document = (Document) data.get(0);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(document.getContentType()));
        headers.setContentDisposition(ContentDisposition.attachment().filename(document.getName()).build());
        return ResponseEntity.ok()
                .headers(headers)
                .body(new ByteArrayResource((byte[]) data.get(1)));
    }

    @PostMapping("translate-file")
    public ResponseEntity<Resource> translateDocument(@ModelAttribute MultipartFile file,
                                                      @RequestParam("targetLanguage") TargetLanguageType targetLanguageType) {
        try {
            byte[] data = subjectDocumentService.translateSubjectDocumentByFile(file, targetLanguageType);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.valueOf(file.getContentType()));
            headers.setContentDisposition(ContentDisposition.attachment().filename(file.getOriginalFilename()).build());
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new ByteArrayResource(data));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PatchMapping("public")
    public ResponseEntity<CustomResponse> makeSubjectDocumentPublic(
            @PathVariable("subjectDocumentId") Long subjectDocumentId) {
        boolean status = subjectDocumentService.makeSubjectDocumentPublic(subjectDocumentId);
        return CustomResponse.generateResponse(status);
    }

    @PostMapping("annotation")
    public ResponseEntity<CustomResponse> saveAndSendAnnotation(@PathVariable("subjectDocumentId") Long subjectDocumentId, @RequestBody String xfdfString, @RequestParam String action) {
        // TODO: save annotation
        log.info(xfdfString);
        pusherService.triggerChanel("annotation-" + subjectDocumentId, action, xfdfString);
        return CustomResponse.generateResponse(true);
    }

    @GetMapping("download")
    public ResponseEntity<Resource> getDownloadUrlForSubjectDocument(@PathVariable("subjectDocumentId") Long subjectDocumentId){
        Blob blob = subjectDocumentService.getSubjectDocumentBlob(subjectDocumentId);
        if(blob == null) return ResponseEntity.badRequest().body(new ByteArrayResource(new byte[0]));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(blob.getContentType()));
        headers.setContentDisposition(ContentDisposition.attachment().filename(blob.getName()).build());
        headers.setContentLength(blob.getSize());
        return ResponseEntity.ok()
                .headers(headers)
                .body(new ByteArrayResource(blob.getContent()));
    }

    @GetMapping("download-multiple")
    public ResponseEntity<Resource> downloadMultipleFiles(@PathVariable("subjectDocumentId") Long subjectDocumentId, @RequestParam("answerIds") List<Long> answerIds) {
        List<Blob> blobs = subjectDocumentService.getSubjectDocumentBlobAndAnswers(subjectDocumentId, answerIds);
        if (blobs.isEmpty()) {
            return ResponseEntity.badRequest().body(new ByteArrayResource(new byte[0]));
        }

        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);
            for (Blob blob : blobs) {
                ZipEntry zipEntry = new ZipEntry(blob.getName());
                zipOutputStream.putNextEntry(zipEntry);
                zipOutputStream.write(blob.getContent());
                zipOutputStream.closeEntry();
            }
            zipOutputStream.close();
            byteArrayOutputStream.close();
            byte[] zipData = byteArrayOutputStream.toByteArray();
            ByteArrayResource resource = new ByteArrayResource(zipData);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDisposition(ContentDisposition.attachment().filename("files.zip").build());
            headers.setContentLength(zipData.length);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
