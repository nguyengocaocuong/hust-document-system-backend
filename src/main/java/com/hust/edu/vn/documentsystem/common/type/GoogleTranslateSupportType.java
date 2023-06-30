package com.hust.edu.vn.documentsystem.common.type;

public enum GoogleTranslateSupportType {
    FOLDER("application/x-www-form-urlencoded;charset=UTF-8", "UN_KNOW"),
    DOC("application/msword", "DOC,DOCX"),
    DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document", "DOCX"),
    PDF("application/pdf", "PDF,DOCX"),
    PPT("application/vnd.ms-powerpoint", "PPT,PPTX"),
    PPTX("application/vnd.openxmlformats-officedocument.presentationml.presentation", "PPTX"),
    XLS("application/vnd.ms-excel", "XLS,XLSX"),
    XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "XLSX");

    private final String mimeType;
    private final String outputType;

    GoogleTranslateSupportType(String mimeType, String outputType) {
        this.mimeType = mimeType;
        this.outputType = outputType;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public String getOutputType() {
        return this.outputType;
    }
}
