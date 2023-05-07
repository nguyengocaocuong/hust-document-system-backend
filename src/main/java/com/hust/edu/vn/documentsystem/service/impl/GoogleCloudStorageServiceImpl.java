package com.hust.edu.vn.documentsystem.service.impl;

import com.google.cloud.storage.*;
import com.hust.edu.vn.documentsystem.common.type.BlobType;
import com.hust.edu.vn.documentsystem.common.type.GoogleTranslateSupportType;
import com.hust.edu.vn.documentsystem.service.GoogleCloudStorageService;
import com.hust.edu.vn.documentsystem.service.ThumbnailService;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class GoogleCloudStorageServiceImpl implements GoogleCloudStorageService {
    private static final String END_FIX_THUMBNAIL = "_thumbnail";
    private static final String BUCKET_NAME = "hust-document-file";
    private final Storage storage;
    private final ThumbnailService thumbnailService;


    @Autowired
    public GoogleCloudStorageServiceImpl(Storage storage, ThumbnailService thumbnailService) {
        this.storage = storage;
        this.thumbnailService = thumbnailService;
    }

    @Override
    public String uploadAvatarToGCP(@NotNull MultipartFile avatar, @NotNull String rootPath) throws IOException {
        String filename = avatar.getOriginalFilename();
        if (filename == null)
            return null;
        String filenamePath = rootPath + "public/avatar" + filename.substring(filename.lastIndexOf("."));
        ArrayList<Acl> owner = new ArrayList<>();
        owner.add(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));
        return uploadToGCP(avatar.getBytes(), avatar.getContentType(), filenamePath, owner);
    }

    @Override
    public boolean uploadDocumentToGCP(MultipartFile document, String rootPath) throws IOException {
        String name = document.getOriginalFilename();
        String filename = document.getOriginalFilename();
        if (filename == null)
            return false;
        byte[] thumbnailBytes = switch (filename.substring(filename.lastIndexOf(".") + 1)) {
            case "pdf" -> thumbnailService.generateThumbnailForPDF(document);
            case "jpg", "jpeg", "png" -> thumbnailService.generateThumbnailForImage(document);
            case "docx" -> thumbnailService.generateThumbnailForDocx(document);
            case "xlsx" -> thumbnailService.generateThumbnailForXlsx(document);
            case "pptx" -> thumbnailService.generateThumbnailForPptx(document);
            case "xls" -> thumbnailService.generateThumbnailForXls(document);
            case "doc" -> thumbnailService.generateThumbnailForDoc(document);
            default -> thumbnailService.generateThumbnailForTxt(document);
        };
        ArrayList<Acl> owner = new ArrayList<>();
        owner.add(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));
        String thumbnailPath = rootPath +  name.substring(0, name.lastIndexOf(".")) + END_FIX_THUMBNAIL + ".jpeg";
        uploadToGCP(
                thumbnailBytes,
                "image/jpeg",
                thumbnailPath,
                owner
        );
        String documentPath = rootPath + name;
        uploadToGCP(
                document.getBytes(),
                document.getContentType(),
                documentPath,
                null
        );
        return true;

    }

    @Override
    public boolean createFolderForUser(String rootPath) {
        String documentRootPath = rootPath + "documents/";
        String publicRootPath = rootPath + "public/";
        BlobType blobRootType = checkPathType(rootPath);
        BlobType blobDocumentType = checkPathType(documentRootPath);
        BlobType blobPublicType = checkPathType(publicRootPath);
        if (blobRootType != BlobType.UN_KNOW || blobDocumentType != BlobType.UN_KNOW || blobPublicType != BlobType.UN_KNOW)
            return true;
        BlobInfo bucketRootInfo = BlobInfo
                .newBuilder(BUCKET_NAME, rootPath)
                .setContentType(GoogleTranslateSupportType.FOLDER.getMimeType())
                .build();
        BlobInfo bucketDocumentInfo = BlobInfo
                .newBuilder(BUCKET_NAME, documentRootPath)
                .setContentType(GoogleTranslateSupportType.FOLDER.getMimeType())
                .build();
        BlobInfo bucketPublicInfo = BlobInfo
                .newBuilder(BUCKET_NAME, publicRootPath)
                .setContentType(GoogleTranslateSupportType.FOLDER.getMimeType())
                .build();
        Blob blobRoot = storage.create(bucketRootInfo);
        Blob blobDocument = storage.create(bucketDocumentInfo);
        Blob blobPublic = storage.create(bucketPublicInfo);
        return blobRoot != null && blobDocument != null && blobPublic != null;
    }

    @Override
    public String uploadDocumentsToGCP(MultipartFile[] documents, String rootPath) throws IOException {
        String path = UUID.randomUUID() + "/";
        String childPath = rootPath + "documents/" + path;
        for (MultipartFile document : documents) {
            uploadDocumentToGCP(document, childPath);
        }
        return path;
    }

    @Override
    public boolean setAclForAccessBlob(Acl owner, String path) {
        BlobId blobId = BlobId.of(BUCKET_NAME, path);
        storage.createAcl(blobId, owner);
        return false;
    }

    private String uploadToGCP(byte[] data, String contentType, String filenamePath, ArrayList<Acl> owner) {
        BlobId blobId = BlobId.of(BUCKET_NAME, filenamePath);
        log.info(filenamePath);
        BlobInfo.Builder blobInfoBuilder = BlobInfo.newBuilder(blobId)
                .setContentType(contentType);
        if (owner != null)
            blobInfoBuilder.setAcl(owner);
        BlobInfo blobInfo = blobInfoBuilder.build();

        storage.create(blobInfo, data);
        return "https://storage.googleapis.com/" + BUCKET_NAME + "/" + filenamePath;
    }

    @Override
    public URL generatePublicUriForAccess(String fileName, long durationSeconds) {
        Blob blob = storage.get(BUCKET_NAME, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blob.getBlobId()).build();
        return storage.signUrl(
                blobInfo,
                durationSeconds,
                TimeUnit.SECONDS,
                Storage.SignUrlOption.httpMethod(HttpMethod.GET),
                Storage.SignUrlOption.withV4Signature()
        );
    }

    @Override
    public URL generatePublicUriForAnyoneHasTokenCanAccessPath(String path, long durationSeconds, Map<String, String> accessHeaders) {
        BlobInfo blobInfo = BlobInfo.newBuilder(BUCKET_NAME, path).build();
        return storage.signUrl(
                blobInfo,
                durationSeconds,
                TimeUnit.SECONDS,
                Storage.SignUrlOption.httpMethod(HttpMethod.GET),
                Storage.SignUrlOption.withV4Signature(),
                Storage.SignUrlOption.withExtHeaders(accessHeaders)
        );
    }

    @Override
    public boolean deleteUlrSigned(String url) {
        BlobId blobId = BlobId.fromGsUtilUri(url);
        return storage.delete(blobId);
    }

    @Override
    public List<String> searchKeywordInAllBlob(String keyword, List<String> blobPrefixes) {
        return null;
    }

    @Override
    public boolean deleteDocumentByRootPath(String path) {
        BlobId blobId = BlobId.of(BUCKET_NAME, path);
        return storage.delete(blobId);
    }

    @Override
    public String getUrlDownload(String path, Long timeout) {
        BlobId blobId = BlobId.of(BUCKET_NAME, path);
        Blob blob = storage.get(blobId);
        return blob.signUrl(timeout, TimeUnit.MINUTES).toString();
    }

    @Override
    public boolean updateDocumentByRootPath(String path, MultipartFile document) throws IOException {
        var list = storage.list(BUCKET_NAME, Storage.BlobListOption.prefix(path),Storage.BlobListOption.currentDirectory(), Storage.BlobListOption.fields(Storage.BlobField.NAME)).getValues();
        list.forEach(value -> deleteDocumentByRootPath(value.getName()));
        uploadDocumentToGCP(document,path);
        return true;
    }

    @Override
    public boolean deleteAllDocumentByRootPath(String rootPath) {
        Iterable<Blob> blobs = storage.list(BUCKET_NAME, Storage.BlobListOption.prefix(rootPath)).getValues();
        for (Blob blob : blobs) {
            storage.delete(blob.getBlobId());
        }
        return true;
    }

    @Override
    public Iterable<Blob> getBlobListByPath(String rootPath) {
        return storage.list(BUCKET_NAME, Storage.BlobListOption.fields(Storage.BlobField.NAME, Storage.BlobField.CONTENT_TYPE), Storage.BlobListOption.prefix(rootPath)).getValues();
    }

    @Override
    public Blob getBlobByPath(String path) {
        return storage.get(BlobId.of(BUCKET_NAME, path));
    }

    @Override
    public String generateUriFromPath(String path) {
        return "https://storage.googleapis.com/" + BUCKET_NAME + "/" + path;
    }

    private BlobType checkPathType(String path) {
        Blob blob = storage.get(BUCKET_NAME, path);
        if (blob == null)
            return BlobType.UN_KNOW;
        return blob.isDirectory() ? BlobType.FOLDER : BlobType.FILE;
    }
}
