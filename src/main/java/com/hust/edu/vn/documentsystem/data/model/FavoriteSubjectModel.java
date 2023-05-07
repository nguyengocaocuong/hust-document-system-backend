package com.hust.edu.vn.documentsystem.data.model;

import com.hust.edu.vn.documentsystem.common.type.NotificationType;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteSubjectModel {
    private Long id;
    private Long subjectId;
    private NotificationType notificationType = NotificationType.ALL;
}
