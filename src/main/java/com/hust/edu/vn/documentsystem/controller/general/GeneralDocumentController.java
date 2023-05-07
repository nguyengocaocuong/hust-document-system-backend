package com.hust.edu.vn.documentsystem.controller.general;

import com.google.cloud.storage.Blob;
import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.dto.BlobDto;
import com.hust.edu.vn.documentsystem.entity.Document;
import com.hust.edu.vn.documentsystem.entity.User;
import com.hust.edu.vn.documentsystem.service.DocumentService;
import com.hust.edu.vn.documentsystem.service.GoogleCloudStorageService;
import com.hust.edu.vn.documentsystem.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/api/v1/public/documents")
@Tag(name = "Documents - api")
public class GeneralDocumentController {
    private final DocumentService documentService;
    private final GoogleCloudStorageService googleCloudStorageService;

    private final UserService userService;
    @Autowired
    public GeneralDocumentController(DocumentService documentService, GoogleCloudStorageService googleCloudStorageService, UserService userService) {
        this.documentService = documentService;
        this.googleCloudStorageService = googleCloudStorageService;
        this.userService = userService;
    }

    @GetMapping("files/{documentId}")
    public ResponseEntity<CustomResponse> getAllFileByDocumentId(@PathVariable("documentId") Long documentId){
        Iterator<Blob> blobList = documentService.getAllFileByDocumentId(documentId).iterator();
        List<BlobDto> blobDtoList = new ArrayList<>();
        while(blobList.hasNext()){
            Blob blob = blobList.next();
            BlobDto blobDto = new BlobDto();
            blobDto.setName(blob.getName());
            blobDto.setContentType(blob.getContentType());
            blobDtoList.add(blobDto);
        }
        return CustomResponse.generateResponse(
                HttpStatus.OK,
                blobDtoList
        );
    }
    @GetMapping("sources/{documentId}")
    public ResponseEntity<ByteArrayResource> getSourceByDocumentId(@PathVariable("documentId") Long documentId){
        Document document  = documentService.findDocumentById(documentId);
        if(document == null){
            return ResponseEntity.notFound().build();
        }
        User user = userService.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

        String path = user.getRootPath() + "documents/" + document.getPath() + document.getName();

        Blob blob = googleCloudStorageService.getBlobByPath(path);
        if(blob == null){
            return ResponseEntity.notFound().build();
        }
        byte[] bytes = blob.getContent();
        ByteArrayResource resource = new ByteArrayResource(bytes);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + document.getName());
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(bytes.length)
                .contentType(MediaType.parseMediaType(document.getContentType()))
                .body(resource);
    }

    @GetMapping("test")
    public ResponseEntity<ByteArrayResource> getSourceByDocumentId(@RequestParam("path") String pathDocument){
        User user = userService.findUserByEmail("cuong.nnc184055@sis.hust.edu.vn");
        String path = user.getRootPath() + "documents/" + pathDocument + "function.pdf";

        Blob blob = googleCloudStorageService.getBlobByPath(path);
        if(blob == null){
            return ResponseEntity.notFound().build();
        }
        byte[] bytes = blob.getContent();
        ByteArrayResource resource = new ByteArrayResource(bytes);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + "function.pdf");
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(bytes.length)
                .contentType(MediaType.parseMediaType("application/pdf"))
                .body(resource);
    }
}
