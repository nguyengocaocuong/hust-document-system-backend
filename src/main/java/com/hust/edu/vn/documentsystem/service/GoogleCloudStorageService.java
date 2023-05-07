package com.hust.edu.vn.documentsystem.service;

import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Blob;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public interface GoogleCloudStorageService {
    String uploadAvatarToGCP(MultipartFile avatar, String rootPath) throws IOException;
    boolean uploadDocumentToGCP(MultipartFile document, String rootPath) throws IOException;

    boolean createFolderForUser(String email);
    String uploadDocumentsToGCP(MultipartFile[] document, String rootPath) throws IOException;
    boolean setAclForAccessBlob(Acl owner, String path);

    URL generatePublicUriForAccess(String filename, long durationSeconds);

    URL generatePublicUriForAnyoneHasTokenCanAccessPath(String path, long durationSeconds, Map<String,String> accessHeaders);

    String generateUriFromPath(String path);

     boolean deleteUlrSigned(String url);

     List<String> searchKeywordInAllBlob(String keyword, List<String> blobPrefixes);

    boolean deleteDocumentByRootPath(String s);

    String getUrlDownload(String path, Long timeout);

    boolean updateDocumentByRootPath(String path, MultipartFile document)  throws IOException ;

    boolean deleteAllDocumentByRootPath(String substring);

    Iterable<Blob> getBlobListByPath(String path);

    Blob getBlobByPath(String path);
}
