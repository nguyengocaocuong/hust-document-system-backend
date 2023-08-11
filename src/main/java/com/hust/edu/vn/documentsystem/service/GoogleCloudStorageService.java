package com.hust.edu.vn.documentsystem.service;

import co.elastic.thumbnails4j.core.ThumbnailingException;
import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Blob;
import com.itextpdf.text.DocumentException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public interface GoogleCloudStorageService {
    String uploadAvatarToGCP(MultipartFile avatar) throws IOException;
    boolean uploadDocumentToGCP(MultipartFile document, String rootPath) throws Exception;

    String uploadDocumentsToGCP(MultipartFile[] document, String rootPath) throws Exception;
    boolean setAclForAccessBlob(Acl owner, String path);

    URL generatePublicUriForAccess(String filename, long durationSeconds);

    URL generatePublicUriForAnyoneHasTokenCanAccessPath(String path, long durationSeconds, Map<String,String> accessHeaders);

    String generateUriFromPath(String path);

     boolean deleteUlrSigned(String url);


    boolean deleteDocumentByRootPath(String s);


    boolean updateDocumentByRootPath(String path, MultipartFile document) throws Exception;

    boolean deleteAllDocumentByRootPath(String substring);

    Iterable<Blob> getBlobListByPath(String path);

    Blob getBlobByPath(String path);

    List<String> createThumbnailAndUploadDocumentToGCP(MultipartFile multipartFile, List<Acl> owner) throws IOException;

    byte[] readBlobByPath(String path);

    boolean deleteBlobByPath(String path);

    String uploadAnnotationToGCP(MultipartFile documentFile) throws IOException;
}
