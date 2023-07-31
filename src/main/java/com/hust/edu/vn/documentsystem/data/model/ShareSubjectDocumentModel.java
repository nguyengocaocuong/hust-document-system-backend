package com.hust.edu.vn.documentsystem.data.model;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShareSubjectDocumentModel {
    private List<Long> shareUserId;
    private List<Long> deleteUserId;
}
