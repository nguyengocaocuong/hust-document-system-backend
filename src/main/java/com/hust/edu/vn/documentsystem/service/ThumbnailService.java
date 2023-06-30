package com.hust.edu.vn.documentsystem.service;

import co.elastic.thumbnails4j.core.ThumbnailingException;
import com.itextpdf.text.DocumentException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ThumbnailService {
    byte[] generateThumbnail(MultipartFile file) throws ThumbnailingException, IOException, DocumentException;

    byte[] generateThumbnailForImage(MultipartFile image) throws ThumbnailingException, IOException;

    byte[] generateThumbnailForPDF(MultipartFile pdf) throws ThumbnailingException, IOException;

    byte[] generateThumbnailForDoc(MultipartFile doc) throws ThumbnailingException, IOException;

    byte[] generateThumbnailForDocx(MultipartFile docx) throws ThumbnailingException, IOException;

    byte[] generateThumbnailForXls(MultipartFile xls) throws ThumbnailingException, IOException;

    byte[] generateThumbnailForXlsx(MultipartFile xlsx) throws ThumbnailingException, IOException;

    byte[] generateThumbnailForPptx(MultipartFile pptx) throws ThumbnailingException, IOException;

    byte[] generateThumbnailForTxt(MultipartFile txt) throws DocumentException, IOException, ThumbnailingException;

}
