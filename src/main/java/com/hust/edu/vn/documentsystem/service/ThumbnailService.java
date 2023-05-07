package com.hust.edu.vn.documentsystem.service;

import org.springframework.web.multipart.MultipartFile;

public interface ThumbnailService {

    byte[] generateThumbnailForImage(MultipartFile image);

    byte[] generateThumbnailForPDF(MultipartFile pdf);

    byte[] generateThumbnailForDoc(MultipartFile doc);

    byte[] generateThumbnailForDocx(MultipartFile docx);

    byte[] generateThumbnailForXls(MultipartFile xls);

    byte[] generateThumbnailForXlsx(MultipartFile xlsx);


    byte[] generateThumbnailForPptx(MultipartFile pptx);

    byte[] generateThumbnailForTxt(MultipartFile txt);

}
