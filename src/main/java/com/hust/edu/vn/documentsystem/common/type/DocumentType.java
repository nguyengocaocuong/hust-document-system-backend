package com.hust.edu.vn.documentsystem.common.type;

public enum DocumentType {
    FOLDER,
    LINK,
    PDF,
    ANNOTATE,
    DOC,
    DOCX,
    XLSX,
    XLS,
    JPG,
    PNG,
    PPT,
    PPTX,
    CSV,
    XML,
    JSON,
    GIF,
    SVG,
    BMP,
    MP3,
    MP4,
    UN_KNOW;

    public static DocumentType getDocumentTypeFromContentType(String contentType) {
        switch (contentType) {
            case "application/pdf":
                return DocumentType.PDF;
            case "application/msword":
                return DocumentType.DOC;
            case "application/vnd.openxmlformats-officedocument.wordprocessingml.document":
                return DocumentType.DOCX;
            case "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet":
                return DocumentType.XLSX;
            case "application/vnd.ms-excel":
                return DocumentType.XLS;
            case "image/jpeg":
            case "image/jpg":
                return DocumentType.JPG;
            case "image/png":
                return DocumentType.PNG;
            case "application/vnd.ms-powerpoint":
                return DocumentType.PPT;
            case "application/vnd.openxmlformats-officedocument.presentationml.presentation":
                return DocumentType.PPTX;
            case "text/csv":
                return DocumentType.CSV;
            case "application/xml":
                return DocumentType.XML;
            case "application/json":
                return DocumentType.JSON;
            case "image/gif":
                return DocumentType.GIF;
            case "image/svg+xml":
                return DocumentType.SVG;
            case "application/vnd.adobe.xfdf":
                return DocumentType.ANNOTATE;
            case "image/bmp":
                return DocumentType.BMP;
            case "audio/mp3":
                return DocumentType.MP3;
            case "video/mp4":
                return DocumentType.MP4;
            default:
                return DocumentType.UN_KNOW;
        }
    }
}
