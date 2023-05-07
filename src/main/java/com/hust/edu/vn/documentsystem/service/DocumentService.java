package com.hust.edu.vn.documentsystem.service;


import com.google.cloud.storage.Blob;
import com.hust.edu.vn.documentsystem.entity.Document;

public interface DocumentService {
    Iterable<Blob> getAllFileByDocumentId(Long documentId);

    Document findDocumentById(Long documentId);
}
