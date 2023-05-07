package com.hust.edu.vn.documentsystem.data.dto;

import com.hust.edu.vn.documentsystem.common.type.DocumentType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Setter
@Getter
@ToString
public class BlobDto implements Serializable {
    private DocumentType documentType;
    private String contentType;
    private String path;
    private String name;
    List<BlobDto> children = new ArrayList<BlobDto>();


}


