package com.hust.edu.vn.documentsystem.data.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class PageDto<T>  implements Serializable {
    private Long totalItems;
    private int totalPages;
    private List<T> items = new ArrayList<>();
}
