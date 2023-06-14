package com.hust.edu.vn.documentsystem.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

public interface SignTextOrLogoImageToDocumentService {
     byte[] sign(MultipartFile file) throws IOException;

     byte[] signToPDF(InputStream inputStream) throws IOException;

     byte[] signToImage(InputStream inputStream) throws IOException;

     byte[] signToDocx(InputStream inputStream) throws IOException;

     byte[] signToXls(InputStream inputStream) throws IOException;

     byte[] signToPPTX(InputStream inputStream) throws IOException;

     byte[] signToPPT(InputStream inputStream) throws IOException;

     byte[] signImageToXlsx(InputStream inputStream) throws IOException;
}
