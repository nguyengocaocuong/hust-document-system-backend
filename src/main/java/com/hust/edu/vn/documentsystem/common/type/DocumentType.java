package com.hust.edu.vn.documentsystem.common.type;

public enum DocumentType {
    FOLDER,
    LINK,
    PDF,
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

    public static DocumentType getDocumentTypeFromExtension(String fileExtension) {
        if (fileExtension.equalsIgnoreCase("pdf")) {
            return DocumentType.PDF;
        } else if (fileExtension.equalsIgnoreCase("doc")) {
            return DocumentType.DOC;
        } else if (fileExtension.equalsIgnoreCase("docx")) {
            return DocumentType.DOCX;
        } else if (fileExtension.equalsIgnoreCase("xlsx")) {
            return DocumentType.XLSX;
        } else if (fileExtension.equalsIgnoreCase("xls")) {
            return DocumentType.XLS;
        } else if (fileExtension.equalsIgnoreCase("jpg") || fileExtension.equalsIgnoreCase("jpeg")) {
            return DocumentType.JPG;
        } else if (fileExtension.equalsIgnoreCase("png")) {
            return DocumentType.PNG;
        } else if (fileExtension.equalsIgnoreCase("ppt")) {
            return DocumentType.PPT;
        } else if (fileExtension.equalsIgnoreCase("pptx")) {
            return DocumentType.PPTX;
        } else if (fileExtension.equalsIgnoreCase("csv")) {
            return DocumentType.CSV;
        } else if (fileExtension.equalsIgnoreCase("xml")) {
            return DocumentType.XML;
        } else if (fileExtension.equalsIgnoreCase("json")) {
            return DocumentType.JSON;
        } else if (fileExtension.equalsIgnoreCase("gif")) {
            return DocumentType.GIF;
        } else if (fileExtension.equalsIgnoreCase("svg")) {
            return DocumentType.SVG;
        } else if (fileExtension.equalsIgnoreCase("bmp")) {
            return DocumentType.BMP;
        } else if (fileExtension.equalsIgnoreCase("mp3")) {
            return DocumentType.MP3;
        } else if (fileExtension.equalsIgnoreCase("mp4")) {
            return DocumentType.MP4;
        } else {
            return DocumentType.UN_KNOW;
        }
    }

}
