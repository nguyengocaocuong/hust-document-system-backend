package com.hust.edu.vn.documentsystem.controller.general;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.model.TranslateDocumentModel;
import com.hust.edu.vn.documentsystem.data.model.TranslateImageModel;
import com.hust.edu.vn.documentsystem.data.model.TranslateTextModel;
import com.hust.edu.vn.documentsystem.service.GoogleCloudStorageService;
import com.hust.edu.vn.documentsystem.service.GoogleCloudTranslateService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/generals/translates")
public class TranslateController {
    private final GoogleCloudTranslateService googleCloudTranslateService;
    private final GoogleCloudStorageService generateUriFromPath;

    @Autowired
    public TranslateController(
            GoogleCloudTranslateService googleCloudTranslateService,
            GoogleCloudStorageService generateUriFromPath) {
        this.googleCloudTranslateService = googleCloudTranslateService;
        this.generateUriFromPath = generateUriFromPath;
    }

    @PostMapping("text")
    public ResponseEntity<CustomResponse> translateText(@ModelAttribute TranslateTextModel translateTextModel) {
        Object translated = googleCloudTranslateService.translateText(translateTextModel.getText(), translateTextModel.getTargetLanguage());
        return CustomResponse.generateResponse(translated != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST, translated);
    }

    @PostMapping("image")
    public ResponseEntity<CustomResponse> translateTextFromImage(@ModelAttribute TranslateImageModel translateImageModel) {
        Object translated =  googleCloudTranslateService.translateTextFromImage(translateImageModel.getImage(), translateImageModel.getTargetLanguage());
        return CustomResponse.generateResponse(translated!= null? HttpStatus.OK : HttpStatus.BAD_REQUEST, translated);
    }

    @PostMapping("document")
    public ResponseEntity<CustomResponse> translateDocument(@ModelAttribute TranslateDocumentModel translateDocumentModel) {
        try {
            String path = googleCloudTranslateService.translateDocument(translateDocumentModel.getDocument(), translateDocumentModel.getTargetLanguage());
            return CustomResponse.generateResponse(HttpStatus.OK,generateUriFromPath.generateUriFromPath(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return CustomResponse.generateResponse(HttpStatus.BAD_REQUEST);
    }
    @GetMapping("test/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<ByteArrayResource> redirectView(@PathVariable("id") Long id, HttpServletResponse response) {

        String url = "https://storage.googleapis.com/hust-document-file/3bf914fb-6b5f-4708-ab6c-8550ad5b395e/documents/9eb3d342-9c85-43ba-a96f-57071110fc9b/function.pdf";
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<byte[]> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<byte[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, byte[].class);
        byte[] pdfBytes = responseEntity.getBody();

        assert pdfBytes != null;
        ByteArrayResource resource = new ByteArrayResource(pdfBytes);
        String filename = "function.pdf";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + filename)
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(pdfBytes.length)
                .body(resource);
    }
}
