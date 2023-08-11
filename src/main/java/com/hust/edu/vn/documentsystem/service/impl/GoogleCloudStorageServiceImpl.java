package com.hust.edu.vn.documentsystem.service.impl;

import co.elastic.thumbnails4j.core.ThumbnailingException;
import com.google.cloud.storage.*;
import com.hust.edu.vn.documentsystem.service.GoogleCloudStorageService;
import com.hust.edu.vn.documentsystem.service.ThumbnailService;
import com.itextpdf.text.DocumentException;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

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
    private static final String THUMBNAIL = "thumbnail";
    private static final String BUCKET_NAME = System.getenv("BUCKET_NAME");
    private final Storage storage;
    private final ThumbnailService thumbnailService;

    
    public GoogleCloudStorageServiceImpl(Storage storage, ThumbnailService thumbnailService) {
        this.storage = storage;
        this.thumbnailService = thumbnailService;
    }

    @Override
    public String uploadAvatarToGCP(@NotNull MultipartFile avatar) throws IOException {
        String filename = avatar.getOriginalFilename();
        if (filename == null)
            return null;
        String filenamePath = UUID.randomUUID() + "/" +   filename.substring(filename.lastIndexOf("."));
        ArrayList<Acl> owner = new ArrayList<>();
        owner.add(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));
        return uploadToGCP(avatar.getBytes(), avatar.getContentType(), filenamePath, owner);
    }

    @Override
    public boolean uploadDocumentToGCP(MultipartFile document, String rootPath)
            throws Exception {
        String name = document.getOriginalFilename();
        String filename = document.getOriginalFilename();
        if (filename == null)
            return false;
        byte[] thumbnailBytes = thumbnailService.generateThumbnail(document);
        ArrayList<Acl> owner = new ArrayList<>();
        owner.add(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));
        String thumbnailPath = rootPath + name.substring(0, name.lastIndexOf(".")) + THUMBNAIL + ".jpeg";
        uploadToGCP(
                thumbnailBytes,
                "image/jpeg",
                thumbnailPath,
                owner);
        String documentPath = rootPath + name;
        uploadToGCP(
                document.getBytes(),
                document.getContentType(),
                documentPath,
                null);
        return true;

    }


    @Override
    public String uploadDocumentsToGCP(MultipartFile[] documents, String rootPath)
            throws Exception {
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
                Storage.SignUrlOption.withV4Signature());
    }

    @Override
    public URL generatePublicUriForAnyoneHasTokenCanAccessPath(String path, long durationSeconds,
            Map<String, String> accessHeaders) {
        BlobInfo blobInfo = BlobInfo.newBuilder(BUCKET_NAME, path).build();
        return storage.signUrl(
                blobInfo,
                durationSeconds,
                TimeUnit.SECONDS,
                Storage.SignUrlOption.httpMethod(HttpMethod.GET),
                Storage.SignUrlOption.withV4Signature(),
                Storage.SignUrlOption.withExtHeaders(accessHeaders));
    }

    @Override
    public boolean deleteUlrSigned(String url) {
        BlobId blobId = BlobId.fromGsUtilUri(url);
        return storage.delete(blobId);
    }


    @Override
    public boolean deleteDocumentByRootPath(String path) {
        BlobId blobId = BlobId.of(BUCKET_NAME, path);
        return storage.delete(blobId);
    }

    @Override
    public boolean updateDocumentByRootPath(String path, MultipartFile document)
            throws Exception {
        var list = storage.list(BUCKET_NAME, Storage.BlobListOption.prefix(path),
                Storage.BlobListOption.currentDirectory(), Storage.BlobListOption.fields(Storage.BlobField.NAME))
                .getValues();
        list.forEach(value -> deleteDocumentByRootPath(value.getName()));
        uploadDocumentToGCP(document, path);
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
        return storage.list(BUCKET_NAME,
                Storage.BlobListOption.fields(Storage.BlobField.NAME, Storage.BlobField.CONTENT_TYPE),
                Storage.BlobListOption.prefix(rootPath)).getValues();
    }

    @Override
    public Blob getBlobByPath(String path) {
        return storage.get(BlobId.of(BUCKET_NAME, path));
    }

    @Override
    public List<String> createThumbnailAndUploadDocumentToGCP(MultipartFile multipartFile, List<Acl> owner)
            throws IOException {
        String documentPath = UUID.randomUUID() + "/";
        String thumbnailPath = documentPath + THUMBNAIL + ".png";
        String filePath = documentPath + multipartFile.getOriginalFilename();
        ArrayList<Acl> ownerForThumbnail = new ArrayList<>();
        ownerForThumbnail.add(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));

        BlobId blobId = BlobId.of(BUCKET_NAME, filePath);
        BlobId blobThumbnailId = BlobId.of(BUCKET_NAME, thumbnailPath);

        BlobInfo.Builder blobInfoBuilder = BlobInfo.newBuilder(blobId)
                .setContentType(multipartFile.getContentType());
        BlobInfo.Builder blobThumbnailInfoBuilder = BlobInfo.newBuilder(blobThumbnailId)
                .setContentType("image/png");
        if (owner != null && !owner.isEmpty())
            blobInfoBuilder.setAcl(owner);
        blobThumbnailInfoBuilder.setAcl(ownerForThumbnail);

        BlobInfo blobThumbnailInfo = blobThumbnailInfoBuilder.build();
        BlobInfo blobInfo = blobInfoBuilder.build();
        storage.create(blobInfo, multipartFile.getInputStream().readAllBytes());
        try {
            byte[] thumbnail = thumbnailService.generateThumbnail(multipartFile);
            if (thumbnail != null && thumbnail.length > 0) {
                storage.create(blobThumbnailInfo, thumbnail);
                log.info(thumbnailPath);
                log.info(generateUriFromPath(thumbnailPath));
                return List.of((owner == null || owner.isEmpty()) ? filePath : generateUriFromPath(filePath),
                        generateUriFromPath(thumbnailPath));
            }
        } catch (ThumbnailingException e) {
            throw new RuntimeException(e);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return List.of((owner == null || owner.isEmpty()) ? filePath : generateUriFromPath(filePath));
    }

    @Override
    public byte[] readBlobByPath(String path) {
        Blob blob = storage.get(BlobId.of(BUCKET_NAME, path));
        return blob.getContent();
    }

    @Override
    public boolean deleteBlobByPath(String path) {
        return storage.delete(BlobId.of(BUCKET_NAME, path));
    }

    @Override
    public String uploadAnnotationToGCP(MultipartFile documentFile) throws IOException {
        String filenamePath = UUID.randomUUID() + "/" + documentFile.getOriginalFilename();
        BlobId blobId = BlobId.of(BUCKET_NAME, filenamePath);
        BlobInfo.Builder blobInfoBuilder = BlobInfo.newBuilder(blobId)
                .setContentType(documentFile.getContentType());
        storage.create(blobInfoBuilder.build(), documentFile.getInputStream().readAllBytes());

        return filenamePath;
    }

    @Override
    public String generateUriFromPath(String path) {
        return "https://storage.googleapis.com/" + BUCKET_NAME + "/" + path;
    }

}
