package com.hust.edu.vn.documentsystem.data.model;

import com.hust.edu.vn.documentsystem.common.type.ShareType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShareSubjectDocumentModel {
    @NotNull
    private Long documentId;

    @NotNull
    private ShareType shareType;

    private List<Long> userIds;

    private long durationSecond;
}
