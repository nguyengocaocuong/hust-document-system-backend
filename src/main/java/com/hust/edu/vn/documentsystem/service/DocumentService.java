package com.hust.edu.vn.documentsystem.service;


import co.elastic.thumbnails4j.core.ThumbnailingException;
import com.google.cloud.storage.Blob;
import com.hust.edu.vn.documentsystem.entity.Document;
import com.itextpdf.text.DocumentException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface DocumentService {
    Document savePrivateDocumentToGoogleCloud(MultipartFile[] documents) throws DocumentException, ThumbnailingException, IOException;
    Document savePublicDocumentToGoogleCloud(MultipartFile[] documents) throws IOException;
}
