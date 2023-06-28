package com.hust.edu.vn.documentsystem.data.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class PageDto<T>  implements Serializable {
    private Long totalItems;
    private int totalPages;
    private List<T> items = new ArrayList<>();
}
